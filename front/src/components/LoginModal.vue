<template>
  <div class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-40">
    <div class="bg-white rounded-2xl shadow-xl w-[370px] p-8 relative">
      <!-- 닫기 버튼 -->
      <button
        class="absolute top-4 right-4 text-gray-400 hover:text-gray-700 text-2xl"
        @click="$emit('close')"
        aria-label="Close"
      >×</button>

      <!-- 로고 -->
      <div class="flex justify-center mb-6 text-[#4DA1F5]">
        Prophet House
      </div>

      <!-- 폼 -->
      <form @submit.prevent="onLogin">
        <div class="mb-4">
          <label class="block text-sm font-bold mb-1" for="id">
            아이디<span class="text-[#0073e9]">*</span>
          </label>
          <input
            id="id"
            v-model="id"
            type="text"
            placeholder="아이디를 입력해 주세요."
            class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#0073e9] text-sm"
            required
          />
        </div>
        <div class="mb-2">
          <label class="block text-sm font-bold mb-1" for="password">
            비밀번호<span class="text-[#0073e9]">*</span>
          </label>
          <input
            id="password"
            v-model="password"
            type="password"
            placeholder="비밀번호를 입력해 주세요."
            class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#0073e9] text-sm"
            required
          />
        </div>
        <div class="flex items-center mb-4">
          <input
            id="saveEmail"
            type="checkbox"
            v-model="saveEmail"
            class="mr-2"
          />
          <label for="saveEmail" class="text-sm text-gray-600">아이디 저장</label>
        </div>
        <button
          type="submit"
          class="w-full bg-[#189dfb] hover:bg-[#0073e9] text-white font-bold py-2 rounded-md mb-2"
        >로그인</button>
        <div class="flex justify-between text-xs text-gray-500 mb-4">
          <span></span>
          <a href="#" class="hover:underline">이메일 · 비밀번호 찾기</a>
        </div>
      </form>

      <!-- 소셜 로그인 -->
      <div class="flex justify-center space-x-5 mb-6">
        <button class="w-10 h-10 rounded-full bg-[#03cf5d] flex items-center justify-center">
          <span class="text-white text-xl font-bold">N</span>
        </button>
        <button class="w-10 h-10 rounded-full bg-[#fee500] flex items-center justify-center">
          <svg width="20" height="20" viewBox="0 0 40 40" fill="none">
            <circle cx="20" cy="20" r="20" fill="#fee500"/>
            <text x="50%" y="55%" text-anchor="middle" fill="#181600" font-size="18" font-weight="bold" dy=".3em">카</text>
          </svg>
        </button>
        <button class="w-10 h-10 rounded-full bg-[#1877f2] flex items-center justify-center">
          <svg width="20" height="20" viewBox="0 0 32 32" fill="white">
            <path d="M29 16c0-7.2-5.8-13-13-13S3 8.8 3 16c0 6.5 4.8 11.8 11 12.8v-9.1H10v-3.7h4V13c0-3.9 2.4-6 5.9-6 1.7 0 3.5.3 3.5.3v3.8h-2c-2 0-2.6 1.2-2.6 2.5v3h4.4l-.7 3.7h-3.7v9.1C24.2 27.8 29 22.5 29 16z"/>
          </svg>
        </button>
      </div>

      <!-- 회원가입 안내 -->
      <div class="border-t pt-4 flex justify-between items-center text-sm">
        <span class="flex items-center text-gray-500">
          <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" d="M16 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2"/>
            <circle cx="12" cy="7" r="4"/>
          </svg>
          회원이 아니신가요?
        </span>
        <router-link
          to="/auth/signup"
          custom
          v-slot="{ navigate }"
        >
          <button
            class="text-[#189dfb] font-bold hover:underline"
            @click="() => { $emit('close'); navigate(); }"
          >회원가입하기</button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const emit = defineEmits(['close'])

const id = ref('')
const password = ref('')
const saveEmail = ref(false)

const router = useRouter()

async function onLogin() {
  try {
    const response = await axios.post('http://localhost:8080/api/user/login', {
      id: id.value,
      password: password.value
    })

    alert("로그인 성공!")
    emit('close')
    router.push('/')
  } catch (error) {
    if (error.response && error.response.status === 401) {
      alert("아이디 또는 비밀번호가 올바르지 않습니다.")
    } else {
      alert("로그인 중 오류가 발생했습니다.")
      console.error(error)
    }
  }
}
</script>