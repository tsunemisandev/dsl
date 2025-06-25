<template>
  <div>
    <h1>タブ付き画面</h1>
<el-tabs><el-tab-pane label="検索"><el-row><el-col :span="8"><OrderNo /></el-col><el-col :span="4"><Search /></el-col></el-row><el-row><el-col :span="24"><ResultTable /></el-col></el-row></el-tab-pane><el-tab-pane label="明細"><el-row><el-col :span="24"><DetailTable /></el-col></el-row></el-tab-pane></el-tabs>
  </div>
</template>
<script setup>
import OrderNo from './OrderNo.vue'
import Search from './Search.vue'
import ResultTable from './ResultTable.vue'
import DetailTable from './DetailTable.vue'
</script>
