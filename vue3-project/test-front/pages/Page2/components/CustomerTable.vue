
<template>
  <el-table :data="tableData" style="width:100%">
    <el-table-column :label="MetaDataField.customerId.label" prop="customerId">
  <template #default="scope">
    {{ scope.row.customerId }}
  </template>
</el-table-column>
<el-table-column :label="MetaDataField.name.label" prop="name">
  <template #default="scope">
    {{ scope.row.name }}
  </template>
</el-table-column>
<el-table-column :label="MetaDataField.phone.label" prop="phone">
  <template #default="scope">
    {{ scope.row.phone }}
  </template>
</el-table-column>
<el-table-column :label="MetaDataField.email.label" prop="email">
  <template #default="scope">
    {{ scope.row.email }}
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
    { customerId: '', name: '', phone: '', email: '' },
{ customerId: '', name: '', phone: '', email: '' },
{ customerId: '', name: '', phone: '', email: '' },
{ customerId: '', name: '', phone: '', email: '' }
])
function editRow(idx: number) {
  // Your editing logic here
  alert('Edit row: ' + idx)
}
</script>