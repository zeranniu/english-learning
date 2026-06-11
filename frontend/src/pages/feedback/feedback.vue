<script lang="ts" setup>
import { APP_CONFIG } from '@/config'
import { feedbackApi } from '@/api'

defineOptions({ name: 'Feedback' })
definePage({ style: { navigationStyle: 'custom', navigationBarTitleText: '意见反馈' } })

const feedbackType = ref('建议')
const content = ref('')
const contact = ref('')
const types = ['建议', 'Bug反馈', '功能需求', '其他']

async function submitFeedback() {
  if (!content.value.trim()) { uni.showToast({ title: '请输入反馈内容', icon: 'none' }); return }
  if (APP_CONFIG.DATA_MODE === 1) {
    try {
      await feedbackApi.submit({ feedbackType: feedbackType.value, content: content.value, contact: contact.value })
    } catch (e) { console.error('Feedback submit error:', e) }
  }
  uni.showToast({ title: '反馈提交成功，感谢！', icon: 'none' })
  setTimeout(() => uni.navigateBack(), 1500)
}
function goBack() { uni.navigateBack() }
</script>

<template>
  <view class="min-h-100vh bg-appbg">
    <view class="flex items-center justify-between px-4 pt-12 pb-4">
      <view class="p-2 active:opacity-70" @click="goBack"><wd-icon name="arrow-left" size="22px" color="#333" /></view>
      <text class="text-16px font-bold text-textMain">意见反馈</text>
      <view class="w-10" />
    </view>
    <view class="mx-4 mb-4">
      <text class="text-14px font-bold text-textMain block mb-3">反馈类型</text>
      <view class="flex flex-wrap gap-2">
        <view v-for="type in types" :key="type" class="px-4 py-2 rounded-full text-13px border active:opacity-80" :style="feedbackType === type ? { background: '#4A90E2', color: '#fff', borderColor: '#4A90E2' } : { background: '#fff', color: '#333', borderColor: '#E5E7EB' }" @click="feedbackType = type">{{ type }}</view>
      </view>
    </view>
    <view class="mx-4 mb-4">
      <text class="text-14px font-bold text-textMain block mb-3">反馈内容</text>
      <view class="bg-white rounded-xl border p-3" style="border-color: #E5E7EB">
        <textarea v-model="content" class="w-full text-14px text-textMain" placeholder="请描述你的建议或遇到的问题..." :style="{ minHeight: '120px' }" />
      </view>
    </view>
    <view class="mx-4 mb-6">
      <text class="text-14px font-bold text-textMain block mb-3">联系方式（选填）</text>
      <input v-model="contact" class="bg-white rounded-xl border px-4 py-3 text-14px" style="border-color: #E5E7EB" placeholder="手机号或邮箱" />
    </view>
    <view class="mx-4">
      <view class="w-full py-4 rounded-xl font-bold text-15px text-center active:opacity-80" style="background: #4A90E2; color: #fff" @click="submitFeedback">提交反馈</view>
    </view>
  </view>
</template>
