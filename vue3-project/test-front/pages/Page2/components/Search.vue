<template>
  <el-button @click="onCustomerSearchClick">{{ meta.label }}</el-button>
</template>
<script setup lang="ts">
import { MetaDataField } from '~/metadata/MetaDataField'
const meta = MetaDataField.Search
// 顧客検索ボタンをクリックしたときの処理
function onCustomerSearchClick() {
  // TODO: Implement handler for click
}
</script>