<template>
  <div class="flex justify-center items-center min-h-screen bg-gray-50">
    <div class="w-full max-w-md bg-white p-8 rounded-lg shadow-md border border-gray-200">
      <h2 class="text-2xl font-bold text-center text-blue-600 mb-6">회원가입</h2>
      <form class="space-y-4">
        <!-- 프로필 이미지 -->
        <div>
          <label class="block mb-1 font-medium text-sm text-gray-700">프로필 이미지</label>
          <input
            type="file"
            accept="image/*"
            class="w-full text-sm file:mr-4 file:py-2 file:px-4 file:rounded file:border-0 file:text-white file:bg-blue-600 hover:file:bg-blue-700"
          />
        </div>

        <!-- 이름 -->
        <div>
          <label class="block mb-1 font-medium text-sm text-gray-700">이름</label>
          <input
            type="text"
            placeholder="이름을 입력하세요"
            class="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <!-- 이메일 -->
        <div>
          <label class="block mb-1 font-medium text-sm text-gray-700">이메일</label>
          <input
            type="email"
            placeholder="example@email.com"
            class="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <!-- 생년월일 -->
        <div>
          <label class="block mb-1 font-medium text-sm text-gray-700">생년월일</label>
          <input
            type="date"
            class="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <!-- 아이디 -->
        <div>
          <label class="block mb-1 font-medium text-sm text-gray-700">아이디</label>
          <input
            type="text"
            placeholder="아이디를 입력하세요"
            class="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <!-- 비밀번호 -->
        <div>
          <label class="block mb-1 font-medium text-sm text-gray-700">비밀번호</label>
          <input
            type="password"
            placeholder="비밀번호"
            class="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <!-- 비밀번호 확인 -->
        <div>
          <label class="block mb-1 font-medium text-sm text-gray-700">비밀번호 확인</label>
          <input
            type="password"
            placeholder="비밀번호 확인"
            class="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <!-- 가입 버튼 -->
        <div>
          <button
            type="submit"
            class="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 rounded"
          >
            회원가입
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
// 향후 v-model이나 유효성 검증 추가 가능
</script>

<style scoped></style>
