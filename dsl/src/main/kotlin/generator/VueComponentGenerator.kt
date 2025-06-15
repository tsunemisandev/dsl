package generator

import dsl.Layout
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

// Recursively collect all component part nodes for Vue generation
fun collectComponentParts(part: LayoutPart): List<LayoutPart> = when (part) {
    is InputText, is Button, is Table -> listOf(part)
    is SubLayout -> collectComponentParts(part.layout)
    is Row -> part.cols.flatMap { collectComponentParts(it) }
    is Col -> part.parts.flatMap { collectComponentParts(it) }
    is Tab -> part.panes.flatMap { collectComponentParts(it) }
    is TabPane -> part.parts.flatMap { collectComponentParts(it) }
    else -> emptyList()
}
fun collectComponentParts(layout: Layout): List<LayoutPart> =
    layout.parts.flatMap { collectComponentParts(it) }

fun collectImports(part: LayoutPart): Set<String> = when (part) {
    is InputText -> setOf(part.meta.id)
    is Button -> setOf(part.meta.id)
    is Table -> setOf(part.meta.id)
    is SubLayout -> setOf(part.layout.id)
    is Row -> part.cols.flatMap { collectImports(it) }.toSet()
    is Col -> part.parts.flatMap { collectImports(it) }.toSet()
    is Tab -> part.panes.flatMap { collectImports(it) }.toSet()
    is TabPane -> part.parts.flatMap { collectImports(it) }.toSet()
    else -> emptySet()
}

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

    // --- New: Recursively generate all field/table component files (InputText, Button, Table) ---
    val allParts = collectComponentParts(layout)
    val alreadyGenerated = mutableSetOf<String>()
    allParts.forEach { part ->
        when (part) {
            is InputText -> {
                val id = part.meta.id
                if (alreadyGenerated.add(id)) {
                    File(dir, "$id.vue").writeText(templateProvider.inputText(part.meta))
                }
            }
            is Button -> {
                val id = part.meta.id
                if (alreadyGenerated.add(id)) {
                    File(dir, "$id.vue").writeText(templateProvider.button(part.meta))
                }
            }
            is Table -> {
                val id = part.meta.id
                if (alreadyGenerated.add(id)) {
                    File(dir, "$id.vue").writeText(templateProvider.table(part.meta))
                }
            }
        }
    }

    val fieldInfos = mutableListOf<DocFieldInfo>()
    val subLayoutInfos = mutableListOf<DocLayoutInfo>()

    layout.parts.forEach { part ->
        when (part) {
            is InputText -> {
                val id = part.meta.id
                fieldInfos.add(
                    DocFieldInfo(
                        id, part.meta.label, "InputText",
                        events = part.meta.events?.map { DocEventInfo(it.trigger, it.handler, it.description) }
                    ))
            }

            is Button -> {
                val id = part.meta.id
                fieldInfos.add(
                    DocFieldInfo(
                        id, part.meta.label, "Button",
                        events = part.meta.events?.map { DocEventInfo(it.trigger, it.handler, it.description) }
                    ))
            }

            is Table -> {
                val id = part.meta.id
                val columnsDoc = part.meta.columns.map { col ->
                    when (col) {
                        is TableColumnMeta.Text -> DocTableColumnInfo(
                            col.id,
                            col.label,
                            "Text",
                            col.editable,
                            col.length
                        )
                        is TableColumnMeta.Number -> DocTableColumnInfo(
                            col.id,
                            col.label,
                            "Number",
                            col.editable,
                            col.length
                        )
                        is TableColumnMeta.Date -> DocTableColumnInfo(col.id, col.label, "Date", col.editable, null)
                        is TableColumnMeta.Select -> DocTableColumnInfo(
                            col.id,
                            col.label,
                            "Select",
                            col.editable,
                            null,
                            col.options
                        )
                    }
                }
                fieldInfos.add(
                    DocFieldInfo(
                        id, part.meta.label, "Table",
                        columns = columnsDoc
                    )
                )
            }

            is SubLayout -> {
                val subInfo = generateVueComponents(part.layout, outputDir, templateProvider, generated)
                subLayoutInfos.add(subInfo)
            }
        }
    }

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
        // Gather all unique component imports (deep)
        val imports = layout.parts.flatMap { collectImports(it) }.toSet()
        imports.forEach { imp ->
            appendLine("import $imp from './$imp.vue'")
        }
        appendLine("</script>")
    }

    File(dir, "${layout.id}.vue").writeText(layoutCode)
    println("Generated: ${layout.id}.vue in $outputDir")

    return DocLayoutInfo(
        id = layout.id,
        title = layout.pageTitle,
        templateProvider = templateProvider.name,
        fields = fieldInfos,
        subLayouts = subLayoutInfos
    )
}
