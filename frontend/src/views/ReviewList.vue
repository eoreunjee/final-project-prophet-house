<template>
  <div class="max-w-6xl mx-auto p-6">
    <!-- 제목과 글쓰기 버튼 -->
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-800">후기 게시판</h1>
      <router-link
        to="/reviews/regist"
        class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded font-medium"
      >
        후기 작성
      </router-link>
    </div>

    <!-- 후기 리스트 -->
    <div class="space-y-4 mb-6">
      <div
        v-for="review in pagedReviews"
        :key="review.reviewId"
        class="p-4 border rounded shadow-sm bg-white"
      >
        <!-- 작성자 + 작성일 -->
        <div class="flex items-center mb-3">
          <span class="text-gray-800 font-semibold mr-3">{{ review.userId }}</span>
          <span class="text-sm text-gray-400">{{ formatDate(review.createdAt )}}</span>
        </div>

        <!-- 지역, 유형 -->
        <div class="flex flex-wrap gap-2 mb-5 text-sm">
          <span class="px-3 py-1 bg-gray-100 text-gray-700 rounded-full">{{ review.location }}</span>
          <span class="px-3 py-1 bg-gray-100 text-gray-700 rounded-full">{{ review.dealType }}</span>
        </div>

        <!-- 본문 -->
        <p class="text-gray-800 mb-4 whitespace-pre-wrap">{{ review.content }}</p>
        <router-link :to="`/reviews/detail/${review.reviewId}`" class="text-sm text-blue-500 hover:underline">
          댓글 {{ review.commentCount }}
        </router-link>
      </div>
    </div>

    <!-- 페이지네이션 -->
    <div class="flex justify-center space-x-2">
      <button
        v-for="page in totalPages"
        :key="page"
        @click="currentPage = page"
        :class="[
          'px-3 py-1 rounded',
          currentPage === page ? 'bg-blue-600 text-white' : 'bg-gray-100 text-gray-600'
        ]"
      >
        {{ page }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const reviews = ref([])
const currentPage = ref(1)
const pageSize = 7

onMounted(async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/reviews')
    reviews.value = res.data
  } catch (err) {
    console.error('후기 목록 불러오기 실패', err)
  }
})

const totalPages = computed(() => Math.ceil(reviews.value.length / pageSize))

const pagedReviews = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return reviews.value.slice(start, start + pageSize)
})

function formatDate(dateString) {
  if (!dateString) return '-'

  try {
    const date = new Date(dateString)
    // UTC → KST 보정
    date.setTime(date.getTime() + -9 * 60 * 60 * 1000)

    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    const hour = String(date.getHours()).padStart(2, '0')
    const minute = String(date.getMinutes()).padStart(2, '0')

    return `${year}-${month}-${day} ${hour}:${minute}`
  } catch (e) {
    return '-'
  }
}
</script>

<style scoped></style>
