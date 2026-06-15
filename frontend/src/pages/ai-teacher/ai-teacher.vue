<script lang="ts" setup>
defineOptions({ name: 'AiTeacher' })
definePage({ style: { navigationStyle: 'custom', navigationBarTitleText: 'AI老师' } })

const messages = ref([
  { role: 'assistant', content: 'Hi! I\'m your AI English teacher. How can I help you today?' },
  { role: 'user', content: 'What is the difference between "a" and "an"?' },
  { role: 'assistant', content: 'Great question! We use "a" before words that start with a consonant sound, like "a book" or "a cat". We use "an" before words that start with a vowel sound (a, e, i, o, u), like "an apple" or "an egg".' },
])
const inputText = ref('')

function sendMessage() {
  if (!inputText.value.trim()) return
  messages.value.push({ role: 'user', content: inputText.value })
  const question = inputText.value
  inputText.value = ''
  setTimeout(() => {
    messages.value.push({ role: 'assistant', content: `That's a good question about "${question}". Let me explain... This is a simulated response.` })
  }, 800)
}
function goBack() { uni.navigateBack() }
</script>

<template>
  <view class="min-h-80vh bg-appbg flex flex-col">
    <view class="flex items-center justify-between px-4 pt-12 pb-4 bg-white border-b" style="border-color: #F3F4F6">
      <view class="p-2 active:opacity-70" @click="goBack"><wd-icon name="arrow-left" size="22px" color="#333" /></view>
      <view class="flex items-center gap-2"><wd-icon name="robot" size="20px" color="#4A90E2" /><text class="text-16px font-bold text-textMain">AI英语老师</text></view>
      <view class="w-10" />
    </view>
    <scroll-view scroll-y class="flex-1 px-4 py-4" style="max-height: calc(100vh - 200px)">
      <view class="space-y-4">
        <view v-for="(msg, index) in messages" :key="index" :class="`flex ${msg.role === 'user' ? 'justify-end' : 'justify-start'}`">
          <view :style="{ maxWidth: '80%', borderRadius: '16px', padding: '12px 16px', background: msg.role === 'user' ? '#4A90E2' : '#fff', color: msg.role === 'user' ? '#fff' : '#333', border: msg.role !== 'user' ? '1px solid #E5E7EB' : 'none', boxShadow: '0 2px 8px rgba(0,0,0,0.04)' }">
            <text :style="{ fontSize: '14px', color: msg.role === 'user' ? '#fff' : '#333' }">{{ msg.content }}</text>
          </view>
        </view>
      </view>
    </scroll-view>
    <view class="px-4 py-3 bg-white border-t flex items-center gap-3 pb-safe" style="border-color: #F3F4F6">
      <input v-model="inputText" class="flex-1 bg-gray-100 rounded-full px-4 py-2.5 text-14px" placeholder="输入你的问题..." @confirm="sendMessage" />
      <view class="w-40px h-40px rounded-full flex items-center justify-center active:opacity-80" style="background: #4A90E2" @click="sendMessage">
        <wd-icon name="arrow-right" size="18px" color="#fff" />
      </view>
    </view>
  </view>
</template>
