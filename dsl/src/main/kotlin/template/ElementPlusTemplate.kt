package template

import model.*

object ElementPlusTemplate : ComponentTemplateProvider {
    override val name = "Element Plus"

    override fun inputText(meta: FieldMeta) = """
<template>
  <el-form-item :label="meta.label">
    <el-input v-model="modelValue" :maxlength="meta.length" :placeholder="meta.label" />
  </el-form-item>
</template>
<script setup lang="ts">
import { MetaDataField } from '~/metadata/MetaDataField'
import { ref, defineProps, watch } from 'vue'

const meta = MetaDataField.${meta.id}
const props = defineProps<{ modelValue: string }>()
const emit = defineEmits<{ (e: 'update:modelValue', value: string): void }>()
const modelValue = ref(props.modelValue)
watch(() => props.modelValue, (val) => { if (val !== modelValue.value) modelValue.value = val })
watch(modelValue, (val) => emit('update:modelValue', val))
</script>
""".trimIndent()

    override fun button(meta: FieldMeta): String {
        val eventStr = meta.events?.joinToString(" ") { "@${it.trigger}=\"${it.handler}\"" } ?: ""
        val eventHandlers = meta.events?.joinToString("\n") {
            (it.description?.let { desc -> "// $desc\n" } ?: "") +
                    "function ${it.handler}() {\n  // TODO: Implement handler for ${it.trigger}\n}"
        } ?: ""
        return """
<template>
  <el-button $eventStr>{{ meta.label }}</el-button>
</template>
<script setup lang="ts">
import { MetaDataField } from '~/metadata/MetaDataField'
const meta = MetaDataField.${meta.id}
$eventHandlers
</script>
""".trimIndent()
    }

    override fun table(meta: TableMeta): String {
        val dataVar = "tableData"
        val colTemplates = meta.columns.joinToString("\n") { col ->
            val tsMeta = "MetaDataField.${col.id}"
            when (col) {
                is TableColumnMeta.Text -> if (col.editable) {
                    """
    <el-table-column :label="$tsMeta.label" prop="${col.id}">
      <template #default="scope">
        <el-input v-model="scope.row.${col.id}" :maxlength="$tsMeta.length" />
      </template>
    </el-table-column>
    """.trimIndent()
                } else {
                    """
    <el-table-column :label="$tsMeta.label" prop="${col.id}">
      <template #default="scope">
        {{ scope.row.${col.id} }}
      </template>
    </el-table-column>
    """.trimIndent()
                }

                is TableColumnMeta.Number -> if (col.editable) {
                    """
    <el-table-column :label="$tsMeta.label" prop="${col.id}">
      <template #default="scope">
        <el-input type="number" v-model="scope.row.${col.id}" :maxlength="$tsMeta.length" />
      </template>
    </el-table-column>
    """.trimIndent()
                } else {
                    """
    <el-table-column :label="$tsMeta.label" prop="${col.id}">
      <template #default="scope">
        {{ scope.row.${col.id} }}
      </template>
    </el-table-column>
    """.trimIndent()
                }

                is TableColumnMeta.Date -> if (col.editable) {
                    """
    <el-table-column :label="$tsMeta.label" prop="${col.id}">
      <template #default="scope">
        <el-date-picker v-model="scope.row.${col.id}" type="date" />
      </template>
    </el-table-column>
    """.trimIndent()
                } else {
                    """
    <el-table-column :label="$tsMeta.label" prop="${col.id}">
      <template #default="scope">
        {{ scope.row.${col.id} }}
      </template>
    </el-table-column>
    """.trimIndent()
                }

                is TableColumnMeta.Select -> if (col.editable) {
                    """
    <el-table-column :label="$tsMeta.label" prop="${col.id}">
      <template #default="scope">
        <el-select v-model="scope.row.${col.id}">
          <el-option
            v-for="option in $tsMeta.options"
            :key="option"
            :label="option"
            :value="option"
          />
        </el-select>
      </template>
    </el-table-column>
    """.trimIndent()
                } else {
                    """
    <el-table-column :label="$tsMeta.label" prop="${col.id}">
      <template #default="scope">
        {{ scope.row.${col.id} }}
      </template>
    </el-table-column>
    """.trimIndent()
                }
            }
        }

        return """
<template>
  <el-table :data="$dataVar" style="width:100%">
    $colTemplates
    <el-table-column label="Actions">
      <template #default="scope">
        <el-button @click="editRow(scope.$""" + """index)">Edit</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>
<script setup lang="ts">
import { MetaDataField } from '~/metadata/MetaDataField'
import { ref } from 'vue'
const $dataVar = ref([
    ${meta.columns.joinToString(",\n") { "{ ${meta.columns.joinToString(", ") { "${it.id}: ''" }} }" }}
])
function editRow(idx: number) {
  // Your editing logic here
  alert('Edit row: ' + idx)
}
</script>
""".trimIndent()
    }

    override fun row(content: String) = "<el-row>$content</el-row>"
    override fun col(span: Int, content: String) = "<el-col :span=\"$span\">$content</el-col>"
    override fun tab(content: String) = "<el-tabs>$content</el-tabs>"
    override fun tabPane(label: String, content: String) = "<el-tab-pane label=\"$label\">$content</el-tab-pane>"
}
