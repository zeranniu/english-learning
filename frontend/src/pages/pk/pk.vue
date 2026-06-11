<script lang="ts" setup>
import { APP_CONFIG } from '@/config'
import { pkApi } from '@/api'

defineOptions({ name: 'PK' })
definePage({ style: { navigationStyle: 'custom', navigationBarTitleText: 'PK挑战' } })

const leaderboard = ref([
  { rank: 1, name: '小明同学 (我)', score: 2560, isMe: true, icon: 'user' },
  { rank: 2, name: '小红同学', score: 2456, isMe: false, icon: 'woman' },
  { rank: 3, name: '小刚同学', score: 2345, isMe: false, icon: 'man' },
  { rank: 4, name: '小花同学', score: 2234, isMe: false, icon: 'woman' },
])
const podiumTop = computed(() => [leaderboard.value[1], leaderboard.value[0], leaderboard.value[2]])
function startPK() { uni.showToast({ title: '匹配对手中...', icon: 'none' }) }
function goBack() { uni.navigateBack() }

async function loadRanking() {
  if (APP_CONFIG.DATA_MODE !== 1) return
  try {
    const res = await pkApi.getRanking()
    if (res && Array.isArray(res) && res.length > 0) {
      leaderboard.value = res.map((r: any) => ({ rank: r.rank, name: r.name, score: r.score, isMe: r.isMe, icon: r.isMe ? 'user' : 'woman' }))
    }
  } catch (e) { console.error('PK load error:', e) }
}
onShow(() => { loadRanking() })
</script>

<template>
  <view class="min-h-100vh bg-appbg">
    <view class="flex items-center justify-between px-4 pt-12 pb-4">
      <view class="p-2 active:opacity-70" @click="goBack"><wd-icon name="arrow-left" size="22px" color="#333" /></view>
      <text class="text-18px font-bold text-textMain">单词PK赛</text>
      <view class="w-10" />
    </view>
    <!-- Podium -->
    <view class="flex justify-center items-end mb-8 mt-4" style="height: 200px">
      <view class="flex flex-col items-center z-10" style="margin-right: -10px">
        <view class="w-48px h-48px rounded-full bg-gray-100 flex items-center justify-center mb-2 border-2 border-white"><wd-icon name="woman" size="24px" color="#666" /></view>
        <text class="text-12px text-textMain font-medium mb-1">{{ podiumTop[0].name }}</text>
        <view class="w-20 bg-gray-100 rounded-t-lg flex flex-col items-center pt-2 border" style="height: 96px; border-color: #E5E7EB"><text class="text-24px font-bold" style="color: #9CA3AF">2</text></view>
      </view>
      <view class="flex flex-col items-center z-20">
        <wd-icon name="trophy" size="36px" color="#EAB308" />
        <view class="w-64px h-64px rounded-full flex items-center justify-center mb-2 border-4" style="background: #EBF3FD; border-color: #FFD700"><wd-icon name="user" size="32px" color="#4A90E2" /></view>
        <text class="text-14px text-textMain font-bold mb-1">{{ podiumTop[1].name }}</text>
        <view class="w-24 bg-yellow-50 rounded-t-lg flex flex-col items-center pt-2 border" style="height: 128px; border-color: #FDE047"><text class="text-30px font-bold" style="color: #CA8A04">1</text></view>
      </view>
      <view class="flex flex-col items-center z-10" style="margin-left: -10px">
        <view class="w-48px h-48px rounded-full bg-gray-100 flex items-center justify-center mb-2 border-2 border-white"><wd-icon name="man" size="24px" color="#666" /></view>
        <text class="text-12px text-textMain font-medium mb-1">{{ podiumTop[2].name }}</text>
        <view class="w-20 rounded-t-lg flex flex-col items-center pt-2 border" style="height: 80px; background: #FFF7ED; border-color: #FDBA74"><text class="text-24px font-bold" style="color: #FB923C">3</text></view>
      </view>
    </view>
    <!-- Leaderboard List -->
    <view class="mx-4 bg-white rounded-16px p-4 mb-6 border" style="border-color: #E5E7EB; box-shadow: 0 4px 16px rgba(0,0,0,0.05)">
      <view v-for="item in leaderboard" :key="item.rank" :class="`flex items-center py-2 ${item.isMe ? '-mx-4 px-4 rounded-lg' : ''}`" :style="item.isMe ? { background: '#EBF3FD' } : {}">
        <text class="w-6 text-center font-bold text-14px" :style="{ color: item.rank === 1 ? '#EAB308' : item.rank === 2 ? '#9CA3AF' : item.rank === 3 ? '#FB923C' : '#999' }">{{ item.rank }}</text>
        <view class="w-40px h-40px rounded-full bg-gray-50 flex items-center justify-center mx-3"><wd-icon :name="item.icon" size="20px" color="#666" /></view>
        <text :class="`flex-1 text-14px ${item.isMe ? 'font-bold text-textMain' : 'text-textMain'}`">{{ item.name }}</text>
        <text class="font-bold text-14px" :style="{ color: item.isMe ? '#4A90E2' : '#333' }">{{ item.score }}分</text>
      </view>
    </view>
    <view class="mx-4 mb-10">
      <view class="w-full py-4 rounded-xl font-bold text-17px text-center active:opacity-80" style="background: #FF9F43; color: #fff; box-shadow: 0 2px 8px rgba(0,0,0,0.1)" @click="startPK">开始 PK</view>
    </view>
  </view>
</template>
