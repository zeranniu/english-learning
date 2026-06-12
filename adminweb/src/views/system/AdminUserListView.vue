<template>
  <div class="page-container">
    <el-card shadow="hover">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <span>用户管理</span>
          <el-button type="primary" size="small" @click="showDialog(null)">新增用户</el-button>
        </div>
      </template>

      <el-table :data="list" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="170" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="showDialog(row)">编辑</el-button>
            <el-popconfirm title="确定删除？" @confirm="handleDelete(row.id)">
              <template #reference>
                <el-button link type="danger" size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :total="total"
          layout="total, prev, pager, next"
          @change="loadData"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="editItem ? '编辑用户' : '新增用户'" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" :disabled="!!editItem" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item v-if="!editItem" label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" active-text="启用" inactive-text="禁用" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import request from '@/utils/request'

const list = ref<any[]>([])
const loading = ref(false)
const saving = ref(false)
const dialogVisible = ref(false)
const formRef = ref<FormInstance>()
const editItem = ref<any>(null)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const form = reactive({
  id: 0,
  username: '',
  nickname: '',
  password: '',
  email: '',
  phone: '',
  status: 1,
})

const rules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

onMounted(loadData)

async function loadData() {
  loading.value = true
  try {
    const data: any = await request.get('/admin/users/list', {
      params: { pageNum: pageNum.value, pageSize: pageSize.value },
    })
    list.value = data.list || []
    total.value = data.total || 0
  } catch {} finally {
    loading.value = false
  }
}

function showDialog(item: any) {
  editItem.value = item
  if (item) {
    Object.assign(form, {
      id: item.id,
      username: item.username,
      nickname: item.nickname,
      password: '',
      email: item.email || '',
      phone: item.phone || '',
      status: item.status,
    })
  } else {
    Object.assign(form, { id: 0, username: '', nickname: '', password: '', email: '', phone: '', status: 1 })
  }
  dialogVisible.value = true
}

async function handleSave() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    if (editItem.value) {
      await request.put(`/admin/users/${form.id}`, {
        nickname: form.nickname,
        email: form.email,
        phone: form.phone,
        status: form.status,
      })
    } else {
      await request.post('/admin/users', {
        username: form.username,
        password: form.password,
        nickname: form.nickname,
        email: form.email,
        phone: form.phone,
      })
    }
    ElMessage.success(editItem.value ? '更新成功' : '创建成功')
    dialogVisible.value = false
    loadData()
  } catch {} finally {
    saving.value = false
  }
}

async function handleDelete(id: number) {
  try {
    await request.delete(`/admin/users/${id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch {}
}
</script>
