<template>
  <div class="max-w-3xl mx-auto p-6">
    <div class="mb-6">
      <h2 class="text-xl font-bold text-gray-800 mb-2">{{ review.userId }}님의 후기</h2>
      <p class="text-sm text-gray-500 mb-1">{{ formatDate(review.createdAt) }}</p>
      <div class="flex flex-wrap gap-2 text-sm mb-4">
        <span class="px-3 py-1 bg-gray-100 text-gray-700 rounded-full">{{ review.location }}</span>
        <span class="px-3 py-1 bg-gray-100 text-gray-700 rounded-full">{{ review.dealType }}</span>
      </div>
      <p class="text-gray-800 whitespace-pre-wrap">{{ review.content }}</p>
      <!-- 후기 작성자일 때만 수정/삭제 버튼 노출 -->
    <div v-if="userId === review.userId" class="flex justify-end gap-2 mb-6">
        <button @click="editReview" class="bg-yellow-400 hover:bg-yellow-500 text-white px-4 py-2 rounded">수정</button>
        <button @click="deleteReview" class="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded">삭제</button>
    </div>

    </div>

    <!-- 댓글 영역 -->
    <div class="border-t pt-6">
      <h3 class="text-lg font-semibold mb-4">댓글 {{ comments.length }}</h3>

      <!-- 댓글 리스트 -->
      <div v-if="comments.length" class="space-y-3 mb-6">
        <div v-for="comment in comments" :key="comment.commentId" class="p-3 bg-gray-50 border rounded">
          <div class="flex justify-between text-sm text-gray-600 mb-1">
            <span>{{ comment.userId }}</span>
            <span>{{ formatDate(comment.createdAt) }}</span>
          </div>
          <p class="text-gray-800">{{ comment.content }}</p>
          <!-- ✅ 본인 댓글만 삭제 버튼 표시 -->
            <div class="text-right" v-if="userId === comment.userId">
            <button
                @click="deleteComment(comment.commentId)"
                class="text-sm text-red-500 hover:underline"
            >
                삭제
            </button>
            </div>
        </div>
      </div>

        <!-- 댓글 작성 폼 -->
        <form @submit.prevent="submitComment" class="space-y-3">
            <textarea
                v-model="newComment"
                rows="3"
                class="w-full border rounded px-3 py-2 resize-none"
                :placeholder="isLoggedIn ? '댓글을 입력하세요' : ''"
                :disabled="!isLoggedIn"
            ></textarea>

            <!-- 안내 문구는 아래에 별도로 표시 -->
            <p v-if="!isLoggedIn" class="text-sm text-red-500 mt-2">로그인이 필요합니다.</p>

            <div class="text-right">
                <button
                type="submit"
                :disabled="!isLoggedIn"
                class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded disabled:opacity-50"
                >
                댓글 등록
                </button>
            </div>
        </form>


    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'
import { userId } from '@/utils/auth'

const route = useRoute()
const reviewId = route.params.id

const review = ref({})
const comments = ref([])
const newComment = ref('')

const router = useRouter()

const isLoggedIn = ref(!!userId.value)

onMounted(async () => {
  const reviewRes = await axios.get(`http://192.168.205.75:8080/api/reviews/${reviewId}`)
  review.value = reviewRes.data

  const commentRes = await axios.get(`http://192.168.205.75:8080/api/reviews/${reviewId}/comments`)
  comments.value = commentRes.data
})

function formatDate(dateString) {
  const date = new Date(dateString)
  date.setTime(date.getTime() + -9 * 60 * 60 * 1000)
  const y = date.getFullYear()
  const m = String(date.getMonth() + 1).padStart(2, '0')
  const d = String(date.getDate()).padStart(2, '0')
  const h = String(date.getHours()).padStart(2, '0')
  const min = String(date.getMinutes()).padStart(2, '0')
  return `${y}-${m}-${d} ${h}:${min}`
}

async function submitComment() {
  if (!newComment.value.trim()) return
  await axios.post(`http://192.168.205.75:8080/api/reviews/${reviewId}/comments`, {
    userId: userId.value,
    content: newComment.value
  })
  const res = await axios.get(`http://192.168.205.75:8080/api/reviews/${reviewId}/comments`)
  comments.value = res.data
  newComment.value = ''
}

async function deleteComment(commentId) {
  await axios.delete(`http://192.168.205.75:8080/api/reviews/${reviewId}/comments/${commentId}`)
  const res = await axios.get(`http://192.168.205.75:8080/api/reviews/${reviewId}/comments`)
  comments.value = res.data
}

function editReview() {
  router.push(`/reviews/edit/${reviewId}`)
}

async function deleteReview() {
  const ok = confirm("정말 삭제하시겠습니까?");
  if (!ok) return;

  try {
    await axios.delete(`http://192.168.205.75:8080/api/reviews/${reviewId}`);
    alert("삭제 완료!");
    window.location.href = "/reviews/list"; // 또는 router.push
  } catch (err) {
    alert("삭제 실패: " + err.response?.data || err.message);
  }
}



</script>

<style scoped></style>
