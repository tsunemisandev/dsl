import java.io.File

import dsl.*
import model.*
import template.*
import generator.*
import pages.Page2.Meta


fun main() {
    val pageRoot = "/home/kombu/Documents/dsl/vue3-proect/test-front/pages/Page2"
    val componentsDir = "$pageRoot/components"
    val typesDir = "$pageRoot/types"
    val docsDir = "$pageRoot/docs"

    fun ensureDirs(vararg dirs: String) = dirs.forEach { File(it).mkdirs() }

    ensureDirs(componentsDir, typesDir, docsDir)

    // ---- Define Layout with Tabs (Optional) ----
    val tabArea = layout("TabArea", "顧客検索・一覧") {
        tab {
            tabPane("検索") {
                row {
                    col(span = 8) { input(Meta.Fields.CustomerId) }
                    col(span = 8) { input(Meta.Fields.CustomerName) }
                    col(span = 8) { button(Meta.Fields.Search) }
                }
            }
            tabPane("一覧") {
                row {
                    col(span = 24) { table(Meta.Tables.CustomerTable) }
                }
            }
        }
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
