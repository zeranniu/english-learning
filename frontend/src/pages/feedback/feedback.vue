<script lang="ts" setup>
import { computed, ref } from 'vue'
import { APP_CONFIG } from '@/config'
import { feedbackApi } from '@/api'

defineOptions({ name: 'Feedback' })
definePage({ style: { navigationStyle: 'custom', navigationBarTitleText: '意见反馈' } })

const STORAGE_KEY = 'english_learning_feedback_records'
const feedbackType = ref('建议')
const content = ref('')
const contact = ref('')
const types = ['建议', 'Bug反馈', '功能需求', '内容纠错', '其他']
const quickTags = ['单词内容', '听力练习', '学习记录', '页面卡顿', '提醒设置']
const canSubmit = computed(() => content.value.trim().length >= 10)

function goBack() {
  uni.navigateBack()
}

function appendTag(tag: string) {
  const prefix = `#${tag} `
  if (!content.value.includes(prefix)) {
    content.value = `${content.value}${content.value ? '\n' : ''}${prefix}`
  }
}

function saveLocalRecord() {
  const records = uni.getStorageSync(STORAGE_KEY)
  const list = Array.isArray(records) ? records : []
  list.unshift({
    id: Date.now(),
    feedbackType: feedbackType.value,
    content: content.value.trim(),
    contact: contact.value.trim(),
    createdAt: new Date().toISOString(),
  })
  uni.setStorageSync(STORAGE_KEY, list.slice(0, 20))
}

async function submitFeedback() {
  if (!canSubmit.value) {
    uni.showToast({ title: '请至少输入10个字', icon: 'none' })
    return
  }
  if (APP_CONFIG.DATA_MODE === 1) {
    try {
      await feedbackApi.submit({ feedbackType: feedbackType.value, content: content.value.trim(), contact: contact.value.trim() })
    } catch (e) {
      console.error('Feedback submit error:', e)
    }
  }
  saveLocalRecord()
  uni.showModal({
    title: '反馈已提交',
    content: '感谢你的反馈，我们会认真查看并持续优化学习体验。',
    showCancel: false,
    success: () => {
      content.value = ''
      contact.value = ''
      feedbackType.value = '建议'
    },
  })
}
</script>

<template>
  <view class="min-h-80vh bg-appbg">
    <view class="flex items-center justify-between px-4 pt-12 pb-4">
      <view class="p-2 active:opacity-70" @click="goBack"><wd-icon name="arrow-left" size="22px" color="#333" /></view>
      <text class="text-16px font-bold text-textMain">意见反馈</text>
      <view class="w-10" />
    </view>

    <view class="mx-4 mb-4 feedback-summary">
      <wd-icon name="edit" size="26px" color="#4A90E2" />
      <view class="ml-3 flex-1">
        <text class="text-16px font-bold text-textMain block">告诉我们你的想法</text>
        <text class="text-12px text-textSub mt-1 block leading-18px">内容错误、功能建议或使用问题，都可以在这里反馈。</text>
      </view>
    </view>

    <view class="mx-4 mb-4">
      <text class="text-14px font-bold text-textMain block mb-3">反馈类型</text>
      <view class="flex flex-wrap gap-2">
        <view v-for="type in types" :key="type" class="px-4 py-2 rounded-full text-13px border active:opacity-80" :style="feedbackType === type ? { background: '#4A90E2', color: '#fff', borderColor: '#4A90E2' } : { background: '#fff', color: '#333', borderColor: '#E5E7EB' }" @click="feedbackType = type">{{ type }}</view>
      </view>
    </view>
    <view class="mx-4 mb-4">
      <text class="text-14px font-bold text-textMain block mb-3">反馈内容</text>
      <view class="feedback-textarea-shell">
        <textarea
          v-model="content"
          class="feedback-textarea text-14px text-textMain"
          maxlength="500"
          placeholder="请描述你的建议或遇到的问题..."
          placeholder-style="color: #C0C4CC"
        />
      </view>
      <view class="flex justify-between items-center mt-2">
        <view class="flex flex-wrap gap-2">
          <view v-for="tag in quickTags" :key="tag" class="quick-tag" @click="appendTag(tag)">{{ tag }}</view>
        </view>
        <text class="text-11px text-gray-400 ml-2">{{ content.length }}/500</text>
      </view>
    </view>
    <view class="mx-4 mb-6">
      <text class="text-14px font-bold text-textMain block mb-3">联系方式（选填）</text>
      <input v-model="contact" class="contact-input text-14px" placeholder="手机号或邮箱" placeholder-style="color: #C0C4CC" />
    </view>
    <view class="mx-4">
      <view class="w-full py-4 rounded-xl font-bold text-15px text-center active:opacity-80" :style="{ background: canSubmit ? '#4A90E2' : '#B8D3F6', color: '#fff' }" @click="submitFeedback">提交反馈</view>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.feedback-summary {
  display: flex;
  align-items: center;
  padding: 16px;
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
}

.feedback-textarea-shell,
.contact-input {
  box-sizing: border-box;
  width: 100%;
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
}

.feedback-textarea-shell {
  padding: 12px;
}

.feedback-textarea {
  width: 100%;
  min-height: 128px;
  line-height: 20px;
}

.contact-input {
  height: 44px;
  padding: 0 14px;
}

.quick-tag {
  padding: 5px 9px;
  color: #4a90e2;
  font-size: 12px;
  background: #ebf3fd;
  border-radius: 999px;
}
</style>
