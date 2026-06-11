<script lang="ts" setup>
import { APP_CONFIG } from '@/config'
import { reminderApi } from '@/api'

defineOptions({ name: 'Reminders' })
definePage({ style: { navigationStyle: 'custom', navigationBarTitleText: '学习提醒' } })

const reminders = ref([
  { id: 1, label: '每日单词学习', time: '08:00', enabled: true },
  { id: 2, label: '听力训练', time: '16:00', enabled: true },
  { id: 3, label: '阅读打卡', time: '19:00', enabled: false },
  { id: 4, label: '语法练习', time: '20:00', enabled: false },
])
function toggleReminder(id: number) {
  const item = reminders.value.find(r => r.id === id)
  if (item) item.enabled = !item.enabled
}
function goBack() { uni.navigateBack() }

async function loadReminders() {
  if (APP_CONFIG.DATA_MODE !== 1) return
  try {
    const res = await reminderApi.getList()
    if (res && Array.isArray(res)) {
      reminders.value = res.map((r: any) => ({ id: r.id, label: r.label, time: r.reminderTime, enabled: r.enabled }))
    }
  } catch (e) { console.error('Reminders load error:', e) }
}
onShow(() => { loadReminders() })
</script>

<template>
  <view class="min-h-100vh bg-appbg">
    <view class="flex items-center justify-between px-4 pt-12 pb-4">
      <view class="p-2 active:opacity-70" @click="goBack"><wd-icon name="arrow-left" size="22px" color="#333" /></view>
      <text class="text-16px font-bold text-textMain">学习提醒</text>
      <view class="w-10" />
    </view>
    <view class="mx-4 mb-4 flex items-center gap-2"><wd-icon name="clock-circle" size="16px" color="#666" /><text class="text-13px text-textSub">设置每日学习提醒，帮助你养成好习惯</text></view>
    <view class="mx-4 bg-white rounded-16px border overflow-hidden" style="border-color: #E5E7EB; box-shadow: 0 4px 16px rgba(0,0,0,0.05)">
      <view v-for="(item, index) in reminders" :key="item.id" :class="`flex items-center justify-between px-4 py-4 ${index < reminders.length - 1 ? 'border-b' : ''}`" style="border-color: #F9FAFB">
        <view>
          <text class="text-14px text-textMain font-medium block">{{ item.label }}</text>
          <text class="text-12px text-textSub mt-1 block">每天 {{ item.time }}</text>
        </view>
        <view class="w-40px h-24px rounded-full flex items-center px-0.5" :style="{ background: item.enabled ? '#4A90E2' : '#E5E7EB' }" @click="toggleReminder(item.id)">
          <view class="w-20px h-20px rounded-full bg-white" :style="{ marginLeft: item.enabled ? 'auto' : '0', boxShadow: '0 1px 3px rgba(0,0,0,0.2)' }" />
        </view>
      </view>
    </view>
  </view>
</template>
