<template>
  <div class="max-w-full mx-auto w-full flex justify-between items-center font-bold">
    <!-- 왼쪽: 로고 + 메뉴 -->
    <div class="flex items-center space-x-8">
      <h1 class="text-xl text-[#4DA1F5] font-bold whitespace-nowrap">
        <router-link to="/">Prophet House</router-link>
      </h1>
      <nav class="flex space-x-6 text-sm whitespace-nowrap">
        <router-link to="/searchApart" class="text-gray-800 hover:text-[#4DA1F5]">지역별 매물 검색</router-link>
        <router-link to="/reviews/list" class="text-gray-800 hover:text-[#4DA1F5]">후기 게시판</router-link>
      </nav>
    </div>

    <!-- 오른쪽: 로그인 여부에 따라 버튼 분기 -->
    <div class="flex items-center space-x-4 text-sm">
      <template v-if="isLoggedIn">
        <span class="text-gray-700">{{ username }} 님</span>
        <button @click="logout" class="text-gray-800 hover:text-red-500">로그아웃</button>
      </template>
      <template v-else>
        <button @click="showLogin = true" class="text-gray-800 hover:text-[#4DA1F5]">
          로그인/회원가입
        </button>
      </template>
    </div>

    <!-- 로그인 모달 -->
    <LoginModal v-if="showLogin" @close="handleLoginModalClose" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import LoginModal from './LoginModal.vue'

const showLogin = ref(false)
const isLoggedIn = ref(false)
const username = ref('')

// 로그인 상태 체크 (예: localStorage에서 확인)
onMounted(() => {
  const storedName = localStorage.getItem('username')
  if (storedName) {
    isLoggedIn.value = true
    username.value = storedName
  }
})

// 로그인 모달 닫힌 후 상태 업데이트
const handleLoginModalClose = () => {
  showLogin.value = false
  const storedName = localStorage.getItem('username')
  if (storedName) {
    isLoggedIn.value = true
    username.value = storedName
  }
}

// 로그아웃
const logout = () => {
  localStorage.removeItem('username')
  isLoggedIn.value = false
  username.value = ''
  alert('로그아웃 되었습니다.')
}
</script>
