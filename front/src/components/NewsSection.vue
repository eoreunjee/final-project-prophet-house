<template>
  <section class="max-w-6xl mx-auto mb-20">
    <div class="flex justify-between items-center mb-6">
      <div class="flex items-center space-x-4">
        <h2 class="text-xl font-bold text-gray-800">키워드 뉴스</h2>
        <!-- 키워드 버튼 -->
        <div class="flex space-x-2">
          <button
            v-for="keyword in keywords"
            :key="keyword"
            @click="selectKeyword(keyword)"
            :class="[
              'px-3 py-1 rounded-full text-xs font-semibold border transition',
              selectedKeyword === keyword
                ? 'bg-blue-100 text-blue-700 border-blue-400'
                : 'bg-gray-100 text-gray-500 border-gray-300 hover:bg-blue-50'
            ]"
          >
            #{{ keyword }}
          </button>
        </div>
      </div>
      <!-- <router-link to="/news" class="text-blue-600 text-sm hover:underline">전체 뉴스 보기</router-link> -->
    </div>
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div
        v-for="(item, index) in news.slice(0, 6)"
        :key="index"
        class="p-4 bg-white rounded border shadow-sm"
      >
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
import { ref, onMounted, watch } from 'vue'
import axios from 'axios'

const keywords = ['부동산', '금리', '아파트', '주택']
const selectedKeyword = ref(keywords[0]) // 기본값: '부동산'
const news = ref([])

const fetchNews = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/news', {
      params: { query: selectedKeyword.value }
    })
    news.value = res.data
  } catch (err) {
    console.error('뉴스 불러오기 실패:', err)
  }
}

function selectKeyword(keyword) {
  selectedKeyword.value = keyword
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

// 키워드 변경시 뉴스 다시 불러오기
watch(selectedKeyword, fetchNews)

onMounted(fetchNews)
</script>
