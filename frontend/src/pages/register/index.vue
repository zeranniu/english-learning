<script lang="ts" setup>
import { ref } from 'vue'
import { httpPost } from '@/http/http'

defineOptions({ name: 'Register' })
definePage({
  style: {
    navigationStyle: 'custom',
    navigationBarTitleText: '注册',
  },
})

const form = ref({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: '',
})
const loading = ref(false)

function handleRegister() {
  const { username, password, confirmPassword, nickname } = form.value

  if (!username.trim()) {
    uni.showToast({ title: '请输入用户名', icon: 'none' })
    return
  }
  if (!password.trim()) {
    uni.showToast({ title: '请输入密码', icon: 'none' })
    return
  }
  if (password !== confirmPassword) {
    uni.showToast({ title: '两次密码不一致', icon: 'none' })
    return
  }
  if (password.length < 6) {
    uni.showToast({ title: '密码至少6位', icon: 'none' })
    return
  }

  loading.value = true
  httpPost('/auth/register', {
    username,
    password,
    nickname: nickname.trim() || username,
  })
    .then(() => {
      uni.showToast({ title: '注册成功', icon: 'success' })
      setTimeout(() => {
        uni.navigateBack()
      }, 1500)
    })
    .catch(() => {
      // 错误已在拦截器中处理
    })
    .finally(() => {
      loading.value = false
    })
}
</script>

<template>
  <view class="min-h-100vh bg-appbg flex flex-col items-center justify-center">
    <!-- Logo Area -->
    <view class="mb-10 flex flex-col items-center">
      <view
        class="w-20 h-20 rounded-24px flex items-center justify-center mb-4"
        style="background: #4A90E2; box-shadow: 0 4px 16px rgba(74, 144, 226, 0.3)"
      >
        <wd-icon name="book" size="40px" color="#fff" />
      </view>
      <text class="text-22px font-bold text-textMain">创建新账号</text>
      <text class="text-13px text-textSub mt-2">加入我们，开始学习之旅</text>
    </view>

    <!-- Register Form -->
    <view
      class="bg-white rounded-20px p-6 mb-6"
      style="width: 85%; max-width: 320px; box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08)"
    >
      <view class="mb-4">
        <text class="text-13px text-textSub block mb-2">用户名</text>
        <view class="register-input-shell">
          <input
            v-model="form.username"
            class="register-input text-14px"
            placeholder="请输入用户名"
            placeholder-style="color: #C0C4CC"
          />
        </view>
      </view>

      <view class="mb-4">
        <text class="text-13px text-textSub block mb-2">昵称（选填）</text>
        <view class="register-input-shell">
          <input
            v-model="form.nickname"
            class="register-input text-14px"
            placeholder="请输入昵称"
            placeholder-style="color: #C0C4CC"
          />
        </view>
      </view>

      <view class="mb-4">
        <text class="text-13px text-textSub block mb-2">密码</text>
        <view class="register-input-shell">
          <input
            v-model="form.password"
            type="password"
            class="register-input text-14px"
            placeholder="请输入密码（至少6位）"
            placeholder-style="color: #C0C4CC"
          />
        </view>
      </view>

      <view class="mb-5">
        <text class="text-13px text-textSub block mb-2">确认密码</text>
        <view class="register-input-shell">
          <input
            v-model="form.confirmPassword"
            type="password"
            class="register-input text-14px"
            placeholder="请再次输入密码"
            placeholder-style="color: #C0C4CC"
          />
        </view>
      </view>

      <view
        class="w-full py-3 rounded-lg font-bold text-15px text-center active:opacity-80"
        :style="{ background: loading ? '#93B4F0' : '#4A90E2', color: '#fff' }"
        @click="handleRegister"
      >
        {{ loading ? '注册中...' : '注 册' }}
      </view>
    </view>

    <!-- Login Link -->
    <view class="flex items-center gap-2">
      <text class="text-13px text-textSub">已有账号？</text>
      <text
        class="text-13px font-medium"
        style="color: #4A90E2"
        @click="uni.navigateBack()"
      >
        返回登录
      </text>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.register-input-shell {
  box-sizing: border-box;
  display: flex;
  align-items: center;
  width: 100%;
  max-width: 100%;
  height: 42px;
  padding: 0 16px;
  overflow: hidden;
  background: #f5f7fa;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
}

.register-input {
  box-sizing: border-box;
  flex: 1;
  width: 100%;
  min-width: 0;
  height: 100%;
  padding: 0;
  line-height: 42px;
  background: transparent;
  border: 0;
}
</style>
