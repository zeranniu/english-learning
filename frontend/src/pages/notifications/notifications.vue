<script lang="ts" setup>
import { APP_CONFIG } from '@/config'
import { notificationApi } from '@/api'

defineOptions({ name: 'Notifications' })
definePage({ style: { navigationStyle: 'custom', navigationBarTitleText: '消息通知' } })

const notifications = ref([
  { id: 1, title: '系统通知', desc: '恭喜完成今日单词学习！', time: '10分钟前', read: false, icon: 'notification' },
  { id: 2, title: '成就解锁', desc: '你已解锁"单词达人"徽章！', time: '2小时前', read: false, icon: 'gift' },
  { id: 3, title: '学习提醒', desc: '今天的阅读任务还没完成哦', time: '昨天', read: true, icon: 'clock-circle' },
  { id: 4, title: 'PK挑战', desc: '小红同学向你发起了PK挑战', time: '2天前', read: true, icon: 'trophy' },
])
function goBack() { uni.navigateBack() }

async function loadNotifications() {
  if (APP_CONFIG.DATA_MODE !== 1) return
  try {
    const res = await notificationApi.getList()
    if (res && Array.isArray(res)) {
      notifications.value = res.map((n: any, i: number) => ({ id: n.id || i + 1, title: n.title, desc: n.desc, time: n.time, read: n.read, icon: n.icon }))
    }
  } catch (e) { console.error('Notifications load error:', e) }
}
onShow(() => { loadNotifications() })
</script>

<template>
  <view class="min-h-100vh bg-appbg">
    <view class="flex items-center justify-between px-4 pt-12 pb-4">
      <view class="p-2 active:opacity-70" @click="goBack"><wd-icon name="arrow-left" size="22px" color="#333" /></view>
      <text class="text-16px font-bold text-textMain">消息通知</text>
      <view class="w-10" />
    </view>
    <view class="mx-4 space-y-3">
      <view v-for="item in notifications" :key="item.id" class="bg-white rounded-16px p-4 border" :style="{ borderColor: item.read ? '#E5E7EB' : '#4A90E2', background: item.read ? '#fff' : '#EBF3FD', boxShadow: '0 2px 8px rgba(0,0,0,0.04)' }">
        <view class="flex items-start gap-3">
          <view class="mt-1"><wd-icon :name="item.icon" size="24px" :color="item.read ? '#999' : '#4A90E2'" /></view>
          <view class="flex-1">
            <view class="flex items-center gap-2">
              <text class="text-14px font-bold text-textMain">{{ item.title }}</text>
              <view v-if="!item.read" class="w-2 h-2 rounded-full" style="background: #EF4444" />
            </view>
            <text class="text-13px text-textSub block mt-1">{{ item.desc }}</text>
            <text class="text-11px text-textLight block mt-2">{{ item.time }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>
