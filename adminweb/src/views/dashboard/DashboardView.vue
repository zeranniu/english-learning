<template>
  <div class="page-container">
    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="6"><el-card shadow="hover"><el-statistic title="总学生数" :value="overview.totalUsers || 0" /></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><el-statistic title="今日活跃" :value="overview.todayLogs || 0" /></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><el-statistic title="总错题数" :value="overview.totalMistakes || 0" /></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><el-statistic title="总题目数" :value="19" /></el-card></el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header><span>近7天学习趋势</span></template>
          <div ref="chartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header><span>高频错题 TOP10</span></template>
          <el-table :data="topMistakes" size="small">
            <el-table-column prop="title" label="题目" show-overflow-tooltip />
            <el-table-column prop="count" label="错误次数" width="80" align="center" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { getOverview } from '@/api'

const overview = ref<any>({})
const topMistakes = ref<any[]>([])
const chartRef = ref<HTMLElement>()

onMounted(async () => {
  try {
    const data: any = await getOverview()
    overview.value = data.overview || {}
    topMistakes.value = data.topMistakes || []
    if (chartRef.value) {
      const chart = echarts.init(chartRef.value)
      const logs = data.recentLogs || []
      chart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: logs.map((l: any) => l.date) },
        yAxis: { type: 'value', name: '学习时长(分钟)' },
        series: [{ data: logs.map((l: any) => l.studyMinutes), type: 'line', smooth: true, areaStyle: {} }],
      })
    }
  } catch {}
})
</script>
