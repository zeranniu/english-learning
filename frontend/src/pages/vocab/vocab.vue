<script lang="ts" setup>
import { APP_CONFIG } from '@/config'
import { vocabApi } from '@/api'

defineOptions({ name: 'Vocab' })
definePage({
  style: {
    navigationStyle: 'custom',
    navigationBarTitleText: '单词学习',
  },
})

// 初始为空数组，从后端加载
const vocabList = ref<any[]>([])
const currentIndex = ref(0)
const showTranslation = ref(false)
const loading = ref(true)

// 学习计时
const startTime = ref(Date.now())
const correctCount = ref(0)

const currentWord = computed(() => vocabList.value[currentIndex.value] || {})
const progressText = computed(() => `${currentIndex.value + 1}/${vocabList.value.length}`)
const progressPercent = computed(() => vocabList.value.length > 0 ? ((currentIndex.value + 1) / vocabList.value.length) * 100 : 0)

function speakWord() {
  uni.showToast({ title: `发音: ${currentWord.value.word}`, icon: 'none' })
}

function toggleTranslation() {
  showTranslation.value = !showTranslation.value
}

async function nextWord(known: boolean) {
  if (known) {
    correctCount.value++
    uni.showToast({ title: '太棒了，又记住了一个词！', icon: 'none' })
  }
  else {
    uni.showToast({ title: '已加入高频错题组', icon: 'none' })
  }
  showTranslation.value = false
  currentIndex.value++

  // 完成所有单词后，记录学习会话
  if (currentIndex.value >= vocabList.value.length) {
    const endTime = Date.now()
    const durationMinutes = Math.max(1, Math.round((endTime - startTime.value) / 60000)) // 至少1分钟
    const score = correctCount.value // 每题1分

    if (APP_CONFIG.DATA_MODE === 1) {
      try {
        await vocabApi.completeSession({ durationMinutes, score })
      } catch (e) {
        console.error('Vocab completeSession error:', e)
      }
    }

    uni.showToast({ title: `今日单词全部完成！得分:${score}/${vocabList.value.length}`, icon: 'none' })
    setTimeout(() => uni.navigateBack(), 2000)
  }
}

function goBack() {
  uni.navigateBack()
}

// 从后端加载单词数据
async function loadWords() {
  loading.value = true
  startTime.value = Date.now() // 重置计时
  correctCount.value = 0
  try {
    if (APP_CONFIG.DATA_MODE === 1) {
      // 从后端API获取
      const res = await vocabApi.getWords()
      if (res && res.words) {
        vocabList.value = res.words
      }
    } else {
      // 静态模拟数据（仅用于开发测试）
      vocabList.value = [
        { id: 1, word: 'book', phonetic: '[bʊk]', translation: '书本', icon: 'book', iconColor: '#4A90E2' },
        { id: 2, word: 'car', phonetic: '[kɑːr]', translation: '汽车', icon: 'location', iconColor: '#EF4444' },
        { id: 3, word: 'cat', phonetic: '[kæt]', translation: '小猫', icon: 'compass', iconColor: '#FF9F43' },
        { id: 4, word: 'sun', phonetic: '[sʌn]', translation: '太阳', icon: 'sun', iconColor: '#EAB308' }
      ]
    }
  } catch (e) {
    console.error('Vocab loadWords error:', e)
    uni.showToast({ title: '加载单词失败', icon: 'none' })
  } finally {
    loading.value = false
  }
}

onShow(() => { loadWords() })
</script>

<template>
  <view class="min-h-80vh bg-appbg flex flex-col">
    <!-- Header -->
    <view class="flex items-center justify-between px-4 pt-12 pb-4">
      <view class="p-2 active:opacity-70" @click="goBack">
        <wd-icon name="arrow-left" size="22px" color="#333" />
      </view>
      <text class="text-16px font-bold text-textMain">单词学习</text>
      <view class="w-10" />
    </view>

    <!-- Progress -->
    <view class="px-4 mb-4">
      <view class="flex justify-between items-center mb-2">
        <text class="text-12px text-textSub">学习进度</text>
        <text class="text-12px font-bold" style="color: #4A90E2">{{ progressText }}</text>
      </view>
      <view class="w-full bg-gray-100 rounded-full h-2">
        <view class="h-2 rounded-full transition-all" :style="{ width: `${progressPercent}%`, background: '#4A90E2' }" />
      </view>
    </view>

    <!-- Flashcard -->
    <view class="flex-1 px-4 flex flex-col items-center justify-center">
      <view
        class="bg-white rounded-24px w-full flex flex-col items-center justify-center p-8 active:opacity-90"
        style="box-shadow: 0 8px 30px rgba(0,0,0,0.08); min-height: 320px"
        @click="toggleTranslation"
      >
        <wd-icon :name="currentWord.icon" size="60px" :color="currentWord.iconColor" />
        <text class="text-28px font-bold text-textMain mt-6">{{ currentWord.word }}</text>
        <text class="text-14px text-textLight mt-2">{{ currentWord.phonetic }}</text>

        <view v-if="showTranslation" class="mt-6 px-4 py-2 rounded-xl" style="background: #EBF3FD">
          <text class="text-16px font-medium" style="color: #4A90E2">{{ currentWord.translation }}</text>
        </view>
        <text v-else class="text-12px text-textLight mt-6">点击卡片查看释义</text>
      </view>
    </view>

    <!-- Action Buttons -->
    <view class="px-4 pb-10 flex gap-4">
      <view
        class="flex-1 py-4 rounded-xl font-bold text-15px text-center active:opacity-70 border"
        style="background: #FEF2F2; border-color: #FECACA; color: #EF4444"
        @click="nextWord(false)"
      >
        不认识
      </view>
      <view
        class="w-48px h-48px rounded-full flex items-center justify-center active:opacity-70"
        style="background: #EBF3FD"
        @click="speakWord"
      >
        <wd-icon name="sound" size="20px" color="#4A90E2" />
      </view>
      <view
        class="flex-1 py-4 rounded-xl font-bold text-15px text-center active:opacity-70 border"
        style="background: #F0FDF4; border-color: #BBF7D0; color: #2ECC71"
        @click="nextWord(true)"
      >
        认识
      </view>
    </view>
  </view>
</template>
