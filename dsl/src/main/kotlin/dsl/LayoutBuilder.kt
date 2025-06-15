package dsl

import model.*

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