package pages.OrderForm
import model.*
import metadata.FieldMetadata

object Meta {
    const val pageTitle = "受注画面"

    object Fields {
        val OrderNo = FieldMeta(
            id = FieldMetadata.受注No.Id,
            label = FieldMetadata.受注No.displayLabel,
            length = FieldMetadata.受注No.length
        )
        val Search = FieldMeta(
            id = FieldMetadata.検索.Id,
            label = FieldMetadata.検索.displayLabel,
            events = listOf(
                EventMeta(
                    trigger = "click",
                    handler = "onSearchClick",
                    description = "検索ボタンをクリックしたときの処理"
                )
            )
        )
    }

    object Tables {
        val ResultTable = TableMeta(
            id = "ResultTable",
            label = "検索結果",
            columns = listOf(
                TableColumnMeta.Text(
                    id = FieldMetadata.氏名.Id,
                    label = FieldMetadata.氏名.displayLabel,
                    editable = true,
                    length = FieldMetadata.氏名.length
                ),
                TableColumnMeta.Select(
                    id = FieldMetadata.性別.Id,
                    label = FieldMetadata.性別.displayLabel,
                    editable = true,
                    options = FieldMetadata.性別.options
                ),
                TableColumnMeta.Date(
                    id = FieldMetadata.生年月日.Id,
                    label = FieldMetadata.生年月日.displayLabel,
                    editable = false
                )
            )
        )

        val DetailTable = TableMeta(
            id = "DetailTable",
            label = "明細",
            columns = listOf(
                TableColumnMeta.Text(
                    id = FieldMetadata.商品番号.Id,
                    label = FieldMetadata.商品番号.displayLabel,
                    editable = false,
                    length = FieldMetadata.商品番号.length
                ),
                TableColumnMeta.Text(
                    id = FieldMetadata.商品名.Id,
                    label = FieldMetadata.商品名.displayLabel,
                    editable = true,
                    length = FieldMetadata.商品名.length
                ),
                TableColumnMeta.Number(
                    id = FieldMetadata.数量.Id,
                    label = FieldMetadata.数量.displayLabel,
                    editable = true,
                    length = FieldMetadata.数量.length
                ),
                TableColumnMeta.Number(
                    id = FieldMetadata.単価.Id,
                    label = FieldMetadata.単価.displayLabel,
                    editable = true,
                    length = FieldMetadata.単価.length
                )
            )
        )
    }
}
