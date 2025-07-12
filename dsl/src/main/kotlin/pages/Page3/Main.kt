package pages.Page3

import java.io.File

import dsl.*
import model.*
import template.*
import generator.*
import pages.Page3.Meta


fun main() {
    val pageRoot = "/home/kombu/Documents/dsl2/dsl/vue3-project/test-front/pages/Page3"
    val componentsDir = "$pageRoot/components"
    val typesDir = "$pageRoot/types"
    val docsDir = "$pageRoot/docs"

    fun ensureDirs(vararg dirs: String) = dirs.forEach { File(it).mkdirs() }

    ensureDirs(componentsDir, typesDir, docsDir)

    // ---- Define Layout with Tabs (Optional) ----
    val tabArea = layout("TabArea", "顧客検索・一覧") {
        input(Meta.Fields.CustomerId)
        input(Meta.Fields.CustomerName)
        button(Meta.Fields.Search)
    }

    val mainForm = layout(
        id = "CustomerList",
        pageTitle = Meta.pageTitle
    ) {
        include(tabArea)
    }

    val templateProvider = ElementPlusTemplate

    // ---- Generate Vue Components ----

    val docInfo = generateVueComponents(
        layout = mainForm,
        outputDir = componentsDir,
        templateProvider = templateProvider
    )

    // ---- Generate Docs ----

    writeHtmlDoc(docInfo, docsDir)

    // ---- Generate TypeScript Types ----

    val searchFields = listOf(
        Meta.Fields.CustomerId,
        Meta.Fields.CustomerName,
        Meta.Fields.Search
    )
    generateTypeScriptTypeFromFields("CustomerSearchForm", searchFields, typesDir)

    val tableFields = Meta.Tables.CustomerTable.columns.map { col ->
        FieldMeta(id = col.id, label = col.label)
    }
    generateTypeScriptTypeFromFields("CustomerTableRow", tableFields, typesDir)

    // ---- Generate TypeScript Event Handler Stubs ----

    generateTypeScriptEventStubs(
        fileName = "eventHandlers.ts",
        layouts = listOf(mainForm),
        outputDir = typesDir
    )

    println("All code, types, event stubs, and docs generated for page CustomerList at $pageRoot.")
}
