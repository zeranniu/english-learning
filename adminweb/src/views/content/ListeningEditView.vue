<template>
  <div class="page-container">
    <el-card shadow="hover" style="max-width: 700px">
      <template #header><span>{{ id ? '编辑听力课程' : '新增听力课程' }}</span></template>
      <el-form :model="form" label-width="80px">
        <el-form-item label="课程标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="时长"><el-input v-model="form.duration" placeholder="如 3:45" /></el-form-item>
        <el-form-item label="音频URL"><el-input v-model="form.audioUrl" /><el-upload :show-file-list="false" :http-request="handleAudioUpload" style="margin-top:8px"><el-button size="small">上传音频</el-button></el-upload></el-form-item>
        <el-form-item label="年级"><el-select v-model="form.grade"><el-option label="三年级" value="三年级" /><el-option label="四年级" value="四年级" /><el-option label="五年级" value="五年级" /><el-option label="六年级" value="六年级" /></el-select></el-form-item>
        <el-divider>题目</el-divider>
        <el-form-item label="题干"><el-input v-model="question.questionText" type="textarea" /></el-form-item>
        <el-form-item label="选项A"><el-input v-model="question.optionA" /></el-form-item>
        <el-form-item label="选项B"><el-input v-model="question.optionB" /></el-form-item>
        <el-form-item label="选项C"><el-input v-model="question.optionC" /></el-form-item>
        <el-form-item label="正确答案"><el-select v-model="question.correctOption"><el-option label="A" value="A" /><el-option label="B" value="B" /><el-option label="C" value="C" /></el-select></el-form-item>
        <el-form-item label="解析"><el-input v-model="question.explanation" type="textarea" /></el-form-item>
        <el-form-item><el-button type="primary" @click="handleSave">保存</el-button><el-button @click="$router.back()">返回</el-button></el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { saveListening, uploadAudio } from '@/api'
import { ElMessage } from 'element-plus'
const route = useRoute(); const router = useRouter(); const id = ref(Number(route.params.id) || 0)
const form = ref({ title: '', duration: '', audioUrl: '', grade: '三年级' })
const question = ref({ questionText: '', optionA: '', optionB: '', optionC: '', correctOption: 'A', explanation: '' })
async function handleAudioUpload({ file }: any) { try { const res: any = await uploadAudio(file); form.value.audioUrl = res.url; ElMessage.success('上传成功') } catch {} }
async function handleSave() { await saveListening({ ...form.value, question: question.value, id: id.value || undefined }); ElMessage.success('保存成功'); router.back() }
</script>
