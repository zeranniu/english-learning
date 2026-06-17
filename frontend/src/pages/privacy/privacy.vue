<script lang="ts" setup>
import { ref } from 'vue'
import { APP_CONFIG } from '@/config'
import { privacyApi } from '@/api'

defineOptions({ name: 'Privacy' })
definePage({ style: { navigationStyle: 'custom', navigationBarTitleText: '隐私设置' } })

interface PrivacySetting {
  label: string
  desc: string
  enabled: boolean
}

const STORAGE_KEY = 'english_learning_privacy_settings'

const defaultPrivacySettings: PrivacySetting[] = [
  { label: '个人资料公开', desc: '其他用户可以看到你的昵称和头像', enabled: true },
  { label: '学习数据共享', desc: '允许将学习数据同步至云端', enabled: false },
  { label: '消息推送', desc: '接收系统通知和学习提醒', enabled: true },
  { label: '个性化推荐', desc: '根据学习情况推荐单词和练习', enabled: true },
  { label: '参与排行榜', desc: '允许展示积分和学习天数排名', enabled: true },
]

const privacySettings = ref<PrivacySetting[]>(defaultPrivacySettings.map(item => ({ ...item })))

function goBack() {
  uni.navigateBack()
}

function saveLocalSettings() {
  uni.setStorageSync(STORAGE_KEY, privacySettings.value)
}

function loadLocalSettings() {
  const saved = uni.getStorageSync(STORAGE_KEY)
  if (Array.isArray(saved) && saved.length > 0) {
    privacySettings.value = saved
  }
}

function toggleSetting(index: number) {
  privacySettings.value[index].enabled = !privacySettings.value[index].enabled
  saveLocalSettings()
}

function clearCache() {
  uni.showModal({
    title: '清理缓存',
    content: '确定清理本地临时缓存吗？不会删除账号和学习进度。',
    success: (res) => {
      if (res.confirm) {
        uni.showToast({ title: '缓存已清理', icon: 'success' })
      }
    },
  })
}

function exportData() {
  uni.showModal({
    title: '学习数据导出',
    content: '已提交导出申请，处理结果会通过消息通知告知你。',
    showCancel: false,
  })
}

function showDeleteAccountTip() {
  uni.showModal({
    title: '注销账号',
    content: '账号注销需要人工确认。请先通过意见反馈提交申请，我们会协助处理。',
    confirmText: '去反馈',
    success: (res) => {
      if (res.confirm) {
        uni.navigateTo({ url: '/pages/feedback/feedback' })
      }
    },
  })
}

async function loadSettings() {
  loadLocalSettings()
  if (APP_CONFIG.DATA_MODE !== 1) return
  try {
    const res = await privacyApi.getSettings()
    if (res && Array.isArray(res)) {
      privacySettings.value = res
      saveLocalSettings()
    }
  } catch (e) {
    console.error('Privacy load error:', e)
  }
}

onShow(() => {
  loadSettings()
})
</script>

<template>
  <view class="min-h-80vh bg-appbg">
    <view class="flex items-center justify-between px-4 pt-12 pb-4">
      <view class="p-2 active:opacity-70" @click="goBack"><wd-icon name="arrow-left" size="22px" color="#333" /></view>
      <text class="text-16px font-bold text-textMain">隐私设置</text>
      <view class="w-10" />
    </view>

    <view class="mx-4 mb-4 privacy-summary">
      <view class="privacy-summary-icon">
        <wd-icon name="lock" size="22px" color="#4A90E2" />
      </view>
      <view class="flex-1">
        <text class="text-16px font-bold text-textMain block">保护孩子的学习数据</text>
        <text class="text-12px text-textSub mt-1 block leading-18px">控制资料展示、学习数据分析和个性化推荐范围。</text>
      </view>
    </view>

    <view class="mx-4 bg-white rounded-16px border overflow-hidden" style="border-color: #E5E7EB; box-shadow: 0 4px 16px rgba(0,0,0,0.05)">
      <view v-for="(item, index) in privacySettings" :key="item.label" :class="`flex items-center justify-between px-4 py-4 ${index < privacySettings.length - 1 ? 'border-b' : ''}`" style="border-color: #F9FAFB">
        <view class="flex-1 mr-4">
          <text class="text-14px text-textMain font-medium block">{{ item.label }}</text>
          <text class="text-12px text-textSub mt-1 block leading-18px">{{ item.desc }}</text>
        </view>
        <view class="w-40px h-24px rounded-full flex items-center px-0.5" :style="{ background: item.enabled ? '#4A90E2' : '#E5E7EB' }" @click="toggleSetting(index)">
          <view class="w-20px h-20px rounded-full bg-white" :style="{ marginLeft: item.enabled ? 'auto' : '0', boxShadow: '0 1px 3px rgba(0,0,0,0.2)' }" />
        </view>
      </view>
    </view>

    <view class="mx-4 mt-4 bg-white rounded-16px border overflow-hidden" style="border-color: #E5E7EB; box-shadow: 0 4px 16px rgba(0,0,0,0.05)">
      <view class="privacy-action" @click="exportData">
        <text class="text-14px text-textMain">导出学习数据</text>
        <wd-icon name="arrow-right" size="16px" color="#999" />
      </view>
      <view class="privacy-action" @click="clearCache">
        <text class="text-14px text-textMain">清理本地缓存</text>
        <wd-icon name="arrow-right" size="16px" color="#999" />
      </view>
      <view class="privacy-action privacy-action--last" @click="showDeleteAccountTip">
        <text class="text-14px" style="color: #EF4444">注销账号</text>
        <wd-icon name="arrow-right" size="16px" color="#999" />
      </view>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.privacy-summary {
  display: flex;
  gap: 12px;
  padding: 16px;
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
}

.privacy-summary-icon {
  display: flex;
  flex-shrink: 0;
  align-items: center;
  justify-content: center;
  width: 44px;
  height: 44px;
  background: #ebf3fd;
  border-radius: 14px;
}

.privacy-action {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  border-bottom: 1px solid #f9fafb;
}

.privacy-action--last {
  border-bottom: 0;
}
</style>
