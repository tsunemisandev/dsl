package metadata

object FieldMetadata {
    // ---- Search / Header Fields ----
    object 受注No {
        const val Id = "OrderNo"
        const val displayLabel = "受注No"
        const val length = 5
    }

    object 検索 {
        const val Id = "Search"
        const val displayLabel = "検索"
        // No length or options for button
    }

    // ---- ResultTable Columns ----
    object 氏名 {
        const val Id = "name"
        const val displayLabel = "氏名"
        const val length = 5
    }

    object 性別 {
        const val Id = "gender"
        const val displayLabel = "性別"
        const val length = 1
        val options = listOf("男", "女")
    }

    object 生年月日 {
        const val Id = "birthday"
        const val displayLabel = "生年月日"
        const val length = 8
    }

    // ---- DetailTable Columns ----
    object 商品番号 {
        const val Id = "itemNo"
        const val displayLabel = "商品番号"
        const val length = 8
    }

    object 商品名 {
        const val Id = "itemName"
        const val displayLabel = "商品名"
        const val length = 40
    }

    object 数量 {
        const val Id = "qty"
        const val displayLabel = "数量"
        const val length = 5
    }

    object 単価 {
        const val Id = "unitPrice"
        const val displayLabel = "単価"
        const val length = 10
    }

    object 顧客ID {
        const val Id = "customerId"
        const val displayLabel = "顧客ID"
        const val length = 10
    }

    object 電話番号 {
        const val Id = "phone"
        const val displayLabel = "電話番号"
        const val length = 15
    }

    object メール {
        const val Id = "email"
        const val displayLabel = "メール"
        const val length = 60
    }

}
