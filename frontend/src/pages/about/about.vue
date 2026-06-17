<script lang="ts" setup>
import { ref } from 'vue'
import { APP_CONFIG } from '@/config'
import { aboutApi } from '@/api'

defineOptions({ name: 'About' })
definePage({ style: { navigationStyle: 'custom', navigationBarTitleText: '关于我们' } })

interface InfoItem {
  label: string
  value: string
}

const infoItems = ref<InfoItem[]>([
  { label: '版本号', value: 'v1.0.0' },
  { label: '适用年龄', value: '6-12岁' },
  { label: '开发团队', value: '少儿英语学习团队' },
])

const featureItems = [
  { icon: 'book', title: '词汇学习', desc: '通过图文和复习计划积累核心单词' },
  { icon: 'sound', title: '听力训练', desc: '用趣味材料提升英文听辨能力' },
  { icon: 'chart', title: '成长记录', desc: '持续记录积分、等级和学习天数' },
]

function goBack() {
  uni.navigateBack()
}

function checkUpdate() {
  uni.showToast({ title: '当前已是最新版本', icon: 'success' })
}

function openPrivacy() {
  uni.navigateTo({ url: '/pages/privacy/privacy' })
}

function openFeedback() {
  uni.navigateTo({ url: '/pages/feedback/feedback' })
}

async function loadInfo() {
  if (APP_CONFIG.DATA_MODE !== 1) return
  try {
    const res = await aboutApi.getInfo()
    if (res && Array.isArray(res)) infoItems.value = res
  } catch (e) {
    console.error('About load error:', e)
  }
}

onShow(() => {
  loadInfo()
})
</script>

<template>
  <view class="min-h-80vh bg-appbg">
    <view class="flex items-center justify-between px-4 pt-12 pb-4">
      <view class="p-2 active:opacity-70" @click="goBack"><wd-icon name="arrow-left" size="22px" color="#333" /></view>
      <text class="text-16px font-bold text-textMain">关于我们</text>
      <view class="w-10" />
    </view>
    <view class="flex flex-col items-center mt-8 mb-10">
      <view class="w-80px h-80px rounded-16px flex items-center justify-center mb-4" style="background: #4A90E2; box-shadow: 0 4px 16px rgba(74,144,226,0.3)">
        <wd-icon name="book" size="36px" color="#fff" />
      </view>
      <text class="text-20px font-bold text-textMain">少儿英语学习</text>
      <text class="text-13px text-textSub mt-1">让英语学习更有趣</text>
    </view>

    <view class="mx-4 mb-4 about-intro">
      <text class="text-16px font-bold text-textMain block mb-2">产品介绍</text>
      <text class="text-13px text-textSub leading-22px">通过单词、听力、阅读、语法和成长激励，帮助孩子建立稳定的英语学习习惯，让每一次练习都有反馈、有目标。</text>
    </view>

    <view class="mx-4 mb-4 bg-white rounded-16px border overflow-hidden" style="border-color: #E5E7EB; box-shadow: 0 4px 16px rgba(0,0,0,0.05)">
      <view v-for="(item, index) in featureItems" :key="item.title" :class="`about-feature ${index < featureItems.length - 1 ? 'border-b' : ''}`" style="border-color: #F9FAFB">
        <view class="about-feature-icon">
          <wd-icon :name="item.icon" size="18px" color="#4A90E2" />
        </view>
        <view>
          <text class="text-14px font-bold text-textMain block">{{ item.title }}</text>
          <text class="text-12px text-textSub mt-1 block">{{ item.desc }}</text>
        </view>
      </view>
    </view>

    <view class="mx-4 bg-white rounded-16px border overflow-hidden" style="border-color: #E5E7EB; box-shadow: 0 4px 16px rgba(0,0,0,0.05)">
      <view v-for="(item, index) in infoItems" :key="item.label" :class="`flex justify-between items-center px-4 py-4 ${index < infoItems.length - 1 ? 'border-b' : ''}`" style="border-color: #F9FAFB">
        <text class="text-14px text-textMain">{{ item.label }}</text>
        <text class="text-14px text-textSub">{{ item.value }}</text>
      </view>
    </view>

    <view class="mx-4 mt-4 bg-white rounded-16px border overflow-hidden" style="border-color: #E5E7EB; box-shadow: 0 4px 16px rgba(0,0,0,0.05)">
      <view class="about-action" @click="checkUpdate">
        <text class="text-14px text-textMain">检查更新</text>
        <wd-icon name="arrow-right" size="16px" color="#999" />
      </view>
      <view class="about-action" @click="openPrivacy">
        <text class="text-14px text-textMain">隐私设置</text>
        <wd-icon name="arrow-right" size="16px" color="#999" />
      </view>
      <view class="about-action about-action--last" @click="openFeedback">
        <text class="text-14px text-textMain">意见反馈</text>
        <wd-icon name="arrow-right" size="16px" color="#999" />
      </view>
    </view>

    <view class="text-center mt-10">
      <text class="text-12px text-textLight">Copyright 2024 少儿英语学习</text>
      <text class="text-12px text-textLight block mt-1">All Rights Reserved</text>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.about-intro {
  padding: 16px;
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
}

.about-feature,
.about-action {
  display: flex;
  align-items: center;
  padding: 16px;
}

.about-feature {
  gap: 12px;
}

.about-feature-icon {
  display: flex;
  flex-shrink: 0;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  background: #ebf3fd;
  border-radius: 12px;
}

.about-action {
  justify-content: space-between;
  border-bottom: 1px solid #f9fafb;
}

.about-action--last {
  border-bottom: 0;
}
</style>
