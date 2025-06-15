package model

sealed class TableColumnMeta {
    abstract val id: String
    abstract val label: String
    abstract val editable: Boolean

    data class Text(
        override val id: String,
        override val label: String,
        override val editable: Boolean = true,
        val length: Int? = null
    ) : TableColumnMeta()

    data class Number(
        override val id: String,
        override val label: String,
        override val editable: Boolean = true,
        val length: Int? = null // for digits
    ) : TableColumnMeta()

    data class Date(
        override val id: String,
        override val label: String,
        override val editable: Boolean = true,
        val format: String? = null
    ) : TableColumnMeta()

    data class Select(
        override val id: String,
        override val label: String,
        override val editable: Boolean = true,
        val options: List<String>
    ) : TableColumnMeta()
}
