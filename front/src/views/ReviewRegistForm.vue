<template>
  <div class="max-w-2xl mx-auto p-6">
    <!-- 제목 -->
    <h1 class="text-2xl font-bold text-gray-800 mb-6">후기 작성</h1>

    <!-- 작성 폼 -->
    <form @submit.prevent="submitReview" class="bg-white border rounded shadow-sm p-6 space-y-5">
      <!-- 이름 입력 -->
      <div>
        <label for="author" class="block text-sm font-medium text-gray-700 mb-1">이름</label>
        <input
          id="author"
          v-model="form.author"
          type="text"
          required
          maxlength="10"
          placeholder="이름을 입력하세요"
          class="w-full border rounded px-3 py-2 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-200"
        />
      </div>

      <!-- 내용 입력 -->
      <div>
        <label for="content" class="block text-sm font-medium text-gray-700 mb-1">후기 내용</label>
        <textarea
          id="content"
          v-model="form.content"
          required
          rows="5"
          maxlength="500"
          placeholder="후기를 남겨주세요 (최대 500자)"
          class="w-full border rounded px-3 py-2 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-200 resize-none"
        ></textarea>
      </div>

      <!-- 버튼 -->
      <div class="flex justify-end gap-3">
        <button
          type="button"
          @click="cancel"
          class="px-4 py-2 rounded border border-gray-300 text-gray-600 hover:bg-gray-100"
        >
          취소
        </button>
        <button
          type="submit"
          class="px-4 py-2 rounded bg-blue-600 text-white font-medium hover:bg-blue-700"
        >
          등록
        </button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const form = ref({
  author: '',
  content: ''
})

function submitReview() {
  // 실제로는 API로 전송
  alert('후기가 등록되었습니다!')
  // 후기 게시판으로 이동 (예: /reviews)
  router.push('/reviews/list')
}

function cancel() {
  // 이전 페이지로 이동 또는 게시판으로 이동
  router.back()
}
</script>

<style scoped>
/* 필요시 추가 스타일 */
</style>
