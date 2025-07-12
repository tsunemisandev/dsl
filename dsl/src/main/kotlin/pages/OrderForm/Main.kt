package pages.OrderForm

import java.io.File

import dsl.*
import model.*
import template.*
import generator.*
import pages.OrderForm.Meta


fun main() {
    val pageRoot = "/home/kombu/Documents/dsl2/dsl/vue3-project/test-front/pages/page1"

    val componentsDir = "$pageRoot/components"
    val typesDir = "$pageRoot/types"
    val docsDir = "$pageRoot/docs"
    fun ensureDirs(vararg dirs: String) = dirs.forEach { File(it).mkdirs() }
    ensureDirs(componentsDir, typesDir, docsDir)

    // ---- Define UI Areas using Tabs, Rows, Cols ----

    val tabArea = layout("TabArea", "タブ付き画面") {
        tab {
            tabPane("検索") {
                row {
                    col(span = 8) { input(Meta.Fields.OrderNo) }
                    col(span = 4) { button(Meta.Fields.Search) }
                }
                row {
                    col(span = 24) { table(Meta.Tables.ResultTable) }
                }
            }
            tabPane("明細") {
                row {
                    col(span = 24) { table(Meta.Tables.DetailTable) }
                }
            }
        }
    }

    val mainForm = layout(
        id = "OrderForm",
        pageTitle = "受注画面"
    ) {
        include(tabArea)
    }

    val templateProvider = ElementPlusTemplate

    // ---- Generate Vue Components ----

//    val docInfo = generateVueComponents(
//        layout = mainForm,
//        outputDir = componentsDir,
//        templateProvider = templateProvider
//    )

    // ---- Generate Docs ----

//    writeHtmlDoc(docInfo, docsDir)
    writeDocHtmlWithGrouping(listOf(mainForm), docsDir)

    // ---- Generate TypeScript Types ----

    val searchFields = listOf(
        Meta.Fields.OrderNo,
        Meta.Fields.Search
    )
    generateTypeScriptTypeFromFields("SearchForm", searchFields, typesDir)

    val resultTableFields = Meta.Tables.ResultTable.columns.map { col ->
        FieldMeta(id = col.id, label = col.label)
    }
    generateTypeScriptTypeFromFields("ResultTableRow", resultTableFields, typesDir)

    val detailTableFields = Meta.Tables.DetailTable.columns.map { col ->
        FieldMeta(id = col.id, label = col.label)
    }
    generateTypeScriptTypeFromFields("DetailTableRow", detailTableFields, typesDir)

    // ---- Generate TypeScript Event Handler Stubs ----

    generateTypeScriptEventStubs(
        fileName = "eventHandlers.ts",
        layouts = listOf(mainForm),
        outputDir = typesDir
    )

    println("All code, types, event stubs, and docs generated for page OrderForm at $pageRoot.")
}
