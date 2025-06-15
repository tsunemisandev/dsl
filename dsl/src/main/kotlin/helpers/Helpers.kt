package helpers

import java.io.File

fun setupPageScaffold(pageName: String) {
    val base = "src/main/kotlin/pages/$pageName"
    val componentsDir = "$base/components"

    File(componentsDir).mkdirs()

    val files = listOf(
        "$base/Meta.kt",
        "$base/Main.kt"
        // More files can be added here as needed
    )
    files.forEach { path ->
        val file = File(path)
        if (!file.exists()) {
            file.writeText("") // You can write template content here
            println("Created $path")
        } else {
            println("$path already exists")
        }
    }
    println("Created directory structure for page: $pageName")
}

fun main() {
    setupPageScaffold("Page2")

}