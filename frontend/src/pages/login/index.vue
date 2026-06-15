<script lang="ts" setup>
import { ref } from 'vue'
import { useTokenStore } from '@/store/token'

defineOptions({ name: 'Login' })
definePage({
  style: {
    navigationStyle: 'custom',
    navigationBarTitleText: '登录',
  },
})

const username = ref('xiaoming')
const password = ref('123456')
const loading = ref(false)
const isLoggingIn = ref(false)

// 页面显示时检查是否已登录
onShow(() => {
  // 如果正在登录中，不重复跳转
  if (isLoggingIn.value) return
  
  const tokenStore = useTokenStore()
  if (tokenStore.updateNowTime().hasValidLogin) {
    // 已登录，直接跳转首页
    uni.switchTab({ url: '/pages/index/index' })
  }
})

async function handleLogin() {
  if (!username.value.trim()) {
    uni.showToast({ title: '请输入用户名', icon: 'none' })
    return
  }
  if (!password.value.trim()) {
    uni.showToast({ title: '请输入密码', icon: 'none' })
    return
  }

  loading.value = true
  isLoggingIn.value = true
  try {
    const tokenStore = useTokenStore()
    await tokenStore.login({ username: username.value, password: password.value })
    // 登录成功，跳转到首页
    uni.switchTab({ url: '/pages/index/index' })
  } catch (e) {
    console.error('Login failed:', e)
    isLoggingIn.value = false
  } finally {
    loading.value = false
  }
}

function goToRegister() {
  uni.navigateTo({ url: '/pages/register/index' })
}
</script>

<template>
  <view class="login-page min-h-100vh bg-appbg flex flex-col items-center justify-center">
    <!-- Logo Area -->
    <view class="mb-10 flex flex-col items-center">
      <view class="w-80px h-80px rounded-20px flex items-center justify-center mb-4" style="background: #4A90E2; box-shadow: 0 4px 16px rgba(74,144,226,0.3)">
        <wd-icon name="book" size="40px" color="#fff" />
      </view>
      <text class="text-22px font-bold text-textMain">少儿英语学习</text>
      <text class="text-13px text-textSub mt-2">登录以同步学习数据</text>
    </view>

    <!-- Login Form -->
    <view class="login-card w-full bg-white rounded-20px mb-6" style="box-shadow: 0 4px 16px rgba(0,0,0,0.05)">
      <view class="mb-5">
        <text class="text-14px text-textSub block mb-2">用户名</text>
        <view class="login-input-shell">
          <input
            v-model="username"
            class="login-input text-14px"
            placeholder="请输入用户名"
            placeholder-style="color: #C0C4CC"
          />
        </view>
      </view>
      <view class="mb-6">
        <text class="text-14px text-textSub block mb-2">密码</text>
        <view class="login-input-shell">
          <input
            v-model="password"
            type="password"
            class="login-input text-14px"
            placeholder="请输入密码"
            placeholder-style="color: #C0C4CC"
          />
        </view>
      </view>
      <view
        class="login-submit w-full py-4 rounded-xl font-bold text-16px text-center active:opacity-80"
        :style="{ background: loading ? '#93B4F0' : '#4A90E2', color: '#fff' }"
        @click="handleLogin"
      >
        {{ loading ? '登录中...' : '登 录' }}
      </view>
    </view>

    <!-- Demo Hint -->
    <view class="demo-card w-full bg-blue-50 rounded-xl border border-blue-100">
      <view class="flex items-center gap-2 mb-2">
        <wd-icon name="info-circle" size="16px" color="#4A90E2" />
        <text class="text-13px font-bold" style="color: #4A90E2">演示账号</text>
      </view>
      <text class="text-12px" style="color: #666">用户名: xiaoming</text>
      <text class="text-12px block" style="color: #666">密码: 123456</text>
    </view>

    <!-- Register Link -->
    <view class="mt-6 flex items-center gap-2">
      <text class="text-13px text-textSub">还没有账号？</text>
      <text class="text-13px font-medium" style="color: #4A90E2" @click="goToRegister">立即注册</text>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.login-page,
.login-card,
.demo-card,
.login-submit,
.login-input-shell {
  box-sizing: border-box;
}

.login-page {
  width: 100%;
  padding: 0 20px;
}

.login-card {
  max-width: 100%;
  padding: 24px;
}

.demo-card {
  max-width: 100%;
  padding: 16px;
}

.login-input-shell {
  display: flex;
  align-items: center;
  width: 100%;
  max-width: 100%;
  height: 42px;
  padding: 0 16px;
  overflow: hidden;
  background: #f8fafc;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
}

.login-input {
  box-sizing: border-box;
  display: block;
  flex: 1 1 0;
  width: 0;
  min-width: 0;
  max-width: 100%;
  height: 100%;
  padding: 0;
  line-height: 42px;
  background: transparent;
  border: 0;
}
</style>
