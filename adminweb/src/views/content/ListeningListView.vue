<template>
  <div class="page-container">
    <div class="search-bar"><el-button type="success" @click="$router.push('/content/listening/edit')">新增听力课程</el-button></div>
    <el-card shadow="hover">
      <el-table :data="list" v-loading="loading">
        <el-table-column prop="id" label="ID" width="60" /><el-table-column prop="title" label="课程标题" /><el-table-column prop="duration" label="时长" width="80" /><el-table-column prop="grade" label="年级" width="80" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="$router.push(`/content/listening/edit/${row.id}`)">编辑</el-button>
            <el-popconfirm title="确定删除？"><template #reference><el-button link type="danger" size="small">删除</el-button></template></el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-wrapper"><el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" layout="total, prev, pager, next" @change="loadData" /></div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getListeningList } from '@/api'
const list = ref<any[]>([]); const loading = ref(false); const total = ref(0); const pageNum = ref(1); const pageSize = ref(10)
async function loadData() { loading.value = true; try { const data: any = await getListeningList({ pageNum: pageNum.value, pageSize: pageSize.value }); list.value = data.list || []; total.value = data.total || 0 } catch {} finally { loading.value = false } }
onMounted(loadData)
</script>
