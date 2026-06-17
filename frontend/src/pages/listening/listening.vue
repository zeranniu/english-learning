<script lang="ts" setup>
import { APP_CONFIG } from '@/config'
import { listeningApi } from '@/api'

defineOptions({ name: 'Listening' })
definePage({
  style: {
    navigationStyle: 'custom',
    navigationBarTitleText: '听力训练',
  },
})

const isPlaying = ref(true)
const playbackRate = ref(1.0)
const progressWidth = ref(35)
const currentTime = ref('1:18')
const showFeedback = ref(false)
const loading = ref(true)

const lessonTitle = ref('')
const lessonDuration = ref('')
const questionText = ref('')
const currentQuestionId = ref(1)

const options = ref<any[]>([])

// 学习计时
const startTime = ref(Date.now())
const correctCount = ref(0)
const totalQuestions = ref(0)

function togglePlay() {
  isPlaying.value = !isPlaying.value
  uni.showToast({ title: isPlaying.value ? '音频播放中' : '音频暂停', icon: 'none' })
}

function toggleSpeed() {
  if (playbackRate.value === 1.0)
    playbackRate.value = 1.25
  else if (playbackRate.value === 1.25)
    playbackRate.value = 1.5
  else
    playbackRate.value = 1.0
  uni.showToast({ title: `倍速: ${playbackRate.value.toFixed(1)}x`, icon: 'none' })
}

async function selectAnswer(index: number) {
  if (options.value[index]?.correct) {
    correctCount.value++
    showFeedback.value = true
    uni.showToast({ title: '正确！', icon: 'none' })
  }
  else {
    uni.showToast({ title: '答错了，请再仔细听听', icon: 'none' })
  }

  // 完成所有题目后，记录学习会话
  if (showFeedback.value) {
    const endTime = Date.now()
    const durationMinutes = Math.max(1, Math.round((endTime - startTime.value) / 60000))
    const score = correctCount.value

    if (APP_CONFIG.DATA_MODE === 1) {
      try {
        await listeningApi.completeSession({ durationMinutes, score })
      } catch (e) {
        console.error('Listening completeSession error:', e)
      }
    }

    setTimeout(() => {
      uni.showToast({ title: `听力练习完成！得分:${score}/${totalQuestions.value}`, icon: 'none' })
      setTimeout(() => uni.navigateBack(), 2000)
    }, 1500)
  }
}

function goBack() {
  uni.navigateBack()
}

// 从后端加载听力课程数据
async function loadLesson() {
  loading.value = true
  startTime.value = Date.now() // 重置计时
  correctCount.value = 0
  try {
    if (APP_CONFIG.DATA_MODE === 1) {
      // 从后端API获取
      const res = await listeningApi.getLessonDetail(1)
      if (res?.lesson) {
        lessonTitle.value = res.lesson.title
        lessonDuration.value = res.lesson.duration || '3:45'
      }
      if (res?.questions?.length > 0) {
        const q = res.questions[0]
        currentQuestionId.value = q.id || 1
        questionText.value = q.questionText
        totalQuestions.value = 1 // 简化为1题
        options.value = [
          { label: q.optionA, correct: q.correctOption === 'A' },
          { label: q.optionB, correct: q.correctOption === 'B' },
          { label: q.optionC, correct: q.correctOption === 'C' },
        ]
      }
    } else {
      // 静态模拟数据（仅用于开发测试）
      lessonTitle.value = 'Lesson 3 - My Friends'
      lessonDuration.value = '3:45'
      questionText.value = 'Who is the boy in the photo?'
      totalQuestions.value = 1
      options.value = [
        { label: 'The boy is Tom.', correct: true },
        { label: 'The boy is Sam.', correct: false },
        { label: 'The boy is Mike.', correct: false },
      ]
    }
  } catch (e) {
    console.error('Listening loadLesson error:', e)
    uni.showToast({ title: '加载听力课程失败', icon: 'none' })
  } finally {
    loading.value = false
  }
}

onShow(() => { loadLesson() })
</script>

<template>
  <view class="min-h-80vh bg-appbg">
    <!-- Header -->
    <view class="flex items-center justify-between px-4 pt-12 pb-4">
      <view class="p-2 active:opacity-70" @click="goBack">
        <wd-icon name="arrow-left" size="22px" color="#333" />
      </view>
      <text class="text-16px font-bold text-textMain">听力训练</text>
      <view class="w-10" />
    </view>

    <!-- Audio Player -->
    <view class="mx-4 mb-6 bg-white rounded-16px p-5 border" style="border-color: #E5E7EB; box-shadow: 0 4px 16px rgba(0,0,0,0.05)">
      <view class="flex items-center gap-3 mb-4">
        <view class="w-48px h-48px rounded-xl flex items-center justify-center" style="background: #FFF3E6">
          <wd-icon name="headset" size="22px" color="#FF9F43" />
        </view>
        <view class="flex-1">
          <text class="text-14px font-bold text-textMain block">{{ lessonTitle || 'Loading...' }}</text>
          <text class="text-12px text-textSub block mt-1">共 {{ lessonDuration || '3:45' }}</text>
        </view>
      </view>

      <!-- Progress Bar -->
      <view class="w-full bg-gray-100 rounded-full h-1.5 mb-2">
        <view class="h-1.5 rounded-full" :style="{ width: `${progressWidth}%`, background: '#4A90E2' }" />
      </view>
      <view class="flex justify-between text-11px text-textLight">
        <text>{{ currentTime }}</text>
        <text>{{ lessonDuration || '3:45' }}</text>
      </view>

      <!-- Controls -->
      <view class="flex justify-center items-center gap-6 mt-4">
        <view class="active:opacity-70" @click="toggleSpeed">
          <view class="px-2 py-1 bg-gray-100 rounded text-12px text-textSub font-bold">
            {{ playbackRate.toFixed(1) }}x
          </view>
        </view>
        <view class="w-48px h-48px rounded-full flex items-center justify-center active:opacity-80" style="background: #4A90E2" @click="togglePlay">
          <wd-icon :name="isPlaying ? 'pause' : 'right'" size="20px" color="#fff" />
        </view>
        <view class="active:opacity-70" @click="toggleSpeed">
          <wd-icon name="refresh" size="20px" color="#666" />
        </view>
      </view>
    </view>

    <!-- Question -->
    <view class="mx-4 mb-4">
      <text class="text-14px font-bold text-textMain block mb-3">听一听，选一选</text>
      <text class="text-13px text-textSub block mb-4">{{ questionText }}</text>
    </view>

    <!-- Options -->
    <view class="mx-4 space-y-3">
      <view
        v-for="(opt, index) in options"
        :key="index"
        class="bg-white rounded-xl p-3 flex justify-between items-center border active:opacity-80"
        style="border-color: #E5E7EB"
        @click="selectAnswer(index)"
      >
        <text class="text-13px text-textMain">{{ opt.label }}</text>
      </view>
    </view>

    <!-- Feedback -->
    <view v-if="showFeedback" class="mx-4 mt-4 p-4 rounded-xl border" style="background: #F0FDF4; border-color: #BBF7D0">
      <view class="flex items-center gap-2 mb-1">
        <wd-icon name="check-circle" size="16px" color="#2ECC71" />
        <text class="text-13px font-bold" style="color: #2ECC71">回答正确！</text>
      </view>
      <text class="text-12px text-textSub">音频中提到了 "The boy is Tom"，你听对了吗？</text>
    </view>

    <view class="h-10" />
  </view>
</template>
