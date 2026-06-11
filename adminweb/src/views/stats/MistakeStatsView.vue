<template>
  <div class="page-container">
    <el-card shadow="hover">
      <template #header><span>高频错题统计</span></template>
      <el-table :data="stats">
        <el-table-column prop="title" label="题目" /><el-table-column prop="count" label="错误次数" width="100" align="center" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getMistakeStats } from '@/api'
const stats = ref<any[]>([])
onMounted(async () => { try { stats.value = await getMistakeStats() } catch {} })
</script>
