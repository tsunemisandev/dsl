package dsl

import model.*

class Layout(
    val id: String,
    val pageTitle: String,
    val parts: List<LayoutPart>
)

fun layout(
    id: String,
    pageTitle: String,
    block: LayoutBuilder.() -> Unit
): Layout {
    val builder = LayoutBuilder(id, pageTitle)
    builder.block()
    return Layout(id, pageTitle, builder.parts)
}
