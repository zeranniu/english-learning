<template>
  <div class="page-container">
    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="8"><el-card shadow="hover"><statistic title="总学生数" :value="overview.totalUsers || 0" /></el-card></el-col>
      <el-col :span="8"><el-card shadow="hover"><statistic title="今日活跃" :value="overview.todayLogs || 0" /></el-card></el-col>
      <el-col :span="8"><el-card shadow="hover"><statistic title="总错题数" :value="overview.totalMistakes || 0" /></el-card></el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover"><template #header><span>学习时长趋势</span></template><div ref="chartRef1" style="height: 300px"></div></el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover"><template #header><span>学习排行 TOP10</span></template>
          <el-table :data="topStudents" size="small">
            <el-table-column prop="nickname" label="学生" /><el-table-column prop="totalScore" label="总积分" width="80" align="center" /><el-table-column prop="studyDays" label="学习天数" width="80" align="center" />
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
const overview = ref<any>({}); const topStudents = ref<any[]>([]); const chartRef1 = ref<HTMLElement>()
onMounted(async () => {
  try {
    const data: any = await getOverview()
    overview.value = data.overview || {}
    topStudents.value = data.topStudents || []
    if (chartRef1.value) {
      const chart = echarts.init(chartRef1.value)
      const logs = data.recentLogs || []
      chart.setOption({ tooltip: { trigger: 'axis' }, xAxis: { type: 'category', data: logs.map((l: any) => l.date) }, yAxis: { type: 'value', name: '分钟' }, series: [{ data: logs.map((l: any) => l.studyMinutes), type: 'bar' }] })
    }
  } catch {}
})
</script>
