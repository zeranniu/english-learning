<script lang="ts" setup>
import { APP_CONFIG } from '@/config'
import { mistakeApi } from '@/api'

defineOptions({ name: 'Mistakes' })
definePage({ style: { navigationStyle: 'custom', navigationBarTitleText: '错题本' } })

const activeTab = ref('vocab')
const mistakesData: Record<string, { id: string, title: string, info: string, tag: string }[]> = {
  vocab: [
    { id: '1', title: '1. apple', info: '拼写错误', tag: '词语' },
    { id: '2', title: '2. family', info: '拼写错误', tag: '词语' },
    { id: '3', title: '3. beautiful', info: '多字母错写', tag: '词语' },
    { id: '4', title: '4. because', info: '发音混淆', tag: '词语' },
  ],
  listening: [{ id: '1', title: 'Who is the boy?', info: '主旨混淆（误选 Tom）', tag: '单选' }],
  reading: [{ id: '1', title: 'Where does Bear live?', info: '细节抓取错误', tag: '主旨' }],
  grammar: [{ id: '1', title: 'I _____ a student. (选 be)', info: 'be动词搭配不清', tag: '选词' }],
}
const tabs = [{ key: 'vocab', label: '单词' }, { key: 'listening', label: '听力' }, { key: 'reading', label: '阅读' }, { key: 'grammar', label: '语法' }]
const realList = ref<any[] | null>(null)
const currentList = computed(() => realList.value || mistakesData[activeTab.value] || [])
function goBack() { uni.switchTab({ url: '/pages/study/study' }) }

async function loadMistakes() {
  if (APP_CONFIG.DATA_MODE !== 1) return
  try {
    const res = await mistakeApi.getList(activeTab.value)
    if (res && Array.isArray(res)) realList.value = res
    else realList.value = null
  } catch (e) { console.error('Mistakes load error:', e); realList.value = null }
}
watch(activeTab, () => { realList.value = null; loadMistakes() })
onShow(() => { loadMistakes() })
</script>

<template>
  <view class="min-h-100vh bg-appbg">
    <view class="flex items-center justify-between px-4 pt-12 pb-4">
      <view class="p-2 active:opacity-70" @click="goBack"><wd-icon name="arrow-left" size="22px" color="#333" /></view>
      <text class="text-16px font-bold text-textMain">错题本</text>
      <view class="w-10" />
    </view>
    <view class="mx-4 flex bg-white p-1 rounded-xl border mb-6" style="border-color: #E5E7EB">
      <view v-for="tab in tabs" :key="tab.key" class="flex-1 text-center py-2 text-12px font-bold rounded-lg" :style="activeTab === tab.key ? { background: '#4A90E2', color: '#fff' } : { color: '#999' }" @click="activeTab = tab.key">{{ tab.label }}</view>
    </view>
    <view class="mx-4 bg-white rounded-16px border p-4 mb-6" style="border-color: #E5E7EB; box-shadow: 0 2px 8px rgba(0,0,0,0.04)">
      <view v-for="item in currentList" :key="item.id" class="flex justify-between items-center py-3 border-b" style="border-color: #F9FAFB">
        <view>
          <text class="text-14px font-bold text-textMain block">{{ item.title }}</text>
          <text class="text-12px text-textLight mt-1 block">{{ item.info }}</text>
        </view>
        <text class="text-11px font-bold px-2 py-0.5 rounded" style="background: #FEF2F2; color: #EF4444">{{ item.tag }}</text>
      </view>
      <view v-if="currentList.length === 0" class="text-center py-8">
        <wd-icon name="no-message" size="40px" color="#D1D5DB" />
        <text class="text-14px text-textLight block mt-2">暂无错题记录，继续加油！</text>
      </view>
    </view>
  </view>
</template>
