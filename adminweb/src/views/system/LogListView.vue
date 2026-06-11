<template>
  <div class="page-container">
    <el-card shadow="hover">
      <template #header><span>操作日志</span></template>
      <el-table :data="logs" v-loading="loading">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="username" label="管理员" width="100" />
        <el-table-column prop="module" label="模块" width="100" />
        <el-table-column prop="action" label="操作" width="120" />
        <el-table-column prop="method" label="请求方法" show-overflow-tooltip />
        <el-table-column prop="ip" label="IP" width="120" />
        <el-table-column prop="createdAt" label="时间" width="170" />
      </el-table>
      <div class="pagination-wrapper">
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" layout="total, prev, pager, next" @change="loadData" />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getLogList } from '@/api'

const logs = ref<any[]>([])
const loading = ref(false)
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(20)

async function loadData() {
  loading.value = true
  try { const data: any = await getLogList({ pageNum: pageNum.value, pageSize: pageSize.value }); logs.value = data.list || []; total.value = data.total || 0 } catch {} finally { loading.value = false }
}

onMounted(loadData)
</script>
