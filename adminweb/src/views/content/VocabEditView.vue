<template>
  <div class="page-container">
    <el-card shadow="hover" style="max-width: 600px">
      <template #header><span>{{ id ? '编辑单词' : '新增单词' }}</span></template>
      <el-form :model="form" label-width="80px">
        <el-form-item label="单词"><el-input v-model="form.word" /></el-form-item>
        <el-form-item label="音标"><el-input v-model="form.phonetic" /></el-form-item>
        <el-form-item label="释义"><el-input v-model="form.translation" /></el-form-item>
        <el-form-item label="图标"><el-input v-model="form.icon" /></el-form-item>
        <el-form-item label="颜色"><el-color-picker v-model="form.iconColor" /></el-form-item>
        <el-form-item label="年级"><el-select v-model="form.grade"><el-option label="三年级" value="三年级" /><el-option label="四年级" value="四年级" /><el-option label="五年级" value="五年级" /><el-option label="六年级" value="六年级" /></el-select></el-form-item>
        <el-form-item><el-button type="primary" @click="handleSave">保存</el-button><el-button @click="$router.back()">返回</el-button></el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getVocabList, saveVocab } from '@/api'
import { ElMessage } from 'element-plus'
const route = useRoute(); const router = useRouter(); const id = ref(Number(route.params.id) || 0)
const form = ref({ word: '', phonetic: '', translation: '', icon: 'book', iconColor: '#4A90E2', grade: '三年级' })
onMounted(async () => { if (id.value) { try { const data: any = await getVocabList({ pageNum: 1, pageSize: 100 }); const item = data.list?.find((w: any) => w.id === id.value); if (item) Object.assign(form.value, item) } catch {} } })
async function handleSave() { await saveVocab({ ...form.value, id: id.value || undefined }); ElMessage.success('保存成功'); router.back() }
</script>
