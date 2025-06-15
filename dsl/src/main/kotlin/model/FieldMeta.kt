package model

data class FieldMeta(
    val id: String,
    val label: String,
    val length: Int? = null,
    val events: List<EventMeta>? = null
)
