
<template>
  <el-table :data="tableData" style="width:100%">
    <el-table-column :label="MetaDataField.name.label" prop="name">
  <template #default="scope">
    <el-input v-model="scope.row.name" :maxlength="MetaDataField.name.length" />
  </template>
</el-table-column>
<el-table-column :label="MetaDataField.gender.label" prop="gender">
  <template #default="scope">
    <el-select v-model="scope.row.gender">
      <el-option
        v-for="option in MetaDataField.gender.options"
        :key="option"
        :label="option"
        :value="option"
      />
    </el-select>
  </template>
</el-table-column>
<el-table-column :label="MetaDataField.birthday.label" prop="birthday">
  <template #default="scope">
    {{ scope.row.birthday }}
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
    { name: '', gender: '', birthday: '' },
{ name: '', gender: '', birthday: '' },
{ name: '', gender: '', birthday: '' }
])
function editRow(idx: number) {
  // Your editing logic here
  alert('Edit row: ' + idx)
}
</script>