<script lang="ts" setup>
import { APP_CONFIG } from '@/config'
import { growthApi } from '@/api'

defineOptions({ name: 'Growth' })
definePage({
  style: {
    navigationStyle: 'custom',
    navigationBarTitleText: '成长中心',
  },
})

const badges = ref([
  { icon: 'gift', label: '单词达人', unlocked: true, color: '#4A90E2' },
  { icon: 'star-fill', label: '阅读之星', unlocked: true, color: '#EAB308' },
  { icon: 'headset', label: '听力大师', unlocked: false, hint: '再听两篇听力即可解锁!' },
  { icon: 'trophy', label: '学习冠军', unlocked: false, hint: '连续获得3次PK胜利解锁!' },
])

function showBadgeHint(badge: typeof badges.value[0]) {
  if (!badge.unlocked && badge.hint) {
    uni.showToast({ title: badge.hint, icon: 'none' })
  }
}

async function loadGrowth() {
  if (APP_CONFIG.DATA_MODE !== 1) return
  try {
    const res = await growthApi.getInfo()
    if (res) {
      levelInfo.value = { level: res.level, levelName: res.levelName, exp: res.exp, expMax: res.expMax }
      if (res.badges) badges.value = res.badges.map((b: any) => ({ icon: b.badgeName === '单词达人' ? 'gift' : b.badgeName === '阅读之星' ? 'star-fill' : b.badgeName === '听力大师' ? 'headset' : 'trophy', label: b.badgeName, unlocked: b.unlocked, color: '#4A90E2' }))
    }
  } catch (e) { console.error('Growth load error:', e) }
}
onShow(() => { loadGrowth() })
</script>

<template>
  <view class="min-h-80vh bg-appbg">
    <!-- Header -->
    <view class="px-6 pt-12 pb-4">
      <text class="text-20px font-bold text-textMain block text-center">成长中心</text>
    </view>

    <!-- Level Card -->
    <view class="mx-4 mb-6 bg-white rounded-16px p-5 border" style="border-color: #E5E7EB; box-shadow: 0 4px 16px rgba(0,0,0,0.05)">
      <view class="flex items-center gap-4">
        <view class="w-56px h-56px rounded-full flex items-center justify-center" style="background: linear-gradient(135deg, #FFB86C 0%, #FF9F43 100%)">
          <wd-icon name="clock-circle" size="28px" color="#fff" />
        </view>
        <view class="flex-1">
          <view class="flex justify-between items-center mb-1">
            <text class="text-17px font-bold text-textMain">Lv.5 <text class="text-14px font-normal text-textSub ml-1">小学学霸</text></text>
            <text class="text-12px text-textLight">规则 ></text>
          </view>
          <view class="w-full bg-gray-100 rounded-full h-2 mt-2">
            <view class="h-2 rounded-full" style="background: #FF9F43; width: 70%" />
          </view>
          <view class="flex justify-between mt-1">
            <text class="text-12px text-textLight">经验值</text>
            <text class="text-12px font-bold" style="color: #FF9F43">850<text class="text-textLight font-normal">/1200</text></text>
          </view>
        </view>
      </view>
    </view>

    <!-- Badges -->
    <view class="mx-4 mb-6 bg-white rounded-16px p-4 border" style="border-color: #E5E7EB; box-shadow: 0 4px 16px rgba(0,0,0,0.05)">
      <text class="text-14px font-bold text-textMain block mb-4">成就徽章</text>
      <view class="grid grid-cols-4 gap-2 text-center">
        <view
          v-for="badge in badges"
          :key="badge.label"
          :style="{ opacity: badge.unlocked ? 1 : 0.4 }"
          @click="showBadgeHint(badge)"
        >
          <view class="flex justify-center">
            <wd-icon :name="badge.icon" size="36px" :color="badge.unlocked ? badge.color : '#9CA3AF'" />
          </view>
          <text :class="`text-10px mt-1 block ${badge.unlocked ? 'text-textMain' : 'text-textLight'}`">{{ badge.label }}</text>
        </view>
      </view>
    </view>

    <!-- Growth Tree -->
    <view class="mx-4 rounded-16px p-6 flex flex-col items-center justify-center relative overflow-hidden border" style="background: #F0FDF4; border-color: #BBF7D0; min-height: 200px">
      <text class="text-14px font-bold absolute top-4 left-4" style="color: #166534">成长树</text>
      <view class="mt-6 relative">
        <wd-icon name="Fire" size="80px" color="#16A34A" />
        <view class="absolute bg-white px-2 py-1 rounded-lg text-12px text-textMain font-bold border" style="top: -8px; right: -40px; border-color: #BBF7D0">
          新晋学霸
        </view>
      </view>
      <view class="w-full h-12 absolute bottom-0 left-0 rounded-b-16px" style="background: #DCFCE7" />
    </view>

    <view class="h-20" />
  </view>
</template>
