<template>
  <div class="max-w-2xl mx-auto p-14">
    <h2 class="text-2xl font-bold text-[#4DA1F5] mb-6">마이페이지</h2>

    <div class="border-b mb-6">
      <nav class="flex space-x-6 text-sm font-medium text-gray-600">
        <button @click="tab = 'info'" :class="tab === 'info' ? 'text-[#4DA1F5] border-b-2 border-[#4DA1F5] font-bold text-lg' : ''">내 정보</button>
        <button @click="tab = 'wishlist'" :class="tab === 'wishlist' ? 'text-[#4DA1F5] border-b-2 border-[#4DA1F5] font-bold text-lg' : ''">찜 목록</button>
        <button @click="tab = 'reviews'" :class="tab === 'reviews' ? 'text-[#4DA1F5] border-b-2 border-[#4DA1F5] font-bold text-lg' : ''">내가 쓴 게시글</button>
        <button @click="tab = 'comments'" :class="tab === 'comments' ? 'text-[#4DA1F5] border-b-2 border-[#4DA1F5] font-bold text-lg' : ''">내 댓글</button>
      </nav>
    </div>

    <!-- 내 정보 탭 -->
    <div v-if="tab === 'info'" class="space-y-5">
      <div class="grid grid-cols-1 gap-3">
        <form @submit.prevent="updateMyinfo" class="bg-white border rounded shadow-sm p-6 space-y-5">
          <div>
            <label class="block text-sm font-semibold">아이디</label>
            <input type="text" :value="user.id" disabled class="w-full border rounded px-3 py-2 bg-gray-100" />
          </div>
          <div>
            <label class="block text-sm font-semibold">이름</label>
            <input type="text"  v-model="form.name" class="w-full border rounded px-3 py-2" />
          </div>
          <div>
            <label class="block text-sm font-semibold">이메일</label>
            <input type="email" v-model="form.email" class="w-full border rounded px-3 py-2" />
          </div>
          <div>
            <label class="block text-sm font-semibold">생일</label>
            <input type="date"  v-model="form.birth" class="w-full border rounded px-3 py-2" />
          </div>
          <div>
            <label class="block text-sm font-semibold">가입일</label>
            <input type="text" :value="user.joinedAt" disabled class="w-full border rounded px-3 py-2 bg-gray-100" />
          </div>
          <div>
            <label class="block text-sm font-semibold">비밀번호 변경</label>
            <input type="password" v-model="form.password" class="w-full border rounded px-3 py-2" />
          </div>
          <div class="text-center">
            <button class="bg-[#4DA1F5] hover:bg-[#2D0AFF] text-white px-20 py-3 rounded w-full">수정</button>
            <button
              type="button"
              @click="withdraw"
              class="text-sm text-gray-500 hover:underline"
            >
              회원 탈퇴
            </button>
          </div>
        </form>
      </div>
      
    </div>

    <!-- 찜 목록 탭 -->
    <div v-if="tab === 'wishlist'" class="space-y-4">
      <p v-if="wishlist.length < 1">찜한 매물이 없습니다</p>
      <div v-for="item in wishlist" :key="item.id" class="p-4 border rounded shadow-sm">
        <p class="font-semibold">{{ item.title }}</p>
        <p class="text-sm text-gray-500">{{ item.address }}</p>
      </div>
    </div>

    <!-- 내가 쓴 게시글 탭 -->
    <div v-if="tab === 'reviews'" class="space-y-4">
      <p v-if="reviews.length < 1">작성한 게시글이 없습니다</p>
      <div
        v-for="review in reviews"
        :key="review.reviewId"
        class="p-4 border rounded shadow-sm bg-white"
      >
        <!-- 작성일 -->
        <div class="flex items-center mb-3">
          <span class="text-sm text-gray-400">{{ formatDate(review.createdAt) }}</span>
        </div>

        <!-- 지역, 유형 -->
        <div class="flex flex-wrap gap-2 mb-4 text-sm">
          <span class="px-3 py-1 bg-gray-100 text-gray-700 rounded-full">{{ review.location }}</span>
          <span class="px-3 py-1 bg-gray-100 text-gray-700 rounded-full">{{ review.dealType }}</span>
        </div>

        <!-- 본문 -->
        <p class="text-gray-800 whitespace-pre-wrap">{{ review.content }}</p>

        <!-- 댓글 수 -->
        <router-link
          :to="`/reviews/detail/${review.reviewId}`"
          class="text-sm text-blue-500 hover:underline mt-2 inline-block"
        >
          댓글 {{ review.commentCount }}
        </router-link>
      </div>
    </div>

    <!-- 내가 쓴 댓글 탭 -->
    <div v-if="tab === 'comments'" class="space-y-4">
      <p v-if="comments.length < 1">작성한 댓글이 없습니다</p>
      <div
        v-for="comment in comments"
        :key="comment.commentId"
        class="p-4 border rounded shadow-sm bg-white"
      >
        <!-- 댓글 본문 -->
        <p class="text-sm text-gray-800">{{ comment.content }}</p>

        <!-- 작성일 + 원글 이동 링크 -->
        <div class="mt-2 flex justify-between items-center text-sm text-gray-500">
        <span>{{ formatDate(comment.createdAt) }}</span>
        <router-link
            :to="`/reviews/detail/${comment.reviewId}`"
            class="text-blue-500 hover:underline"
        >
            원글 보기 →
        </router-link>
        </div>
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

// 유저 정보 (수정)
const form = ref({
  name: '',
  email: '',
  birth: '',
  joinedAt: ''
})

const wishlist = ref([])
const reviews = ref([])
const comments = ref([])

onMounted(async () => {
  const token = localStorage.getItem('token')
  if (!token) return alert('로그인이 필요합니다.')

  try {
    const res = await axios.get('http://localhost:8080/api/user/me', {
      headers: { Authorization: `Bearer ${token}` }
    })

    const data = res.data
    user.value = {
      id: data.id,
      name: data.name,
      email: data.email,
      birth: data.birthDate,
      joinedAt: data.createdAt?.slice(0, 10)
    }

    form.value.name = data.name
    form.value.email = data.email
    form.value.birth = data.birthDate

    // ✅ 본인이 쓴 리뷰 불러오기
    const reviewRes = await axios.get('http://localhost:8080/api/reviews/myReviews', {
      params: { userId: data.id },
      headers: { Authorization: `Bearer ${token}` }
    })
    reviews.value = reviewRes.data

    const commentRes = await axios.get('http://localhost:8080/api/reviews/myComments', {
        params: { userId: data.id },
        headers: { Authorization: `Bearer ${token}` }
    })
    comments.value = commentRes.data


  } catch (err) {
    console.error('사용자 정보 조회 실패:', err)
    alert('로그인 세션이 만료되었거나 오류가 발생했습니다.')
  }
})

const updateMyinfo = async () => {
  const token = localStorage.getItem('token')
  if (!token) return alert('로그인이 필요합니다.')

  try {
    await axios.put('http://localhost:8080/api/user/update', {
      id: user.value.id,
      name: form.value.name,
      email: form.value.email,
      birthDate: form.value.birth,
      password: form.value.password || null
    }, {
      headers: { Authorization: `Bearer ${token}` }
    })

    alert('정보가 수정되었습니다.')
    console.log('?')
    user.value.name = form.value.name
    user.value.email = form.value.email
    user.value.birth = form.value.birth
    form.value.password = '' // 입력 초기화

  } catch (err) {
    console.error('정보 수정 실패:', err)
    alert('정보 수정 중 오류가 발생했습니다.')
  }
}

function formatDate(dateString) {
  if (!dateString) return '-'

  try {
    const date = new Date(dateString)
    date.setTime(date.getTime() + -9 * 60 * 60 * 1000) // KST 보정

    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    const hour = String(date.getHours()).padStart(2, '0')
    const minute = String(date.getMinutes()).padStart(2, '0')

    return `${year}-${month}-${day} ${hour}:${minute}`
  } catch (e) {
    console.log('formatDate()에러: ',e);
    return '-'
  }
}

const withdraw = async () => {
  if (!confirm('정말 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.')) return

  const token = localStorage.getItem('token')
  try {
    await axios.delete(`http://localhost:8080/api/user/${user.value.id}`, {
      headers: { Authorization: `Bearer ${token}` }
    })
    alert('회원 탈퇴가 완료되었습니다.')
    localStorage.removeItem('token')
    window.location.href = '/' // 홈으로 이동
  } catch (err) {
    console.error('회원 탈퇴 실패:', err)
    alert('탈퇴 중 오류가 발생했습니다.')
  }
}

</script>

<style scoped>
</style>