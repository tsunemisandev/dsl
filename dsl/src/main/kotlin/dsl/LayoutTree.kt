package dsl

import model.*

/** ノード共通インターフェース（木構造として走査可能） */
interface LayoutNode {
    val children: List<LayoutPart>
}

/** 各UIパート定義：全てchildren持つ or emptyList返す */
open class InputText(val meta: FieldMeta) : LayoutPart, LayoutNode {
    override val children: List<LayoutPart> get() = emptyList()
}
open class Button(val meta: FieldMeta) : LayoutPart, LayoutNode {
    override val children: List<LayoutPart> get() = emptyList()
}
open class Table(val meta: TableMeta) : LayoutPart, LayoutNode {
    override val children: List<LayoutPart> get() = emptyList()
}
open class Row(val cols: List<Col>) : LayoutPart, LayoutNode {
    override val children: List<LayoutPart> get() = cols
}
open class Col(val span: Int, val parts: List<LayoutPart>) : LayoutPart, LayoutNode {
    override val children: List<LayoutPart> get() = parts
}
open class Tab(val panes: List<TabPane>) : LayoutPart, LayoutNode {
    override val children: List<LayoutPart> get() = panes
}
open class TabPane(val label: String, val parts: List<LayoutPart>) : LayoutPart, LayoutNode {
    override val children: List<LayoutPart> get() = parts
}
open class SubLayout(val layout: Layout) : LayoutPart, LayoutNode {
    override val children: List<LayoutPart> get() = layout.parts
}

/** レイアウト全体（ルート） */
class Layout(val id: String, val pageTitle: String, val parts: List<LayoutPart>)

/** DSLビルダー */
class LayoutBuilder(val id: String, val pageTitle: String) {
    val parts = mutableListOf<LayoutPart>()
    fun input(fieldMeta: FieldMeta) = parts.add(InputText(fieldMeta))
    fun button(fieldMeta: FieldMeta) = parts.add(Button(fieldMeta))
    fun table(tableMeta: TableMeta) = parts.add(Table(tableMeta))
    fun include(subLayout: Layout) = parts.add(SubLayout(subLayout))
    fun row(block: RowBuilder.() -> Unit) {
        val rowBuilder = RowBuilder()
        rowBuilder.block()
        parts.add(Row(rowBuilder.cols))
    }
    fun tab(block: TabBuilder.() -> Unit) {
        val tabBuilder = TabBuilder()
        tabBuilder.block()
        parts.add(Tab(tabBuilder.panes))
    }
}

class RowBuilder {
    val cols = mutableListOf<Col>()
    fun col(span: Int = 1, block: ColBuilder.() -> Unit) {
        val colBuilder = ColBuilder()
        colBuilder.block()
        cols.add(Col(span, colBuilder.parts))
    }
}

class ColBuilder {
    val parts = mutableListOf<LayoutPart>()
    fun input(fieldMeta: FieldMeta) = parts.add(InputText(fieldMeta))
    fun button(fieldMeta: FieldMeta) = parts.add(Button(fieldMeta))
    fun table(tableMeta: TableMeta) = parts.add(Table(tableMeta))
    fun include(subLayout: Layout) = parts.add(SubLayout(subLayout))
    fun row(block: RowBuilder.() -> Unit) {
        val rowBuilder = RowBuilder()
        rowBuilder.block()
        parts.add(Row(rowBuilder.cols))
    }
}

class TabBuilder {
    val panes = mutableListOf<TabPane>()
    fun tabPane(label: String, block: TabPaneBuilder.() -> Unit) {
        val builder = TabPaneBuilder()
        builder.block()
        panes.add(TabPane(label, builder.parts))
    }
}
class TabPaneBuilder {
    val parts = mutableListOf<LayoutPart>()
    fun input(fieldMeta: FieldMeta) = parts.add(InputText(fieldMeta))
    fun button(fieldMeta: FieldMeta) = parts.add(Button(fieldMeta))
    fun table(tableMeta: TableMeta) = parts.add(Table(tableMeta))
    fun include(subLayout: Layout) = parts.add(SubLayout(subLayout))
    fun row(block: RowBuilder.() -> Unit) {
        val rowBuilder = RowBuilder()
        rowBuilder.block()
        parts.add(Row(rowBuilder.cols))
    }
}

/** --- ツリー走査ユーティリティ --- */

// 深さ優先で全ノードに対してvisitコールバック
fun traverseTree(node: LayoutPart, level: Int = 0, visit: (LayoutPart, Int) -> Unit) {
    visit(node, level)
    if (node is LayoutNode) {
        node.children.forEach { traverseTree(it, level + 1, visit) }
    }
}

// Layout全体を走査
fun traverseLayout(layout: Layout, visit: (LayoutPart, Int) -> Unit) {
    layout.parts.forEach { traverseTree(it, 0, visit) }
}

/** --- 例: ノードツリーをprintする --- */
fun printLayoutTree(layout: Layout) {
    traverseLayout(layout) { node, level ->
        val indent = "  ".repeat(level)
        val label = when (node) {
            is InputText -> "InputText(${node.meta.id})"
            is Button -> "Button(${node.meta.id})"
            is Table -> "Table(${node.meta.id})"
            is Row -> "Row"
            is Col -> "Col(span=${node.span})"
            is Tab -> "Tab"
            is TabPane -> "TabPane(${node.label})"
            is SubLayout -> "SubLayout(${node.layout.id})"
            else -> node::class.simpleName ?: "?"
        }
        println("$indent- $label")
    }
}

/** --- 例: ノードツリーをJSON風に変換（実際の用途に応じて調整可） --- */
fun toJsonLike(node: LayoutPart): Map<String, Any?> {
    val base = mutableMapOf<String, Any?>("type" to node::class.simpleName)
    when (node) {
        is InputText -> {
            base["meta"] = node.meta.id
        }
        is Button -> {
            base["meta"] = node.meta.id
        }
        is Table -> {
            base["meta"] = node.meta.id
        }
        is Row -> {
            base["cols"] = node.children.map { toJsonLike(it) }
        }
        is Col -> {
            base["span"] = node.span
            base["parts"] = node.children.map { toJsonLike(it) }
        }
        is Tab -> {
            base["panes"] = node.children.map { toJsonLike(it) }
        }
        is TabPane -> {
            base["label"] = node.label
            base["parts"] = node.children.map { toJsonLike(it) }
        }
        is SubLayout -> {
            base["layoutId"] = node.layout.id
            base["parts"] = node.children.map { toJsonLike(it) }
        }
    }
    return base
}

fun toJsonLikeLayout(layout: Layout): Map<String, Any?> = mapOf(
    "id" to layout.id,
    "title" to layout.pageTitle,
    "parts" to layout.parts.map { toJsonLike(it) }
)

