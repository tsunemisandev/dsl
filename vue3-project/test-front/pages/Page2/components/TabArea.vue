<template>
  <div>
    <h1>顧客検索・一覧</h1>
<el-tabs><el-tab-pane label="検索"><el-row><el-col :span="8"><customerId /></el-col><el-col :span="8"><name /></el-col><el-col :span="8"><Search /></el-col></el-row></el-tab-pane><el-tab-pane label="一覧"><el-row><el-col :span="24"><CustomerTable /></el-col></el-row></el-tab-pane></el-tabs>
  </div>
</template>
<script setup>
import customerId from './customerId.vue'
import name from './name.vue'
import Search from './Search.vue'
import CustomerTable from './CustomerTable.vue'
</script>
