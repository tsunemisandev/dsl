<template>
  <el-button @click="onSearchClick">{{ meta.label }}</el-button>
</template>
<script setup lang="ts">
import { MetaDataField } from '~/metadata/MetaDataField'
const meta = MetaDataField.Search
// 検索ボタンをクリックしたときの処理
function onSearchClick() {
  // TODO: Implement handler for click
}
</script>