package model

data class TableMeta(
    val id: String,
    val label: String,
    val columns: List<TableColumnMeta>
)
