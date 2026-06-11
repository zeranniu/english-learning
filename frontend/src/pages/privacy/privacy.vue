<script lang="ts" setup>
import { APP_CONFIG } from '@/config'
import { privacyApi } from '@/api'

defineOptions({ name: 'Privacy' })
definePage({ style: { navigationStyle: 'custom', navigationBarTitleText: '隐私设置' } })

const privacySettings = ref([
  { label: '个人资料公开', desc: '其他用户可以看到你的昵称和头像', enabled: true },
  { label: '学习数据共享', desc: '允许将学习数据同步至云端', enabled: false },
  { label: '消息推送', desc: '接收系统通知和学习提醒', enabled: true },
])
function toggleSetting(index: number) { privacySettings.value[index].enabled = !privacySettings.value[index].enabled }
function goBack() { uni.navigateBack() }

async function loadSettings() {
  if (APP_CONFIG.DATA_MODE !== 1) return
  try {
    const res = await privacyApi.getSettings()
    if (res && Array.isArray(res)) privacySettings.value = res
  } catch (e) { console.error('Privacy load error:', e) }
}
onShow(() => { loadSettings() })
</script>

<template>
  <view class="min-h-100vh bg-appbg">
    <view class="flex items-center justify-between px-4 pt-12 pb-4">
      <view class="p-2 active:opacity-70" @click="goBack"><wd-icon name="arrow-left" size="22px" color="#333" /></view>
      <text class="text-16px font-bold text-textMain">隐私设置</text>
      <view class="w-10" />
    </view>
    <view class="mx-4 bg-white rounded-16px border overflow-hidden" style="border-color: #E5E7EB; box-shadow: 0 4px 16px rgba(0,0,0,0.05)">
      <view v-for="(item, index) in privacySettings" :key="item.label" :class="`flex items-center justify-between px-4 py-4 ${index < privacySettings.length - 1 ? 'border-b' : ''}`" style="border-color: #F9FAFB">
        <view class="flex-1 mr-4">
          <text class="text-14px text-textMain font-medium block">{{ item.label }}</text>
          <text class="text-12px text-textSub mt-1 block">{{ item.desc }}</text>
        </view>
        <view class="w-40px h-24px rounded-full flex items-center px-0.5" :style="{ background: item.enabled ? '#4A90E2' : '#E5E7EB' }" @click="toggleSetting(index)">
          <view class="w-20px h-20px rounded-full bg-white" :style="{ marginLeft: item.enabled ? 'auto' : '0', boxShadow: '0 1px 3px rgba(0,0,0,0.2)' }" />
        </view>
      </view>
    </view>
  </view>
</template>
