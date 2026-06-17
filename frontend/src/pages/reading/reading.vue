<script lang="ts" setup>
import { APP_CONFIG } from '@/config'
import { readingApi } from '@/api'

defineOptions({ name: 'Reading' })
definePage({
  style: {
    navigationStyle: 'custom',
    navigationBarTitleText: '阅读理解',
  },
})

const activeTab = ref('vocab')
const loading = ref(true)

// 初始为空，从后端加载
const vocabList = ref<any[]>([])
const passageTitle = ref('')
const passage = ref('')
const questions = ref<any[]>([])
const currentQuestion = ref(0)
const selectedAnswer = ref(-1)
const showResult = ref(false)

async function selectAnswer(index: number) {
  // 调用后端API记录学习
  if (APP_CONFIG.DATA_MODE === 1) {
    try {
      await readingApi.answerQuestion({
        questionId: questions.value[currentQuestion.value]?.id || 1,
        answerIndex: index
      })
    } catch (e) {
      console.error('Reading answerQuestion error:', e)
    }
  }

  selectedAnswer.value = index
  showResult.value = true
  if (index === questions.value[currentQuestion.value]?.answer) {
    uni.showToast({ title: '正确！', icon: 'none' })
  } else {
    uni.showToast({ title: '再想想哦~', icon: 'none' })
  }

  // 完成所有题目后，记录学习会话
  if (currentQuestion.value === questions.value.length - 1) {
    const endTime = Date.now()
    const durationMinutes = Math.max(1, Math.round((endTime - startTime.value) / 60000))
    const correctAnswers = questions.value.filter((q, i) => {
      // 这里简化处理，实际应该记录用户的选择
      return q.answer === 0 // 假设第一题选A正确
    }).length

    if (APP_CONFIG.DATA_MODE === 1) {
      try {
        await readingApi.completeSession({ durationMinutes, score: questions.value.length })
      } catch (e) {
        console.error('Reading completeSession error:', e)
      }
    }

    setTimeout(() => {
      uni.showToast({ title: `阅读练习完成！`, icon: 'none' })
      setTimeout(() => uni.navigateBack(), 2000)
    }, 1500)
  }
}

function nextQuestion() {
  if (currentQuestion.value < questions.value.length - 1) {
    currentQuestion.value++
    selectedAnswer.value = -1
    showResult.value = false
  } else {
    uni.showToast({ title: '阅读全部完成！', icon: 'none' })
  }
}

function goBack() { uni.navigateBack() }

// 学习计时
const startTime = ref(Date.now())

// 从后端加载阅读文章数据
async function loadPassage() {
  loading.value = true
  startTime.value = Date.now() // 重置计时
  try {
    if (APP_CONFIG.DATA_MODE === 1) {
      // 从后端API获取
      const res = await readingApi.getPassageDetail(1)
      if (res?.passage) {
        passageTitle.value = res.passage.title
        passage.value = res.passage.content
      }
      if (res?.vocabs) {
        vocabList.value = res.vocabs
      }
      if (res?.questions) {
        questions.value = res.questions.map((q: any) => ({
          id: q.id,
          q: q.questionText,
          options: [q.optionA, q.optionB, q.optionC],
          answer: q.correctOption
        }))
      }
    } else {
      // 静态模拟数据（仅用于开发测试）
      passageTitle.value = 'A Beautiful Village'
      passage.value = 'There is a forest near our village. In the forest, there is a long river. The river goes through the mountain. Over the river, there is a small bridge. On the other side of the bridge, there is a beautiful lake. Many fish live in the lake.'
      vocabList.value = [
        { word: 'forest', meaning: '森林' },
        { word: 'river', meaning: '河流' },
        { word: 'mountain', meaning: '山脉' },
        { word: 'bridge', meaning: '桥' },
        { word: 'village', meaning: '村庄' },
        { word: 'lake', meaning: '湖泊' },
      ]
      questions.value = [
        { q: 'Where is the forest?', options: ['Near the village', 'On the mountain', 'Behind the lake'], answer: 0 },
        { q: 'What is over the river?', options: ['A forest', 'A bridge', 'A village'], answer: 1 },
      ]
    }
  } catch (e) {
    console.error('Reading load error:', e)
    uni.showToast({ title: '加载阅读文章失败', icon: 'none' })
  } finally {
    loading.value = false
  }
}

onShow(() => { loadPassage() })
</script>

<template>
  <view class="min-h-80vh bg-appbg">
    <view class="flex items-center justify-between px-4 pt-12 pb-4">
      <view class="p-2 active:opacity-70" @click="goBack"><wd-icon name="arrow-left" size="22px" color="#333" /></view>
      <text class="text-16px font-bold text-textMain">阅读理解</text>
      <view class="w-10" />
    </view>
    <view class="mx-4 mb-4">
      <text class="text-17px font-bold text-textMain block">A Beautiful Village</text>
      <view class="flex items-center gap-1 mt-1"><wd-icon name="star-fill" size="12px" color="#EAB308" /><wd-icon name="star-fill" size="12px" color="#EAB308" /><text class="text-12px text-textSub ml-1">| 建议: 5分钟</text></view>
    </view>
    <view class="mx-4 flex border-b mb-4" style="border-color: #F3F4F6">
      <view :class="`flex-1 pb-2 text-center text-14px font-bold`" :style="activeTab === 'vocab' ? { color: '#4A90E2', borderBottom: '2px solid #4A90E2' } : { color: '#999', borderBottom: '2px solid transparent' }" @click="activeTab = 'vocab'">生词表</view>
      <view :class="`flex-1 pb-2 text-center text-14px font-bold`" :style="activeTab === 'passage' ? { color: '#4A90E2', borderBottom: '2px solid #4A90E2' } : { color: '#999', borderBottom: '2px solid transparent' }" @click="activeTab = 'passage'">短文内容</view>
    </view>
    <view v-if="activeTab === 'vocab'" class="mx-4 bg-white rounded-16px p-4 border mb-6" style="border-color: #E5E7EB; box-shadow: 0 4px 16px rgba(0,0,0,0.05)">
      <view v-for="item in vocabList" :key="item.word" class="flex justify-between items-center py-3 border-b" style="border-color: #F9FAFB">
        <text class="text-14px font-bold text-textMain">{{ item.word }}</text>
        <text class="text-13px text-textSub">{{ item.meaning }}</text>
      </view>
    </view>
    <view v-if="activeTab === 'passage'" class="mx-4 bg-white rounded-16px p-5 border mb-6" style="border-color: #E5E7EB; box-shadow: 0 4px 16px rgba(0,0,0,0.05)">
      <text class="text-14px text-textMain" style="line-height: 24px">{{ passage }}</text>
    </view>
    <view class="mx-4 mb-6">
      <text class="text-14px font-bold text-textMain block mb-3">阅读理解题</text>
      <view v-for="(qItem, qIndex) in questions" :key="qIndex" class="mb-4">
        <text class="text-13px text-textMain font-medium block mb-2">{{ qIndex + 1 }}. {{ qItem.q }}</text>
        <view v-for="(opt, oIndex) in qItem.options" :key="oIndex" class="mb-2">
          <view :style="{ padding: '12px', borderRadius: '12px', fontSize: '13px', borderWidth: '1px', borderStyle: 'solid', borderColor: selectedAnswer === oIndex && showResult && qIndex === currentQuestion ? (oIndex === qItem.answer ? '#2ECC71' : '#EF4444') : '#E5E7EB', background: selectedAnswer === oIndex && showResult && qIndex === currentQuestion ? (oIndex === qItem.answer ? '#F0FDF4' : '#FEF2F2') : '#fff' }" class="active:opacity-80" @click="currentQuestion === qIndex && selectAnswer(oIndex)">
            {{ String.fromCharCode(65 + oIndex) }}. {{ opt }}
          </view>
        </view>
      </view>
      <view v-if="showResult && currentQuestion < questions.length - 1" class="mt-4 py-3 rounded-xl font-bold text-14px text-center active:opacity-80" style="background: #4A90E2; color: #fff" @click="nextQuestion">下一题</view>
    </view>
    <view class="h-10" />
  </view>
</template>
