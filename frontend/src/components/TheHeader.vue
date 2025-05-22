<template>
  <div class="max-w-full mx-auto w-full flex justify-between items-center font-bold">
    <!-- 왼쪽: 로고 + 메뉴 -->
    <div class="flex items-center space-x-8">
      <h1 class="text-xl text-[#4DA1F5] font-bold whitespace-nowrap">
        <router-link to="/">Prophet House</router-link>
      </h1>
      <nav class="flex space-x-6 text-sm whitespace-nowrap">
        <router-link to="/searchApart" class="text-gray-800 hover:text-[#4DA1F5]">매매 실거래가</router-link>
        <router-link to="/reviews/list" class="text-gray-800 hover:text-[#4DA1F5]">후기 게시판</router-link>
        <router-link to=""></router-link>
      </nav>
    </div>

    <!-- 오른쪽: 로그인 여부에 따라 버튼 분기 -->
    <div class="flex items-center space-x-4 text-sm">
      <template v-if="username">
        <span class="text-gray-700">{{ username }} 님</span>
        <!-- ✅ 마이페이지 버튼 추가 -->
        <router-link
          to="/user/myPage"
          class="text-gray-800 hover:text-[#4DA1F5]"
        >
          마이페이지
        </router-link>
        <button @click="logout" class="text-gray-800 hover:text-red-500">로그아웃</button>
      </template>
      <template v-else>
        <button @click="openLoginModal" class="text-gray-800 hover:text-[#4DA1F5]">
          로그인/회원가입
        </button>
      </template>
    </div>

    <!-- 로그인 모달 -->
    <LoginModal v-if="showLoginModal" @close="closeLoginModal" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { userId, username } from '@/utils/auth'
import LoginModal from './LoginModal.vue'

const router = useRouter()
const showLoginModal = ref(false)

function logout() {
  localStorage.removeItem('token')
  username.value = null
  userId.value = null
  router.push('/')
}

function openLoginModal() {
  showLoginModal.value = true
}

function closeLoginModal() {
  showLoginModal.value = false
}
</script>
