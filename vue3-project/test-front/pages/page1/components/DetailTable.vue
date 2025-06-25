
<template>
  <el-table :data="tableData" style="width:100%">
    <el-table-column :label="MetaDataField.itemNo.label" prop="itemNo">
  <template #default="scope">
    {{ scope.row.itemNo }}
  </template>
</el-table-column>
<el-table-column :label="MetaDataField.itemName.label" prop="itemName">
  <template #default="scope">
    <el-input v-model="scope.row.itemName" :maxlength="MetaDataField.itemName.length" />
  </template>
</el-table-column>
<el-table-column :label="MetaDataField.qty.label" prop="qty">
  <template #default="scope">
    <el-input type="number" v-model="scope.row.qty" :maxlength="MetaDataField.qty.length" />
  </template>
</el-table-column>
<el-table-column :label="MetaDataField.unitPrice.label" prop="unitPrice">
  <template #default="scope">
    <el-input type="number" v-model="scope.row.unitPrice" :maxlength="MetaDataField.unitPrice.length" />
  </template>
</el-table-column>
    <el-table-column label="Actions">
      <template #default="scope">
        <el-button @click="editRow(scope.$index)">Edit</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>
<script setup lang="ts">
import { MetaDataField } from '~/metadata/MetaDataField'
import { ref } from 'vue'
const tableData = ref([
    { itemNo: '', itemName: '', qty: '', unitPrice: '' },
{ itemNo: '', itemName: '', qty: '', unitPrice: '' },
{ itemNo: '', itemName: '', qty: '', unitPrice: '' },
{ itemNo: '', itemName: '', qty: '', unitPrice: '' }
])
function editRow(idx: number) {
  // Your editing logic here
  alert('Edit row: ' + idx)
}
</script>