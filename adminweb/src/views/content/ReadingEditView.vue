<template>
  <div class="page-container">
    <el-card shadow="hover" style="max-width: 700px">
      <template #header><span>{{ id ? '编辑阅读文章' : '新增阅读文章' }}</span></template>
      <el-form :model="form" label-width="80px">
        <el-form-item label="文章标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="正文"><el-input v-model="form.content" type="textarea" :rows="6" /></el-form-item>
        <el-form-item label="难度"><el-input-number v-model="form.difficulty" :min="1" :max="5" /></el-form-item>
        <el-form-item label="建议时长"><el-input-number v-model="form.suggestedMinutes" :min="1" /></el-form-item>
        <el-form-item label="年级"><el-select v-model="form.grade"><el-option label="三年级" value="三年级" /><el-option label="四年级" value="四年级" /><el-option label="五年级" value="五年级" /><el-option label="六年级" value="六年级" /></el-select></el-form-item>
        <el-form-item><el-button type="primary" @click="handleSave">保存文章</el-button><el-button @click="$router.back()">返回</el-button></el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getReadingList, savePassage } from '@/api'
import { ElMessage } from 'element-plus'
const route = useRoute(); const router = useRouter(); const id = ref(Number(route.params.id) || 0)
const form = ref({ title: '', content: '', difficulty: 2, suggestedMinutes: 5, grade: '三年级' })
onMounted(async () => { if (id.value) { try { const data: any = await getReadingList({ pageNum: 1, pageSize: 100 }); const item = data.list?.find((w: any) => w.id === id.value); if (item) Object.assign(form.value, item) } catch {} } })
async function handleSave() { await savePassage({ ...form.value, id: id.value || undefined }); ElMessage.success('保存成功'); router.back() }
</script>
