<script lang="ts" setup>
import { APP_CONFIG } from '@/config'
import { grammarApi } from '@/api'

defineOptions({ name: 'Grammar' })
definePage({
  style: {
    navigationStyle: 'custom',
    navigationBarTitleText: '语法练习',
  },
})

const grammarQuestions = [
  {
    question: 'I _____ a student.',
    options: ['A. am', 'B. is', 'C. are', 'D. be'],
    correct: 0,
    explanation: '解析：主语第一人称单数 I 对应的 be 动词形式必须是 am。',
  },
  {
    question: 'She _____ like apples.',
    options: ['A. don\'t', 'B. doesn\'t', 'C. isn\'t', 'D. not'],
    correct: 1,
    explanation: '解析：单三人称否定助动词使用 doesn\'t。',
  },
]

const currentIndex = ref(0)
const selectedIndex = ref(-1)
const showExplanation = ref(false)

const currentQ = computed(() => grammarQuestions[currentIndex.value])
const progressText = computed(() => `${currentIndex.value + 1}/${grammarQuestions.length}`)
const progressPercent = computed(() => ((currentIndex.value + 1) / grammarQuestions.length) * 100)

function selectOption(index: number) {
  if (selectedIndex.value !== -1)
    return
  selectedIndex.value = index
  showExplanation.value = true
  if (index === currentQ.value.correct) {
    uni.showToast({ title: '正确！', icon: 'none' })
  }
  else {
    uni.showToast({ title: '答错了，看看解析吧', icon: 'none' })
  }
}

function nextQuestion() {
  if (currentIndex.value < grammarQuestions.length - 1) {
    currentIndex.value++
    selectedIndex.value = -1
    showExplanation.value = false
  }
  else {
    uni.showToast({ title: '语法练习完成！', icon: 'none' })
    setTimeout(() => uni.navigateBack(), 1500)
  }
}

function goBack() {
  uni.navigateBack()
}

async function loadQuestions() {
  if (APP_CONFIG.DATA_MODE !== 1) return
  try {
    const res = await grammarApi.getQuestions()
    if (res && Array.isArray(res) && res.length > 0) {
      grammarQuestions.splice(0, grammarQuestions.length, ...res.map((q: any) => ({
        question: q.questionText,
        options: [q.optionA, q.optionB, q.optionC, q.optionD],
        correct: q.correctOption,
        explanation: q.explanation,
      })))
    }
  } catch (e) { console.error('Grammar load error:', e) }
}
onShow(() => { loadQuestions() })
</script>

<template>
  <view class="min-h-100vh bg-appbg">
    <!-- Header -->
    <view class="flex items-center justify-between px-4 pt-12 pb-4">
      <view class="p-2 active:opacity-70" @click="goBack">
        <wd-icon name="arrow-left" size="22px" color="#333" />
      </view>
      <text class="text-16px font-bold text-textMain">语法练习</text>
      <view class="w-10" />
    </view>

    <!-- Progress -->
    <view class="px-4 mb-6">
      <view class="flex justify-between items-center mb-2">
        <text class="text-12px text-textSub">题目进度</text>
        <text class="text-12px font-bold" style="color: #4A90E2">{{ progressText }}</text>
      </view>
      <view class="w-full bg-gray-100 rounded-full h-2">
        <view class="h-2 rounded-full transition-all" :style="{ width: `${progressPercent}%`, background: '#4A90E2' }" />
      </view>
    </view>

    <!-- Question Card -->
    <view class="mx-4 bg-white rounded-16px p-6 border mb-4" style="border-color: #E5E7EB; box-shadow: 0 4px 16px rgba(0,0,0,0.05)">
      <text class="text-12px text-textLight block mb-2">选择填空</text>
      <text class="text-17px font-bold text-textMain block mb-6">{{ currentQ.question }}</text>

      <view class="space-y-3">
        <view
          v-for="(opt, index) in currentQ.options"
          :key="index"
          :style="{
            padding: '16px',
            borderRadius: '12px',
            fontSize: '14px',
            borderWidth: '1px',
            borderStyle: 'solid',
            borderColor: selectedIndex === -1 ? '#E5E7EB' : index === currentQ.correct ? '#2ECC71' : index === selectedIndex ? '#EF4444' : '#F3F4F6',
            background: selectedIndex === -1 ? '#fff' : index === currentQ.correct ? '#F0FDF4' : index === selectedIndex ? '#FEF2F2' : '#fff',
            color: selectedIndex === -1 ? '#333' : index === currentQ.correct ? '#2ECC71' : index === selectedIndex ? '#EF4444' : '#999',
            fontWeight: selectedIndex === index || index === currentQ.correct ? 'bold' : 'normal',
          }"
          class="active:opacity-80"
          @click="selectOption(index)"
        >
          {{ opt }}
        </view>
      </view>
    </view>

    <!-- Explanation -->
    <view v-if="showExplanation" class="mx-4 mb-4 p-4 rounded-xl border" style="background: #EBF3FD; border-color: #BFDBFE">
      <view class="flex items-center gap-2 mb-1">
        <wd-icon name="bulb" size="16px" color="#4A90E2" />
        <text class="text-13px font-bold" style="color: #4A90E2">解析说明</text>
      </view>
      <text class="text-12px text-textSub">{{ currentQ.explanation }}</text>
    </view>

    <!-- Next Button -->
    <view v-if="selectedIndex !== -1" class="mx-4">
      <view
        class="w-full py-4 rounded-xl font-bold text-15px text-center active:opacity-80"
        style="background: #4A90E2; color: #fff"
        @click="nextQuestion"
      >
        {{ currentIndex < grammarQuestions.length - 1 ? '下一题' : '完成练习' }}
      </view>
    </view>

    <view class="h-10" />
  </view>
</template>
