<template>
  <div class="max-w-5xl mx-auto p-6">
    <h2 class="text-2xl font-bold text-[#4DA1F5] mb-6">마이페이지</h2>

    <div class="border-b mb-6">
      <nav class="flex space-x-6 text-sm font-medium text-gray-600">
        <button @click="tab = 'info'" :class="tab === 'info' ? 'text-[#4DA1F5] border-b-2 border-[#4DA1F5]' : ''">내 정보</button>
        <button @click="tab = 'wishlist'" :class="tab === 'wishlist' ? 'text-[#4DA1F5] border-b-2 border-[#4DA1F5]' : ''">찜 목록</button>
        <button @click="tab = 'reviews'" :class="tab === 'reviews' ? 'text-[#4DA1F5] border-b-2 border-[#4DA1F5]' : ''">내가 쓴 게시글</button>
        <button @click="tab = 'comments'" :class="tab === 'comments' ? 'text-[#4DA1F5] border-b-2 border-[#4DA1F5]' : ''">내 댓글</button>
      </nav>
    </div>

    <!-- 내 정보 탭 -->
    <div v-if="tab === 'info'" class="space-y-4">
      <div class="grid grid-cols-2 gap-4">
        <div>
          <label class="block text-sm font-semibold">아이디</label>
          <input type="text" :value="user.id" disabled class="w-full border rounded px-3 py-2 bg-gray-100" />
        </div>
        <div>
          <label class="block text-sm font-semibold">이름</label>
          <input type="text" :value="user.name" class="w-full border rounded px-3 py-2" />
        </div>
        <div>
          <label class="block text-sm font-semibold">이메일</label>
          <input type="email" :value="user.email" class="w-full border rounded px-3 py-2" />
        </div>
        <div>
          <label class="block text-sm font-semibold">생일</label>
          <input type="date" :value="user.birth" class="w-full border rounded px-3 py-2" />
        </div>
        <div>
          <label class="block text-sm font-semibold">가입일</label>
          <input type="text" :value="user.joinedAt" disabled class="w-full border rounded px-3 py-2 bg-gray-100" />
        </div>
        <div>
          <label class="block text-sm font-semibold">비밀번호</label>
          <input type="password" value="********" disabled class="w-full border rounded px-3 py-2 bg-gray-100" />
        </div>
      </div>
      <div class="text-right">
        <button class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded">수정</button>
      </div>
    </div>

    <!-- 찜 목록 탭 -->
    <div v-if="tab === 'wishlist'" class="space-y-4">
      <div v-for="item in wishlist" :key="item.id" class="p-4 border rounded shadow-sm">
        <p class="font-semibold">{{ item.title }}</p>
        <p class="text-sm text-gray-500">{{ item.address }}</p>
      </div>
    </div>

    <!-- 내가 쓴 게시글 탭 -->
    <div v-if="tab === 'reviews'" class="space-y-4">
      <div v-for="review in reviews" :key="review.reviewId" class="p-4 border rounded shadow-sm">
        <p class="font-semibold">{{ review.location }} - {{ review.dealType }}</p>
        <p class="text-sm text-gray-600 whitespace-pre-wrap">{{ review.content }}</p>
      </div>
    </div>

    <!-- 내가 쓴 댓글 탭 -->
    <div v-if="tab === 'comments'" class="space-y-4">
      <div v-for="comment in comments" :key="comment.commentId" class="p-4 border rounded shadow-sm">
        <p class="text-sm text-gray-600">{{ comment.content }}</p>
        <p class="text-xs text-gray-400">작성일: {{ formatDate(comment.createdAt) }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

// 초기 탭
const tab = ref('info')

// 유저 정보
const user = ref({
  id: '',
  name: '',
  email: '',
  birth: '',
  joinedAt: ''
})

// 페이지 마운트 시 사용자 정보 불러오기
onMounted(async () => {
  const token = localStorage.getItem('token')
  if (!token) return alert('로그인이 필요합니다.')

  try {
    const response = await axios.get('http://localhost:8080/api/user/me', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })

    const data = response.data
    user.value = {
      id: data.id,
      name: data.name,
      email: data.email,
      birth: data.birthDate,       // 백엔드에서 birth_date as birthDate로 내려옴
      joinedAt: data.createdAt?.slice(0, 10)    // created_at as createdAt
    }

  } catch (err) {
    console.error('사용자 정보 조회 실패:', err)
    alert('로그인 세션이 만료되었거나 오류가 발생했습니다.')
  }
})
</script>

<style scoped>
button {
  transition: all 0.2s ease-in-out;
}
</style>