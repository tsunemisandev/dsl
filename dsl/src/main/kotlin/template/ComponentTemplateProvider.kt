package template

import model.*

interface ComponentTemplateProvider {
    val name: String
    fun inputText(meta: FieldMeta): String
    fun button(meta: FieldMeta): String
    fun table(meta: TableMeta): String
    fun row(content: String): String
    fun col(span: Int, content: String): String
    fun tab(content: String): String
    fun tabPane(label: String, content: String): String
}
