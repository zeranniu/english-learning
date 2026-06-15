<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { useTokenStore } from '@/store/token'
import { httpGet } from '@/http/http'

defineOptions({ name: 'Profile' })
definePage({
  style: {
    navigationStyle: 'custom',
    navigationBarTitleText: '我的',
  },
})

// 用户信息
const userInfo = ref({
  nickname: '',
  grade: '',
  studentNo: '',
  studyDays: 0,
  totalScore: 0,
  level: 1,
})

const menuItems = [
  { icon: 'notification', label: '消息通知', target: '/pages/notifications/notifications' },
  { icon: 'clock-circle', label: '学习提醒', target: '/pages/reminders/reminders' },
  { icon: 'moon', label: '深色模式', target: '', toggle: true },
  { icon: 'lock', label: '隐私设置', target: '/pages/privacy/privacy' },
  { icon: 'user-group', label: '家长中心', target: '', badge: '新功能' },
  { icon: 'edit', label: '意见反馈', target: '/pages/feedback/feedback' },
  { icon: 'info-circle', label: '关于我们', target: '/pages/about/about' },
]

const darkMode = ref(false)

// 获取用户信息
async function loadUserInfo() {
  try {
    const data: any = await httpGet('/auth/profile')
    if (data) {
      userInfo.value = {
        nickname: data.nickname || '用户',
        grade: data.grade || '',
        studentNo: data.studentNo || '',
        studyDays: data.studyDays || 0,
        totalScore: data.totalScore || 0,
        level: data.level || 1,
      }
    }
  } catch (e) {
    console.error('获取用户信息失败:', e)
  }
}

onMounted(() => {
  loadUserInfo()
})

function handleClick(item: typeof menuItems[0]) {
  if (item.toggle) {
    darkMode.value = !darkMode.value
    return
  }
  if (item.target) {
    uni.navigateTo({ url: item.target })
  }
}

async function handleLogout() {
  uni.showModal({
    title: '提示',
    content: '确定要退出登录吗？',
    success: async (res) => {
      if (res.confirm) {
        const tokenStore = useTokenStore()
        await tokenStore.logout()
        uni.showToast({ title: '已退出登录', icon: 'success' })
        // 跳转到登录页
        uni.reLaunch({ url: '/pages/login/index' })
      }
    },
  })
}
</script>

<template>
  <view class="min-h-80vh bg-appbg">
    <!-- Profile Header -->
    <view class="px-6 pt-12 pb-6 flex flex-col items-center">
      <view class="w-80px h-80px rounded-full flex items-center justify-center overflow-hidden mb-3 border-2 border-white" style="background: #EBF3FD; box-shadow: 0 2px 8px rgba(0,0,0,0.1)">
        <wd-icon name="user" size="40px" color="#4A90E2" />
      </view>
      <text class="text-18px font-bold text-textMain">{{ userInfo.nickname }}</text>
      <text class="text-13px text-textSub mt-1">{{ userInfo.grade }} · 学号 {{ userInfo.studentNo }}</text>
    </view>

    <!-- Stats Row -->
    <view class="mx-4 mb-6 bg-white rounded-16px p-4 flex justify-around border" style="border-color: #E5E7EB; box-shadow: 0 4px 16px rgba(0,0,0,0.05)">
      <view class="text-center">
        <text class="text-20px font-bold block" style="color: #4A90E2">{{ userInfo.studyDays }}</text>
        <text class="text-11px text-textSub mt-1 block">学习天数</text>
      </view>
      <view class="w-1px bg-gray-100" />
      <view class="text-center">
        <text class="text-20px font-bold block" style="color: #FF9F43">{{ userInfo.totalScore }}</text>
        <text class="text-11px text-textSub mt-1 block">总积分</text>
      </view>
      <view class="w-1px bg-gray-100" />
      <view class="text-center">
        <text class="text-20px font-bold block" style="color: #2ECC71">Lv.{{ userInfo.level }}</text>
        <text class="text-11px text-textSub mt-1 block">当前等级</text>
      </view>
    </view>

    <!-- Menu List -->
    <view class="mx-4 bg-white rounded-16px border overflow-hidden" style="border-color: #E5E7EB; box-shadow: 0 4px 16px rgba(0,0,0,0.05)">
      <view
        v-for="(item, index) in menuItems"
        :key="item.label"
        :class="`flex items-center justify-between px-4 py-4 active:bg-gray-50 ${index < menuItems.length - 1 ? 'border-b border-gray-50' : ''}`"
        @click="handleClick(item)"
      >
        <view class="flex items-center gap-3">
          <wd-icon :name="item.icon" size="20px" color="#333" />
          <text class="text-14px text-textMain">{{ item.label }}</text>
        </view>
        <view class="flex items-center gap-2">
          <text v-if="item.badge" class="text-10px text-white px-2 py-0.5 rounded-full" style="background: #EF4444">{{ item.badge }}</text>
          <view v-if="item.toggle" class="w-40px h-24px rounded-full flex items-center px-0.5" :style="{ background: darkMode ? '#4A90E2' : '#E5E7EB' }" @click.stop="darkMode = !darkMode">
            <view class="w-20px h-20px rounded-full bg-white" :style="{ marginLeft: darkMode ? 'auto' : '0', boxShadow: '0 1px 3px rgba(0,0,0,0.2)' }" />
          </view>
          <wd-icon v-else-if="item.target" name="arrow-right" size="16px" color="#999" />
        </view>
      </view>
    </view>

    <!-- Logout Button -->
    <view class="mx-4 mt-6">
      <view
        class="bg-white rounded-16px border py-4 flex items-center justify-center active:opacity-70"
        style="border-color: #E5E7EB; box-shadow: 0 4px 16px rgba(0,0,0,0.05)"
        @click="handleLogout"
      >
        <wd-icon name="logout" size="20px" color="#EF4444" />
        <text class="text-14px font-bold" style="color: #EF4444; margin-left: 8px">退出登录</text>
      </view>
    </view>

    <view class="h-20" />
  </view>
</template>
