// /metadata/MetaDataField.ts
// This file is auto-generated. Do not edit directly.

export const MetaDataField = {
    // ===== OrderForm =====
    OrderNo: {
        id: 'OrderNo',
        label: '受注No',
        length: 10
    },
    Search: {
        id: 'Search',
        label: '検索'
    },
    name: {
        id: 'name',
        label: '氏名',
        length: 5
    },
    gender: {
        id: 'gender',
        label: '性別',
        options: ['男', '女'],
        length: 1
    },
    birthday: {
        id: 'birthday',
        label: '生年月日',
        length: 8
    },
    itemNo: {
        id: 'itemNo',
        label: '商品番号',
        length: 12
    },
    itemName: {
        id: 'itemName',
        label: '商品名',
        length: 30
    },
    qty: {
        id: 'qty',
        label: '数量',
        length: 4
    },
    unitPrice: {
        id: 'unitPrice',
        label: '単価',
        length: 8
    },

    // ===== CustomerList =====
    customerId: {
        id: 'customerId',
        label: '顧客ID',
        length: 10
    },
    customerName: {
        id: 'customerName',
        label: '氏名',
        length: 40
    },
    phone: {
        id: 'phone',
        label: '電話番号',
        length: 15
    },
    email: {
        id: 'email',
        label: 'メール',
        length: 60
    }
} as const;
