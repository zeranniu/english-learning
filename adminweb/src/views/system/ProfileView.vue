<template>
  <div class="page-container">
    <el-row :gutter="20">
      <!-- 头像与基本信息 -->
      <el-col :span="8">
        <el-card shadow="hover">
          <div style="display: flex; flex-direction: column; align-items: center; padding: 20px 0">
            <el-avatar :size="100" :src="form.avatar || ''" style="background: #409eff; font-size: 36px">
              {{ (form.nickname || form.username || 'A').charAt(0).toUpperCase() }}
            </el-avatar>
            <h3 style="margin: 16px 0 4px">{{ form.nickname || form.username }}</h3>
            <p style="color: #909399; font-size: 13px">{{ form.username }}</p>
            <el-upload
              :show-file-list="false"
              :http-request="handleAvatarUpload"
              accept="image/png,image/jpeg,image/jpg"
              style="margin-top: 16px"
            >
              <el-button size="small" type="primary">
                <el-icon style="margin-right: 4px"><Upload /></el-icon>更换头像
              </el-button>
            </el-upload>
          </div>
        </el-card>
      </el-col>

      <!-- 个人信息编辑 -->
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header><span>个人信息</span></template>
          <el-form :model="form" label-width="80px" style="max-width: 500px">
            <el-form-item label="用户名">
              <el-input :model-value="form.username" disabled />
            </el-form-item>
            <el-form-item label="昵称">
              <el-input v-model="form.nickname" placeholder="请输入昵称" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="form.phone" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="saving" @click="handleSaveInfo">保存信息</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card shadow="hover" style="margin-top: 20px">
          <template #header><span>修改密码</span></template>
          <el-form :model="passwordForm" :rules="passwordRules" ref="passwordRef" label-width="100px" style="max-width: 500px">
            <el-form-item label="当前密码" prop="oldPassword">
              <el-input v-model="passwordForm.oldPassword" type="password" show-password placeholder="请输入当前密码" />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="passwordForm.newPassword" type="password" show-password placeholder="请输入新密码（至少6位）" />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="passwordForm.confirmPassword" type="password" show-password placeholder="请再次输入新密码" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="changingPwd" @click="handleChangePassword">修改密码</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, type FormInstance, type FormRules, type UploadRequestOptions } from 'element-plus'
import { Upload } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'
import request from '@/utils/request'

const authStore = useAuthStore()
const saving = ref(false)
const changingPwd = ref(false)
const passwordRef = ref<FormInstance>()

const form = reactive({
  id: 0,
  username: '',
  nickname: '',
  email: '',
  phone: '',
  avatar: '',
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
})

const passwordRules: FormRules = {
  oldPassword: [{ required: true, message: '请输入当前密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (_rule: unknown, value: string, callback: (error?: Error) => void) => {
        if (value !== passwordForm.newPassword) callback(new Error('两次密码不一致'))
        else callback()
      },
      trigger: 'blur',
    },
  ],
}

onMounted(async () => {
  try {
    const data: any = await request.get('/admin/auth/info')
    Object.assign(form, {
      id: data.id,
      username: data.username,
      nickname: data.nickname,
      email: data.email || '',
      phone: data.phone || '',
      avatar: data.avatar || '',
    })
  } catch {}
})

async function handleSaveInfo() {
  saving.value = true
  try {
    await request.put('/admin/auth/profile', {
      nickname: form.nickname,
      email: form.email,
      phone: form.phone,
    })
    // 更新 store 中的昵称
    if (authStore.adminInfo) {
      authStore.adminInfo.nickname = form.nickname
      localStorage.setItem('admin_info', JSON.stringify(authStore.adminInfo))
    }
    ElMessage.success('保存成功')
  } catch {} finally {
    saving.value = false
  }
}

async function handleChangePassword() {
  const valid = await passwordRef.value?.validate().catch(() => false)
  if (!valid) return
  changingPwd.value = true
  try {
    await request.put('/admin/auth/password', {
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword,
    })
    ElMessage.success('密码修改成功，请重新登录')
    passwordForm.oldPassword = ''
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''
    // 3秒后自动退出
    setTimeout(() => {
      authStore.logout()
      location.href = '/login'
    }, 3000)
  } catch {} finally {
    changingPwd.value = false
  }
}

async function handleAvatarUpload(options: UploadRequestOptions) {
  const fd = new FormData()
  fd.append('file', options.file)
  try {
    const res: any = await request.post('/admin/upload/image', fd, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
    form.avatar = res.url
    // 同时更新后端
    await request.put('/admin/auth/profile', { avatar: res.url })
    if (authStore.adminInfo) {
      authStore.adminInfo.avatar = res.url
      localStorage.setItem('admin_info', JSON.stringify(authStore.adminInfo))
    }
    ElMessage.success('头像上传成功')
  } catch {
    ElMessage.error('头像上传失败')
  }
}
</script>
