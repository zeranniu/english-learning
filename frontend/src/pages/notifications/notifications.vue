<script lang="ts" setup>
import { computed, ref } from 'vue'
import { APP_CONFIG } from '@/config'
import { notificationApi } from '@/api'

defineOptions({ name: 'Notifications' })
definePage({ style: { navigationStyle: 'custom', navigationBarTitleText: '消息通知' } })

interface NotificationItem {
  id: number
  title: string
  desc: string
  time: string
  read: boolean
  icon: string
}

const notifications = ref<NotificationItem[]>([
  { id: 1, title: '系统通知', desc: '恭喜完成今日单词学习！', time: '10分钟前', read: false, icon: 'notification' },
  { id: 2, title: '成就解锁', desc: '你已解锁"单词达人"徽章！', time: '2小时前', read: false, icon: 'gift' },
  { id: 3, title: '学习提醒', desc: '今天的阅读任务还没完成哦', time: '昨天', read: true, icon: 'clock-circle' },
  { id: 4, title: 'PK挑战', desc: '小红同学向你发起了PK挑战', time: '2天前', read: true, icon: 'trophy' },
])

const unreadCount = computed(() => notifications.value.filter(item => !item.read).length)

function goBack() {
  uni.navigateBack()
}

async function loadNotifications() {
  if (APP_CONFIG.DATA_MODE !== 1) return
  try {
    const res = await notificationApi.getList()
    if (res && Array.isArray(res)) {
      notifications.value = res.map((n: any, i: number) => ({
        id: n.id || i + 1,
        title: n.title || '消息通知',
        desc: n.desc || n.content || '',
        time: n.time || n.createdAt || '',
        read: Boolean(n.read),
        icon: n.icon || 'notification',
      }))
    }
  } catch (e) {
    console.error('Notifications load error:', e)
  }
}

async function markAsRead(item: NotificationItem) {
  if (item.read) return
  item.read = true
  if (APP_CONFIG.DATA_MODE !== 1) return
  try {
    await notificationApi.markAsRead(item.id)
  } catch (e) {
    console.error('Notification mark read error:', e)
  }
}

function markAllRead() {
  if (unreadCount.value === 0) {
    uni.showToast({ title: '暂无未读消息', icon: 'none' })
    return
  }
  notifications.value.forEach((item) => {
    item.read = true
  })
  uni.showToast({ title: '已全部标为已读', icon: 'success' })
}

onShow(() => {
  loadNotifications()
})
</script>

<template>
  <view class="min-h-80vh bg-appbg">
    <view class="flex items-center justify-between px-4 pt-12 pb-4">
      <view class="p-2 active:opacity-70" @click="goBack"><wd-icon name="arrow-left" size="22px" color="#333" /></view>
      <text class="text-16px font-bold text-textMain">消息通知</text>
      <text class="text-13px" style="color: #4A90E2" @click="markAllRead">全部已读</text>
    </view>

    <view class="mx-4 mb-4 notification-summary">
      <view>
        <text class="text-18px font-bold text-white block">{{ unreadCount }} 条未读消息</text>
        <text class="text-12px text-white opacity-85 mt-1 block">学习提醒、成就奖励和系统消息会集中展示在这里</text>
      </view>
      <wd-icon name="notification" size="30px" color="#fff" />
    </view>

    <view class="mx-4 space-y-3">
      <view
        v-for="item in notifications"
        :key="item.id"
        class="notification-card bg-white rounded-16px p-4 border active:opacity-80"
        :class="{ 'notification-card--unread': !item.read }"
        @click="markAsRead(item)"
      >
        <view class="flex items-start gap-3">
          <view class="notification-icon mt-1" :class="{ 'notification-icon--read': item.read }">
            <wd-icon :name="item.icon" size="22px" :color="item.read ? '#999' : '#4A90E2'" />
          </view>
          <view class="flex-1 min-w-0">
            <view class="flex items-center justify-between gap-2">
              <text class="text-14px font-bold text-textMain">{{ item.title }}</text>
              <view v-if="!item.read" class="w-2 h-2 rounded-full flex-shrink-0" style="background: #EF4444" />
            </view>
            <text class="text-13px text-textSub block mt-1 leading-20px">{{ item.desc }}</text>
            <text class="text-11px text-textLight block mt-2">{{ item.time }}</text>
          </view>
        </view>
      </view>

      <view v-if="notifications.length === 0" class="empty-card">
        <wd-icon name="notification" size="32px" color="#CBD5E1" />
        <text class="text-13px text-textSub mt-2 block">暂无消息通知</text>
      </view>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.notification-summary {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 18px;
  background: linear-gradient(135deg, #4a90e2 0%, #7bb7ff 100%);
  border-radius: 18px;
  box-shadow: 0 8px 20px rgba(74, 144, 226, 0.22);
}

.notification-card {
  border-color: #e5e7eb;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.notification-card--unread {
  background: #ebf3fd;
  border-color: #4a90e2;
}

.notification-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 38px;
  height: 38px;
  background: #ebf3fd;
  border-radius: 12px;
}

.notification-icon--read {
  background: #f8fafc;
}

.empty-card {
  padding: 32px 16px;
  text-align: center;
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 16px;
}
</style>
