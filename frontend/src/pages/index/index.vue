<script lang="ts" setup>
import { APP_CONFIG } from '@/config'
import { homeApi } from '@/api'
import { useTokenStore } from '@/store/token'

defineOptions({ name: 'Home' })
definePage({
  type: 'home',
  style: {
    navigationStyle: 'custom',
    navigationBarTitleText: '首页',
  },
})

const stats = ref({
  nickname: '小明同学', grade: '三年级', streakDays: 7,
  studyMinutes: 25, tasksCompleted: 8, weekScore: 156, weekRank: 12,
})

const tasks = ref([
  { id: 1, title: '学习10个单词', done: true, progress: '10/10', target: '/pages/vocab/vocab' },
  { id: 2, title: '完成1篇听力', done: true, progress: '1/1', target: '/pages/listening/listening' },
  { id: 3, title: '完成1篇阅读', done: false, progress: '0/1', target: '/pages/reading/reading' },
])

const quickEntries = [
  { label: '单词学习', icon: 'book', color: '#4A90E2', bg: '#EBF3FD', target: '/pages/vocab/vocab' },
  { label: '听力训练', icon: 'headset', color: '#FF9F43', bg: '#FFF3E6', target: '/pages/listening/listening' },
  { label: '阅读理解', icon: 'file', color: '#2ECC71', bg: '#E8F8EF', target: '/pages/reading/reading' },
  { label: '语法练习', icon: 'edit', color: '#8B5CF6', bg: '#F3EEFF', target: '/pages/grammar/grammar' },
]

async function loadData() {
  // 未登录时不请求接口
  const tokenStore = useTokenStore()
  if (!tokenStore.updateNowTime().hasLogin) return
  try {
    const res = await homeApi.getStats()
    if (res) Object.assign(stats.value, res)
    const taskRes = await homeApi.getTasks()
    if (taskRes) tasks.value = taskRes
  } catch (e) { console.error('Home loadData error:', e) }
}

onShow(() => { loadData() })

function navigateTo(url: string) { uni.navigateTo({ url }) }
function switchTo(url: string) { uni.switchTab({ url }) }
</script>

<template>
  <view class="min-h-80vh bg-appbg">
    <!-- Header -->
    <view class="flex items-center justify-between px-6 pt-12 pb-4" style="padding-right: 100px">
      <view class="flex items-center gap-3">
        <view class="w-48px h-48px rounded-full flex items-center justify-center" style="background: #EBF3FD; border: 2px solid #fff; box-shadow: 0 1px 4px rgba(0,0,0,0.08)">
          <wd-icon name="user" size="24px" color="#4A90E2" />
        </view>
        <view>
          <text class="text-16px font-bold text-textMain">{{ stats.nickname }}</text>
          <text class="block text-12px text-textSub mt-1">{{ stats.grade }}</text>
        </view>
      </view>
      <view class="bg-blue-50 px-3 py-1.5 rounded-full text-12px font-medium flex items-center gap-1 border border-blue-100">
        <text style="color: #4A90E2">连续学习</text>
        <text class="font-bold text-14px" style="color: #FF9F43">{{ stats.streakDays }}</text>
        <text style="color: #4A90E2">天</text>
      </view>
    </view>

    <!-- Today's Stats Card -->
    <view class="mx-4 mb-6 rounded-16px p-5 text-white relative overflow-hidden" style="background: linear-gradient(135deg, #6AB0FF 0%, #4A90E2 100%)">
      <view class="absolute w-24 h-24 bg-white rounded-full" style="opacity: 0.1; right: -24px; top: -24px" />
      <view class="absolute w-16 h-16 bg-white rounded-full" style="opacity: 0.1; right: 48px; bottom: -32px" />
      <text class="text-13px font-medium block mb-3" style="opacity: 0.9">今日学习</text>
      <view class="flex justify-between items-center">
        <view class="flex-1 text-center">
          <text class="text-22px font-bold block">{{ stats.studyMinutes }}</text>
          <text class="text-11px block mt-1" style="opacity: 0.8">今日时长(分钟)</text>
        </view>
        <view class="w-1px h-8 bg-white" style="opacity: 0.2" />
        <view class="flex-1 text-center">
          <text class="text-22px font-bold block">{{ stats.tasksCompleted }}</text>
          <text class="text-11px block mt-1" style="opacity: 0.8">完成任务(个)</text>
        </view>
        <view class="w-1px h-8 bg-white" style="opacity: 0.2" />
        <view class="flex-1 text-center">
          <text class="text-22px font-bold block">{{ stats.weekScore }}</text>
          <text class="text-11px block mt-1" style="opacity: 0.8">本周得分(分)</text>
        </view>
        <view class="w-1px h-8 bg-white" style="opacity: 0.2" />
        <view class="flex-1 text-center">
          <text class="text-22px font-bold block">{{ stats.weekRank }}</text>
          <text class="text-11px block mt-1" style="opacity: 0.8">本周排名(名)</text>
        </view>
      </view>
    </view>

    <!-- Quick Access -->
    <view class="mx-4 mb-6">
      <text class="text-14px font-bold text-textMain block mb-3">快捷入口</text>
      <view class="grid grid-cols-4 gap-4">
        <view
          v-for="item in quickEntries"
          :key="item.label"
          class="flex flex-col items-center gap-2 active:opacity-70"
          @click="navigateTo(item.target)"
        >
          <view class="w-48px h-48px rounded-16px flex justify-center items-center" :style="{ background: item.bg }">
            <wd-icon :name="item.icon" size="24px" :color="item.color" />
          </view>
          <text class="text-12px text-textMain font-medium">{{ item.label }}</text>
        </view>
      </view>
    </view>

    <!-- Today's Tasks -->
    <view class="mx-4 bg-white rounded-16px p-4 mb-4" style="box-shadow: 0 4px 16px rgba(0,0,0,0.05)">
      <view class="flex justify-between items-center mb-4">
        <text class="text-14px font-bold text-textMain">今日任务</text>
      </view>
      <view class="space-y-4">
        <view
          v-for="task in tasks"
          :key="task.id"
          class="flex items-center justify-between"
          @click="navigateTo(task.target)"
        >
          <view class="flex items-center gap-3">
            <view class="w-20px h-20px rounded-full flex items-center justify-center" :style="task.done ? { background: '#2ECC71', border: '2px solid #2ECC71' } : { border: '2px solid #D1D5DB' }">
              <wd-icon v-if="task.done" name="check" size="12px" color="#fff" />
            </view>
            <text class="text-14px text-textMain font-medium">{{ task.title }}</text>
          </view>
          <text class="text-12px" :style="{ color: '#999' }">
            进度 <text :style="{ color: task.done ? '#4A90E2' : '#999' }">{{ task.progress }}</text>
          </text>
        </view>
      </view>
    </view>

    <!-- Growth Banner -->
    <view
      class="mx-4 mt-4 rounded-16px p-4 flex justify-between items-center border active:opacity-80"
      style="background: #FFF7ED; border-color: #FED7AA"
      @click="switchTo('/pages/growth/growth')"
    >
      <view class="flex items-center gap-3">
        <view class="w-40px h-40px rounded-full flex items-center justify-center" style="background: #FDBA74">
          <wd-icon name="trophy" size="20px" color="#fff" />
        </view>
        <view>
          <text class="text-14px font-bold text-textMain block">距离升级还需 350 经验</text>
          <text class="text-12px text-textSub block mt-1">继续加油，即将解锁新成就！</text>
        </view>
      </view>
      <wd-icon name="arrow-right" size="18px" color="#999" />
    </view>
  </view>
</template>
