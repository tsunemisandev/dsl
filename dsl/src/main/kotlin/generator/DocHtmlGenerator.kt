package generator

import dsl.Button
import dsl.Col
import dsl.InputText
import dsl.Layout
import dsl.LayoutNode
import dsl.Row
import dsl.SubLayout
import dsl.Tab
import dsl.TabPane
import dsl.Table
import model.LayoutPart
import java.io.File

// Copy your doc html generator function here (with Tailwind/tooltips)
data class DocEventInfo(
    val trigger: String,
    val handler: String,
    val description: String?
)

data class DocTableColumnInfo(
    val id: String,
    val label: String,
    val type: String,
    val editable: Boolean,
    val length: Int? = null,
    val options: List<String>? = null
)

data class DocFieldInfo(
    val id: String,
    val label: String,
    val type: String,
    val columns: List<DocTableColumnInfo>? = null,
    val events: List<DocEventInfo>? = null
)

data class DocLayoutInfo(
    val id: String,
    val title: String,
    val templateProvider: String,
    val fields: List<DocFieldInfo>,
    val subLayouts: List<DocLayoutInfo>
)

fun writeHtmlDoc(
    root: DocLayoutInfo,
    outputDir: String
) {
    val html = buildString {
        appendLine(
            """
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>UI Component Documentation</title>
  <meta name="viewport" content="width=device-width,initial-scale=1">
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-50 text-slate-800">
  <div class="max-w-3xl mx-auto py-10 px-4">
    <h1 class="text-3xl font-bold mb-8 text-blue-700">UI Page & Component Catalog</h1>
"""
        )

        fun writeLayout(layout: DocLayoutInfo, level: Int = 0) {
            val indent = if (level == 0) "" else " ml-10"
            appendLine("""<div class="bg-white rounded-xl shadow p-6 mb-8 border-l-8 border-blue-400$indent">""")
            appendLine(
                """  <div class="flex items-center mb-2">
                <span class="text-xl font-semibold">${layout.title}</span>
                <span class="ml-4 text-gray-500 text-base">(${layout.id})</span>
                <span class="ml-3 px-3 py-1 bg-blue-400 text-white rounded-full text-xs">${layout.templateProvider}</span>
            </div>"""
            )
            if (layout.fields.isNotEmpty()) {
                appendLine(
                    """
                <div>
                <table class="table-auto w-full bg-slate-50 rounded mb-4 text-sm">
                  <thead>
                    <tr>
                      <th class="text-left py-2 px-3 bg-blue-50">Type</th>
                      <th class="text-left py-2 px-3 bg-blue-50">ID</th>
                      <th class="text-left py-2 px-3 bg-blue-50">Label</th>
                      <th class="text-left py-2 px-3 bg-blue-50">Columns</th>
                      <th class="text-left py-2 px-3 bg-blue-50">Events</th>
                    </tr>
                  </thead>
                  <tbody>
                """
                )
                layout.fields.forEach { field ->
                    appendLine("<tr>")
                    appendLine("<td class='py-1 px-2'><span class='px-2 py-1 rounded bg-indigo-100 text-indigo-700 font-bold text-xs'>${field.type}</span></td>")
                    appendLine("<td class='py-1 px-2'>${field.id}</td>")
                    appendLine("<td class='py-1 px-2'>${field.label}</td>")
                    // Columns (for tables)
                    if (field.columns != null) {
                        appendLine("<td class='py-1 px-2'>")
                        appendLine(
                            """
                          <table class='border border-blue-100 rounded w-full text-xs'>
                            <thead>
                              <tr>
                                <th class="py-1 px-2 bg-blue-50">ID</th>
                                <th class="py-1 px-2 bg-blue-50">Label</th>
                                <th class="py-1 px-2 bg-blue-50">Type</th>
                                <th class="py-1 px-2 bg-blue-50">Editable</th>
                              </tr>
                            </thead>
                            <tbody>
                        """
                        )
                        field.columns.forEach { col ->
                            appendLine(
                                """
                              <tr>
                                <td class='py-1 px-2'>${col.id}</td>
                                <td class='py-1 px-2'>${col.label}</td>
                                <td class='py-1 px-2'>${col.type}</td>
                                <td class='py-1 px-2'>${if (col.editable) "✔️" else "❌"}</td>
                              </tr>
                            """
                            )
                        }
                        appendLine("</tbody></table></td>")
                    } else {
                        appendLine("<td class='py-1 px-2'>-</td>")
                    }
                    // Events (with tooltip)
                    if (field.events != null && field.events.isNotEmpty()) {
                        appendLine("<td class='py-1 px-2'>")
                        field.events.forEach { event ->
                            appendLine(
                                """
                                <div class='mb-1'>
                                  <span
                                    class='inline-block bg-blue-100 text-blue-700 rounded px-2 py-0.5 text-xs font-mono'
                                    title="${event.description?.replace("\"", "&quot;") ?: ""}"
                                  >@${event.trigger}</span>
                                  <span class='ml-1'>${event.handler}</span>
                                </div>
                            """.trimIndent()
                            )
                        }
                        appendLine("</td>")
                    } else {
                        appendLine("<td class='py-1 px-2'>-</td>")
                    }
                    appendLine("</tr>")
                }
                appendLine(
                    """
                  </tbody>
                </table>
                </div>
                """
                )
            } else {
                appendLine("""<div class="text-gray-400 mb-2">(No fields/components in this layout)</div>""")
            }
            layout.subLayouts.forEach { writeLayout(it, level + 1) }
            appendLine("</div>")
        }

        writeLayout(root)
        appendLine(
            """
    <footer class="mt-10 text-right text-xs text-gray-400">
      Powered by Kotlin DSL Generator · ${java.time.LocalDateTime.now().toLocalDate()}
    </footer>
  </div>
</body>
</html>
        """.trimIndent()
        )
    }
    File(outputDir, "docs.html").writeText(html)
    println("Generated: docs.html in $outputDir")
}

fun writeDocHtmlWithGrouping(
    layouts: List<Layout>,
    outputDir: String
) {
    fun isLeaf(part: LayoutPart): Boolean =
        part is InputText || part is Button || part is Table

    // グループ名を積み重ねて走査。Row/Colは無視
    fun collectLeafsGrouped(
        part: LayoutPart,
        groupStack: List<String> = emptyList()
    ): List<Triple<List<String>, String, LayoutPart>> = when (part) {
        is Tab -> part.children.flatMap { collectLeafsGrouped(it, groupStack + listOf("Tab")) }
        is TabPane -> part.children.flatMap { collectLeafsGrouped(it, groupStack + listOf("Tab: ${part.label}")) }
        is SubLayout -> part.children.flatMap { collectLeafsGrouped(it, groupStack + listOf("SubLayout: ${part.layout.pageTitle}")) }
        is Row, is Col -> part.children.flatMap { collectLeafsGrouped(part=it, groupStack) } // 完全に無視
        else -> if (isLeaf(part)) {
            // 構成リーフ
            listOf(Triple(groupStack, when (part) {
                is InputText -> part.meta.id
                is Button -> part.meta.id
                is Table -> part.meta.id
                else -> part::class.simpleName ?: "?"
            }, part))
        } else if (part is LayoutNode) {
            part.children.flatMap { collectLeafsGrouped(it, groupStack) }
        } else emptyList()
    }

    val html = buildString {
        appendLine("""
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>UI構成ドキュメント</title>
  <meta name="viewport" content="width=device-width,initial-scale=1">
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-50 text-slate-800">
  <div class="max-w-3xl mx-auto py-10 px-4">
    <h1 class="text-2xl font-bold mb-8 text-blue-700">UIレイアウト構成ドキュメント</h1>
""")
        layouts.forEach { layout ->
            appendLine("""
            <section class="mb-12">
              <div class="mb-4 p-4 bg-blue-100 border-l-8 border-blue-400 shadow rounded-xl">
                <h2 class="text-xl font-bold text-blue-700 mb-2">${layout.pageTitle}</h2>
                <div class="text-gray-400 text-sm mb-2">${layout.id}</div>
              </div>
            """.trimIndent())

            // グループ階層ごとにまとめる
            fun renderGroupedTable(
                groupPath: List<String>,
                leafs: List<Triple<List<String>, String, LayoutPart>>,
                level: Int = 0
            ) {
                // このグループで抽出
                val inGroup = leafs.filter { it.first == groupPath }
                val subGroups = leafs.map { it.first }
                    .filter { it.size > groupPath.size && it.subList(0, groupPath.size) == groupPath }
                    .map { it[groupPath.size] }
                    .distinct()
                // 見出し
                if (groupPath.isNotEmpty()) {
                    val lastGroup = groupPath.last()
                    appendLine("<div class=\"mt-${if (level == 0) "0" else "6"} mb-2 text-lg font-bold text-blue-700\">${"　".repeat(level)}$lastGroup</div>")
                }
                // テーブル
                if (inGroup.isNotEmpty()) {
                    appendLine("""
                        <table class="table-auto w-full mb-4 shadow bg-white rounded-xl">
                          <thead>
                            <tr>
                              <th class="bg-blue-50 text-left py-2 px-4 rounded-tl-xl">構成</th>
                              <th class="bg-blue-50 text-left py-2 px-4">タイプ</th>
                            </tr>
                          </thead>
                          <tbody>
                    """.trimIndent())
                    inGroup.forEach { (_, id, part) ->
                        val label = when (part) {
                            is InputText -> "InputText <span class='text-xs text-gray-500'>(${part.meta.id})</span>"
                            is Button -> "Button <span class='text-xs text-gray-500'>(${part.meta.id})</span>"
                            is Table -> "Table <span class='text-xs text-gray-500'>(${part.meta.id})</span>"
                            else -> part::class.simpleName ?: "?"
                        }
                        val type = when (part) {
                            is InputText -> "InputText"
                            is Button -> "Button"
                            is Table -> "Table"
                            else -> part::class.simpleName ?: "?"
                        }
                        appendLine("""
                          <tr>
                            <td class="py-2 px-4 font-mono text-blue-800">$label</td>
                            <td class="py-2 px-4">$type</td>
                          </tr>
                        """.trimIndent())
                    }
                    appendLine("""
                          </tbody>
                        </table>
                    """.trimIndent())
                }
                // サブグループ
                subGroups.forEach { sg ->
                    renderGroupedTable(groupPath + listOf(sg), leafs, level + 1)
                }
            }

            val leafs = layout.parts.flatMap { collectLeafsGrouped(it, emptyList()) }
            renderGroupedTable(emptyList(), leafs)

            appendLine("</section>")
        }

        appendLine("""
      <footer class="mt-10 text-right text-xs text-gray-400">
        Powered by DSL Layout Generator · ${java.time.LocalDateTime.now().toLocalDate()}
      </footer>
    </div>
  </body>
</html>
        """.trimIndent())
    }
    File(outputDir, "docs.html").writeText(html)
    println("Generated: docs.html in $outputDir")
}
