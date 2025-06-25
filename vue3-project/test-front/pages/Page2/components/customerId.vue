<template>
  <el-form-item :label="meta.label">
    <el-input v-model="modelValue" :maxlength="meta.length" :placeholder="meta.label" />
  </el-form-item>
</template>
<script setup lang="ts">
import { MetaDataField } from '~/metadata/MetaDataField'
import { ref, defineProps, watch } from 'vue'

const meta = MetaDataField.customerId
const props = defineProps<{ modelValue: string }>()
const emit = defineEmits<{ (e: 'update:modelValue', value: string): void }>()
const modelValue = ref(props.modelValue)
watch(() => props.modelValue, (val) => { if (val !== modelValue.value) modelValue.value = val })
watch(modelValue, (val) => emit('update:modelValue', val))
</script>