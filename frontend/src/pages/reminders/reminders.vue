<script lang="ts" setup>
import { computed, ref } from 'vue'
import { APP_CONFIG } from '@/config'
import { reminderApi } from '@/api'

defineOptions({ name: 'Reminders' })
definePage({ style: { navigationStyle: 'custom', navigationBarTitleText: '学习提醒' } })

interface ReminderItem {
  id: number
  label: string
  time: string
  enabled: boolean
}

const STORAGE_KEY = 'english_learning_reminders'

const defaultReminders: ReminderItem[] = [
  { id: 1, label: '每日单词学习', time: '08:00', enabled: true },
  { id: 2, label: '听力训练', time: '16:00', enabled: true },
  { id: 3, label: '阅读打卡', time: '19:00', enabled: false },
  { id: 4, label: '语法练习', time: '20:00', enabled: false },
]

const reminders = ref<ReminderItem[]>(defaultReminders.map(item => ({ ...item })))
const enabledCount = computed(() => reminders.value.filter(item => item.enabled).length)

function goBack() {
  uni.navigateBack()
}

function saveLocalReminders() {
  uni.setStorageSync(STORAGE_KEY, reminders.value)
}

function loadLocalReminders() {
  const saved = uni.getStorageSync(STORAGE_KEY)
  if (Array.isArray(saved) && saved.length > 0) {
    reminders.value = saved
  }
}

async function toggleReminder(id: number) {
  const item = reminders.value.find(r => r.id === id)
  if (!item) return
  item.enabled = !item.enabled
  saveLocalReminders()

  if (APP_CONFIG.DATA_MODE !== 1) return
  try {
    await reminderApi.toggle({ id: item.id, enabled: item.enabled })
  } catch (e) {
    console.error('Reminder toggle error:', e)
  }
}

function updateReminderTime(id: number, event: any) {
  const item = reminders.value.find(r => r.id === id)
  if (!item) return
  item.time = event.detail.value
  saveLocalReminders()
  uni.showToast({ title: '提醒时间已更新', icon: 'success' })
}

function resetReminders() {
  reminders.value = defaultReminders.map(item => ({ ...item }))
  saveLocalReminders()
  uni.showToast({ title: '已恢复默认提醒', icon: 'success' })
}

async function loadReminders() {
  loadLocalReminders()
  if (APP_CONFIG.DATA_MODE !== 1) return
  try {
    const res = await reminderApi.getList()
    if (res && Array.isArray(res)) {
      reminders.value = res.map((r: any, index: number) => ({
        id: r.id || index + 1,
        label: r.label || r.title || '学习提醒',
        time: r.reminderTime || r.time || '20:00',
        enabled: Boolean(r.enabled),
      }))
      saveLocalReminders()
    }
  } catch (e) {
    console.error('Reminders load error:', e)
  }
}

onShow(() => {
  loadReminders()
})
</script>

<template>
  <view class="min-h-80vh bg-appbg">
    <view class="flex items-center justify-between px-4 pt-12 pb-4">
      <view class="p-2 active:opacity-70" @click="goBack"><wd-icon name="arrow-left" size="22px" color="#333" /></view>
      <text class="text-16px font-bold text-textMain">学习提醒</text>
      <text class="text-13px" style="color: #4A90E2" @click="resetReminders">重置</text>
    </view>

    <view class="mx-4 mb-4 reminder-summary">
      <view>
        <text class="text-18px font-bold text-white block">已开启 {{ enabledCount }} 项提醒</text>
        <text class="text-12px text-white opacity-85 mt-1 block">设置每日学习提醒，帮助孩子养成好习惯</text>
      </view>
      <wd-icon name="clock-circle" size="30px" color="#fff" />
    </view>

    <view class="mx-4 bg-white rounded-16px border overflow-hidden" style="border-color: #E5E7EB; box-shadow: 0 4px 16px rgba(0,0,0,0.05)">
      <view v-for="(item, index) in reminders" :key="item.id" :class="`reminder-row ${index < reminders.length - 1 ? 'border-b' : ''}`" style="border-color: #F9FAFB">
        <view class="flex-1 min-w-0">
          <text class="text-14px text-textMain font-medium block">{{ item.label }}</text>
          <picker mode="time" :value="item.time" @change="updateReminderTime(item.id, $event)">
            <view class="flex items-center gap-1 mt-1 active:opacity-70">
              <text class="text-12px text-textSub block">每天 {{ item.time }}</text>
              <wd-icon name="edit" size="12px" color="#999" />
            </view>
          </picker>
        </view>
        <view class="w-40px h-24px rounded-full flex items-center px-0.5" :style="{ background: item.enabled ? '#4A90E2' : '#E5E7EB' }" @click="toggleReminder(item.id)">
          <view class="w-20px h-20px rounded-full bg-white" :style="{ marginLeft: item.enabled ? 'auto' : '0', boxShadow: '0 1px 3px rgba(0,0,0,0.2)' }" />
        </view>
      </view>
    </view>

    <view class="mx-4 mt-4 reminder-tip">
      <wd-icon name="info-circle" size="16px" color="#4A90E2" />
      <text class="text-12px text-textSub ml-2">在浏览器或小程序中会保存提醒偏好；实际推送能力取决于运行平台授权。</text>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.reminder-summary {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 18px;
  background: linear-gradient(135deg, #4a90e2 0%, #7bb7ff 100%);
  border-radius: 18px;
  box-shadow: 0 8px 20px rgba(74, 144, 226, 0.22);
}

.reminder-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 16px;
}

.reminder-tip {
  display: flex;
  align-items: flex-start;
  padding: 12px;
  background: #ebf3fd;
  border-radius: 12px;
}
</style>
