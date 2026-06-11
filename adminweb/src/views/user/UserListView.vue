<template>
  <div class="page-container">
    <div class="search-bar">
      <el-input v-model="keyword" placeholder="搜索用户名/昵称" style="width: 200px" clearable @clear="loadData" />
      <el-select v-model="grade" placeholder="年级" clearable style="width: 120px" @change="loadData">
        <el-option label="一年级" value="一年级" /><el-option label="二年级" value="二年级" />
        <el-option label="三年级" value="三年级" /><el-option label="四年级" value="四年级" />
        <el-option label="五年级" value="五年级" /><el-option label="六年级" value="六年级" />
      </el-select>
      <el-button type="primary" @click="loadData">搜索</el-button>
    </div>
    <el-card shadow="hover">
      <el-table :data="list" v-loading="loading">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="grade" label="年级" width="80" />
        <el-table-column prop="level" label="等级" width="60" />
        <el-table-column prop="totalScore" label="总积分" width="80" />
        <el-table-column prop="studyDays" label="学习天数" width="90" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="$router.push(`/user/edit/${row.id}`)">编辑</el-button>
            <el-popconfirm title="确定删除？" @confirm="handleDelete(row.id)">
              <template #reference><el-button link type="danger" size="small">删除</el-button></template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-wrapper">
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" layout="total, prev, pager, next" @change="loadData" />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getUserList, deleteUser } from '@/api'
import { ElMessage } from 'element-plus'

const list = ref<any[]>([])
const loading = ref(false)
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const keyword = ref('')
const grade = ref('')

async function loadData() {
  loading.value = true
  try {
    const data: any = await getUserList({ pageNum: pageNum.value, pageSize: pageSize.value, keyword: keyword.value, grade: grade.value })
    list.value = data.list || []
    total.value = data.total || 0
  } catch {} finally { loading.value = false }
}

async function handleDelete(id: number) {
  await deleteUser(id)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>
