package generator

import dsl.*
import model.*
import template.ComponentTemplateProvider
import java.io.File

fun renderPart(part: LayoutPart, provider: ComponentTemplateProvider): String = when (part) {
    is InputText -> "<${part.meta.id} />"
    is Button -> "<${part.meta.id} />"
    is Table -> "<${part.meta.id} />"
    is SubLayout -> "<${part.layout.id} />"
    is Row -> provider.row(part.cols.joinToString("") { renderPart(it, provider) })
    is Col -> provider.col(part.span, part.parts.joinToString("") { renderPart(it, provider) })
    is Tab -> provider.tab(part.panes.joinToString("") { renderPart(it, provider) })
    is TabPane -> provider.tabPane(part.label, part.parts.joinToString("") { renderPart(it, provider) })
    else -> ""
}
/**
 * 全パート（InputText/Button/Table etc.）を再帰的に走査し、
 * 必要なVue SFCファイルを生成
 */
fun generateComponentFiles(
    node: LayoutPart,
    dir: File,
    templateProvider: ComponentTemplateProvider,
    already: MutableSet<String>
) {
    when (node) {
        is InputText -> if (already.add(node.meta.id)) {
            File(dir, "${node.meta.id}.vue").writeText(templateProvider.inputText(node.meta))
        }
        is Button -> if (already.add(node.meta.id)) {
            File(dir, "${node.meta.id}.vue").writeText(templateProvider.button(node.meta))
        }
        is Table -> if (already.add(node.meta.id)) {
            File(dir, "${node.meta.id}.vue").writeText(templateProvider.table(node.meta))
        }
    }
    // 子ノードも再帰
    if (node is LayoutNode) node.children.forEach { generateComponentFiles(it, dir, templateProvider, already) }
}

/**
 * 使いやすいツリーtoDocFieldInfo変換（TabなどもOK）
 */
fun toDocFields(node: LayoutPart): List<DocFieldInfo> = when (node) {
    is InputText -> listOf(DocFieldInfo(
        id = node.meta.id,
        label = node.meta.label,
        type = "InputText",
        events = node.meta.events?.map { DocEventInfo(it.trigger, it.handler, it.description) }
    ))
    is Button -> listOf(DocFieldInfo(
        id = node.meta.id,
        label = node.meta.label,
        type = "Button",
        events = node.meta.events?.map { DocEventInfo(it.trigger, it.handler, it.description) }
    ))
    is Table -> listOf(DocFieldInfo(
        id = node.meta.id,
        label = node.meta.label,
        type = "Table",
        columns = node.meta.columns.map {
            when (it) {
                is TableColumnMeta.Text -> DocTableColumnInfo(it.id, it.label, "Text", it.editable, it.length)
                is TableColumnMeta.Number -> DocTableColumnInfo(it.id, it.label, "Number", it.editable, it.length)
                is TableColumnMeta.Date -> DocTableColumnInfo(it.id, it.label, "Date", it.editable)
                is TableColumnMeta.Select -> DocTableColumnInfo(it.id, it.label, "Select", it.editable, options = it.options)
            }
        }
    ))
    is Row, is Col, is SubLayout ->
        if (node is LayoutNode) node.children.flatMap { toDocFields(it) } else emptyList()
    is Tab -> listOf(DocFieldInfo(
        id = node.hashCode().toString(),
        label = "Tab",
        type = "Tab",
        columns = (node as Tab).children.map {
            if (it is TabPane) DocTableColumnInfo(it.hashCode().toString(), it.label, "TabPane", false) else null
        }.filterNotNull()
    )) + node.children.flatMap { toDocFields(it) }
    is TabPane -> listOf(DocFieldInfo(
        id = node.hashCode().toString(),
        label = "Tab: ${(node as TabPane).label}",
        type = "TabPane"
    )) + node.children.flatMap { toDocFields(it) }
    else -> emptyList()
}

fun collectImports(node: LayoutPart): Set<String> = when (node) {
    is InputText -> setOf(node.meta.id)
    is Button -> setOf(node.meta.id)
    is Table -> setOf(node.meta.id)
    is SubLayout -> setOf(node.layout.id)
    is LayoutNode -> node.children.flatMap { collectImports(it) }.toSet()
    else -> emptySet()
}

/**
 * Layout → Vueファイル・ドキュメント生成（TabやRow, サブレイアウトも再帰的に対応）
 */
fun generateVueComponents(
    layout: Layout,
    outputDir: String,
    templateProvider: ComponentTemplateProvider,
    generated: MutableSet<String> = mutableSetOf()
): DocLayoutInfo {
    val dir = File(outputDir)
    if (!dir.exists()) dir.mkdirs()
    if (!generated.add(layout.id)) {
        return DocLayoutInfo(layout.id, layout.pageTitle, templateProvider.name, emptyList(), emptyList())
    }

    // 全SFC生成
    val already = mutableSetOf<String>()
    layout.parts.forEach { generateComponentFiles(it, dir, templateProvider, already) }

    // ドキュメント用ノードツリー取得
    val fieldInfos = layout.parts.flatMap { toDocFields(it) }

    // インポート集計
    val imports = layout.parts.flatMap { collectImports(it) }.toSet()

    val layoutCode = buildString {
        appendLine("<template>")
        appendLine("  <div>")
        appendLine("    <h1>${layout.pageTitle}</h1>")
        layout.parts.forEach { part ->
            appendLine(renderPart(part, templateProvider))
        }
        appendLine("  </div>")
        appendLine("</template>")
        appendLine("<script setup>")
        imports.forEach { imp -> appendLine("import $imp from './$imp.vue'") }
        appendLine("</script>")
    }

    File(dir, "${layout.id}.vue").writeText(layoutCode)
    println("Generated: ${layout.id}.vue in $outputDir")

    return DocLayoutInfo(
        id = layout.id,
        title = layout.pageTitle,
        templateProvider = templateProvider.name,
        fields = fieldInfos,
        subLayouts = emptyList() // サブレイアウトがあれば再帰で追加も可
    )
}
