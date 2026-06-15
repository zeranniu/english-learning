<script lang="ts" setup>
defineOptions({ name: 'Study' })
definePage({
  style: {
    navigationStyle: 'custom',
    navigationBarTitleText: '学习中心',
  },
})

const modules = [
  {
    id: 'vocab',
    title: '单词学习',
    desc: '掌握课本核心词汇',
    icon: 'book',
    iconColor: '#4A90E2',
    bg: '#EBF3FD',
    border: '#BFDBFE',
    target: '/pages/vocab/vocab',
  },
  {
    id: 'listening',
    title: '听力训练',
    desc: '提升英语听力水平',
    icon: 'headset',
    iconColor: '#FF9F43',
    bg: '#FFF3E6',
    border: '#FED7AA',
    target: '/pages/listening/listening',
  },
  {
    id: 'reading',
    title: '阅读理解',
    desc: '增强阅读理解能力',
    icon: 'file',
    iconColor: '#2ECC71',
    bg: '#E8F8EF',
    border: '#A7F3D0',
    target: '/pages/reading/reading',
  },
  {
    id: 'grammar',
    title: '语法练习',
    desc: '巩固语法知识点',
    icon: 'edit',
    iconColor: '#8B5CF6',
    bg: '#F3EEFF',
    border: '#DDD6FE',
    target: '/pages/grammar/grammar',
  },
]

function navigateTo(url: string) {
  uni.navigateTo({ url })
}
</script>

<template>
  <view class="min-h-80vh bg-appbg">
    <!-- Header -->
    <view class="px-6 pt-12 pb-6">
      <text class="text-20px font-bold text-textMain block">学习中心</text>
      <text class="text-13px text-textSub block mt-1">选择你想要练习的模块</text>
    </view>

    <!-- Module Cards -->
    <view class="px-4 space-y-4">
      <view
        v-for="item in modules"
        :key="item.id"
        class="bg-white rounded-16px p-5 flex items-center gap-4 active:opacity-80"
        :style="{ boxShadow: '0 4px 16px rgba(0,0,0,0.05)', borderWidth: '1px', borderStyle: 'solid', borderColor: item.border }"
        @click="navigateTo(item.target)"
      >
        <view class="w-56px h-56px rounded-16px flex items-center justify-center" :style="{ background: item.bg }">
          <wd-icon :name="item.icon" size="28px" :color="item.iconColor" />
        </view>
        <view class="flex-1">
          <text class="text-16px font-bold text-textMain block">{{ item.title }}</text>
          <text class="text-12px text-textSub block mt-1">{{ item.desc }}</text>
        </view>
        <wd-icon name="arrow-right" size="18px" color="#999" />
      </view>
    </view>

    <!-- PK Challenge Banner -->
    <view class="mx-4 mt-6">
      <view
        class="rounded-16px p-5 text-white relative overflow-hidden active:opacity-80"
        style="background: linear-gradient(135deg, #FFB86C 0%, #FF9F43 100%)"
        @click="navigateTo('/pages/pk/pk')"
      >
        <view class="absolute w-20 h-20 bg-white rounded-full" style="opacity: 0.1; right: -20px; top: -20px" />
        <view class="flex items-center gap-3">
          <view class="w-48px h-48px rounded-full flex items-center justify-center" style="background: rgba(255,255,255,0.2)">
            <wd-icon name="trophy" size="24px" color="#fff" />
          </view>
          <view class="flex-1">
            <text class="text-16px font-bold block">单词PK赛</text>
            <text class="text-12px block mt-1" style="opacity: 0.9">和小伙伴一起PK吧！</text>
          </view>
          <wd-icon name="arrow-right" size="20px" color="#fff" />
        </view>
      </view>
    </view>

    <view class="h-20" />
  </view>
</template>
