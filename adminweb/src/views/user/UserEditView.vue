<template>
  <div class="page-container">
    <el-card shadow="hover" style="max-width: 600px">
      <template #header><span>编辑学生信息</span></template>
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名"><el-input v-model="form.username" disabled /></el-form-item>
        <el-form-item label="昵称"><el-input v-model="form.nickname" /></el-form-item>
        <el-form-item label="年级">
          <el-select v-model="form.grade"><el-option label="一年级" value="一年级" /><el-option label="二年级" value="二年级" />
            <el-option label="三年级" value="三年级" /><el-option label="四年级" value="四年级" />
            <el-option label="五年级" value="五年级" /><el-option label="六年级" value="六年级" /></el-select>
        </el-form-item>
        <el-form-item label="等级"><el-input-number v-model="form.level" :min="1" :max="10" /></el-form-item>
        <el-form-item label="总积分"><el-input-number v-model="form.totalScore" :min="0" /></el-form-item>
        <el-form-item><el-button type="primary" @click="handleSave">保存</el-button><el-button @click="$router.back()">返回</el-button></el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getUserDetail, updateUser } from '@/api'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const form = ref<any>({})

onMounted(async () => {
  const id = Number(route.params.id)
  if (id) {
    try { form.value = await getUserDetail(id) } catch {}
  }
})

async function handleSave() {
  await updateUser(Number(route.params.id), form.value)
  ElMessage.success('保存成功')
  router.back()
}
</script>
