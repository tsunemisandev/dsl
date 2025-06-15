package pages.Page2
import metadata.FieldMetadata
import model.*

object Meta {
    const val pageTitle = "顧客一覧"
    object Fields {
        val CustomerId = FieldMeta(
            id = FieldMetadata.顧客ID.Id,
            label = FieldMetadata.顧客ID.displayLabel,
            length = FieldMetadata.顧客ID.length
        )
        val CustomerName = FieldMeta(
            id = FieldMetadata.氏名.Id,
            label = FieldMetadata.氏名.displayLabel,
            length = FieldMetadata.氏名.length
        )
        val Search = FieldMeta(
            id = FieldMetadata.検索.Id,
            label = FieldMetadata.検索.displayLabel,
            events = listOf(
                EventMeta(
                    trigger = "click",
                    handler = "onCustomerSearchClick",
                    description = "顧客検索ボタンをクリックしたときの処理"
                )
            )
        )
    }
    object Tables {
        val CustomerTable = TableMeta(
            id = "CustomerTable",
            label = "顧客一覧",
            columns = listOf(
                TableColumnMeta.Text(
                    id = FieldMetadata.顧客ID.Id,
                    label = FieldMetadata.顧客ID.displayLabel,
                    editable = false,
                    length = FieldMetadata.顧客ID.length
                ),
                TableColumnMeta.Text(
                    id = FieldMetadata.氏名.Id,
                    label = FieldMetadata.氏名.displayLabel,
                    editable = false,
                    length = FieldMetadata.氏名.length
                ),
                TableColumnMeta.Text(
                    id = FieldMetadata.電話番号.Id,
                    label = FieldMetadata.電話番号.displayLabel,
                    editable = false,
                    length = FieldMetadata.電話番号.length
                ),
                TableColumnMeta.Text(
                    id = FieldMetadata.メール.Id,
                    label = FieldMetadata.メール.displayLabel,
                    editable = false,
                    length = FieldMetadata.メール.length
                )
            )
        )
    }
}
