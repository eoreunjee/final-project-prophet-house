<template>
  <div class="flex justify-center items-center min-h-screen bg-gray-50">
    <div class="w-full max-w-md bg-white p-8 rounded-lg shadow-md border border-gray-200">
      <!-- 프로필 이미지 -->
      <div class="text-2xl flex justify-center mb-6 font-bold text-[#4DA1F5]">
        회원가입
      </div>

      <form class="space-y-5" @submit.prevent="register">
        <!-- 이름 -->
        <div>
          <label class="block mb-1 text-sm font-bold text-gray-700">
            이름<span class="text-[#ff4d4f]">*</span>
          </label>
          <input type="text" v-model="form.name" placeholder="이름을 입력해 주세요."
                 class="w-full px-4 py-2 border border-gray-300 rounded focus:ring-2 focus:ring-[#0073e9]" />
        </div>

        <!-- 아이디 -->
        <div>
          <label class="block mb-1 text-sm font-bold text-gray-700">
            아이디<span class="text-[#ff4d4f]">*</span>
          </label>
          <input type="text" v-model="form.id" placeholder="아이디를 입력해 주세요."
                 class="w-full px-4 py-2 border border-gray-300 rounded focus:ring-2 focus:ring-[#0073e9]" />
        </div>

        <!-- 이메일 -->
        <div>
          <label class="block mb-1 text-sm font-bold text-gray-700">
            이메일<span class="text-[#ff4d4f]">*</span>
          </label>
          <input type="email" v-model="form.email" placeholder="이메일을 입력해 주세요."
                 class="w-full px-4 py-2 border border-gray-300 rounded focus:ring-2 focus:ring-[#0073e9]" />
        </div>

        <!-- 비밀번호 -->
        <div>
          <label class="block mb-1 text-sm font-bold text-gray-700">
            비밀번호<span class="text-[#ff4d4f]">*</span>
          </label>
          <input type="password" v-model="form.password" placeholder="비밀번호를 입력해 주세요."
                 class="w-full px-4 py-2 border border-gray-300 rounded focus:ring-2 focus:ring-[#0073e9]" />
          <p class="text-xs text-gray-400 mt-1">
            비밀번호는 영문, 숫자, 특수문자를 조합하여 8자 이상 입력해 주세요.
          </p>
        </div>

        <!-- 비밀번호 확인 -->
        <div>
          <label class="block mb-1 text-sm font-bold text-gray-700">
            비밀번호 확인<span class="text-[#ff4d4f]">*</span>
          </label>
          <input type="password" v-model="form.passwordConfirm" placeholder="비밀번호를 입력해 주세요."
                 class="w-full px-4 py-2 border border-gray-300 rounded focus:ring-2 focus:ring-[#0073e9]" />
        </div>

        <!-- 생년월일 -->
      <div>
        <label class="block mb-1 text-sm font-bold text-gray-700">
          생년월일<span class="text-[#ff4d4f]">*</span>
        </label>
        <input
          type="date"
          v-model="form.birthDate"
          class="w-full px-4 py-2 border border-gray-300 rounded focus:ring-2 focus:ring-[#0073e9]"
        />
      </div>

        <!-- 가입 버튼 -->
        <button type="submit"
                class="w-full bg-[#189dfb] hover:bg-[#0073e9] text-white font-semibold py-2 rounded mt-2">
          가입 완료하기
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

const form = ref({
  name: '',
  id: '',
  email: '',
  password: '',
  passwordConfirm: '',
  birthDate: ''
})

const register = async () => {
  if (form.value.password !== form.value.passwordConfirm) {
    alert('비밀번호가 일치하지 않습니다.')
    return
  }

  try {
    await axios.post('http://localhost:8080/api/user/register', {
      name: form.value.name,
      id: form.value.id,
      email: form.value.email,
      password: form.value.password,
      birthDate: form.value.birthDate
    })
    alert(`회원가입이 완료되었습니다!`)
    router.push('/')
  } catch (error) {
    console.error(error)
    alert(`회원가입 중 오류가 발생했습니다.`)
  }
}
</script>

