<script lang="ts" setup>
import { APP_CONFIG } from '@/config'
import { aboutApi } from '@/api'

defineOptions({ name: 'About' })
definePage({ style: { navigationStyle: 'custom', navigationBarTitleText: '关于我们' } })

const infoItems = [
  { label: '版本号', value: 'v1.0.0' },
  { label: '适用年龄', value: '6-12岁' },
  { label: '开发团队', value: '少儿英语学习团队' },
]
function goBack() { uni.navigateBack() }

async function loadInfo() {
  if (APP_CONFIG.DATA_MODE !== 1) return
  try {
    const res = await aboutApi.getInfo()
    if (res && Array.isArray(res)) infoItems.splice(0, infoItems.length, ...res)
  } catch (e) { console.error('About load error:', e) }
}
onShow(() => { loadInfo() })
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
    <view class="mx-4 bg-white rounded-16px border overflow-hidden" style="border-color: #E5E7EB; box-shadow: 0 4px 16px rgba(0,0,0,0.05)">
      <view v-for="(item, index) in infoItems" :key="item.label" :class="`flex justify-between items-center px-4 py-4 ${index < infoItems.length - 1 ? 'border-b' : ''}`" style="border-color: #F9FAFB">
        <text class="text-14px text-textMain">{{ item.label }}</text>
        <text class="text-14px text-textSub">{{ item.value }}</text>
      </view>
    </view>
    <view class="text-center mt-10">
      <text class="text-12px text-textLight">Copyright 2024 少儿英语学习</text>
      <text class="text-12px text-textLight block mt-1">All Rights Reserved</text>
    </view>
  </view>
</template>
