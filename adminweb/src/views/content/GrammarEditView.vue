<template>
  <div class="page-container">
    <el-card shadow="hover" style="max-width: 700px">
      <template #header><span>{{ id ? '编辑语法题' : '新增语法题' }}</span></template>
      <el-form :model="form" label-width="80px">
        <el-form-item label="题目"><el-input v-model="form.questionText" type="textarea" /></el-form-item>
        <el-form-item label="选项A"><el-input v-model="form.optionA" /></el-form-item>
        <el-form-item label="选项B"><el-input v-model="form.optionB" /></el-form-item>
        <el-form-item label="选项C"><el-input v-model="form.optionC" /></el-form-item>
        <el-form-item label="选项D"><el-input v-model="form.optionD" /></el-form-item>
        <el-form-item label="正确答案"><el-input-number v-model="form.correctOption" :min="0" :max="3" /></el-form-item>
        <el-form-item label="解析"><el-input v-model="form.explanation" type="textarea" /></el-form-item>
        <el-form-item label="年级"><el-select v-model="form.grade"><el-option label="三年级" value="三年级" /><el-option label="四年级" value="四年级" /><el-option label="五年级" value="五年级" /><el-option label="六年级" value="六年级" /></el-select></el-form-item>
        <el-form-item><el-button type="primary" @click="handleSave">保存</el-button><el-button @click="$router.back()">返回</el-button></el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getGrammarList, saveGrammar } from '@/api'
import { ElMessage } from 'element-plus'
const route = useRoute(); const router = useRouter(); const id = ref(Number(route.params.id) || 0)
const form = ref({ questionText: '', optionA: '', optionB: '', optionC: '', optionD: '', correctOption: 0, explanation: '', grade: '三年级' })
onMounted(async () => { if (id.value) { try { const data: any = await getGrammarList({ pageNum: 1, pageSize: 100 }); const item = data.list?.find((w: any) => w.id === id.value); if (item) Object.assign(form.value, item) } catch {} } })
async function handleSave() { await saveGrammar({ ...form.value, id: id.value || undefined }); ElMessage.success('保存成功'); router.back() }
</script>
