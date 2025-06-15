package model

data class EventMeta(
    val trigger: String,
    val handler: String,
    val description: String? = null
)
