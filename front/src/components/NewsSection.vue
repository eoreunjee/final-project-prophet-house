<template>
  <section class="max-w-6xl mx-auto mb-20">
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-xl font-bold text-gray-800">부동산 뉴스</h2>
      <router-link to="/news" class="text-blue-600 text-sm hover:underline">전체 뉴스 보기</router-link>
    </div>
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div
        v-for="(item, index) in news.slice(0, 6)"
        :key="index"
        class="p-4 bg-white rounded border shadow-sm">
        <a :href="item.link" target="_blank">
          <h3 class="font-semibold text-gray-800 mb-1" v-html="item.title"></h3>
        </a>
        <p class="text-sm text-gray-600 mb-2" v-html="item.description"></p>
        <p class="text-xs text-gray-400">{{ getRelativeTime(item.pubDate) }}</p>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const news = ref([])

const fetchNews = async () => {
const keywords = ['부동산', '경제']
const query = keywords.join(' OR ')
  try {
    const res = await axios.get('http://localhost:8080/api/news', {
      params: { query }
    })
    news.value = res.data
  } catch (err) {
    console.error('뉴스 불러오기 실패:', err)
  }
}

function getRelativeTime(dateString) {
  const now = new Date()
  const past = new Date(dateString)
  const diffMs = now - past
  const diffSec = Math.floor(diffMs / 1000)
  const diffMin = Math.floor(diffSec / 60)
  const diffHour = Math.floor(diffMin / 60)
  const diffDay = Math.floor(diffHour / 24)

  if (diffSec < 60) return '방금 전'
  if (diffMin < 60) return `${diffMin}분 전`
  if (diffHour < 24) return `${diffHour}시간 전`
  if (diffDay < 7) return `${diffDay}일 전`

  return past.toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}

onMounted(() => {
  fetchNews()
})
</script>
