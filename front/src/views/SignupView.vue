<template>
  <div class="flex justify-center items-center min-h-screen bg-gray-50">
    <div class="w-full max-w-2xl bg-white p-8 rounded-lg shadow-md border border-gray-200">
      <h2 class="text-2xl font-bold text-center text-blue-600 mb-2">회원가입</h2>
      <p class="text-center text-sm text-gray-500 mb-6">부동산 정보 플랫폼의 회원이 되어 다양한 서비스를 이용해보세요.</p>

      <form class="space-y-6">
        <!-- 프로필 이미지 -->
        <div class="flex flex-col items-center">
          <img src="https://via.placeholder.com/100" alt="프로필" class="w-24 h-24 rounded-full mb-2" />
          <label class="inline-block cursor-pointer bg-blue-600 hover:bg-blue-700 text-white text-sm py-1 px-3 rounded">
            프로필 사진 업로드
            <input type="file" accept="image/*" class="hidden" />
          </label>
        </div>

        <!-- 2열 입력 폼 -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <!-- 이름 -->
          <div>
            <label class="block mb-1 text-sm font-medium text-gray-700">이름</label>
            <input type="text" placeholder="이름" class="w-full px-4 py-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500" />
          </div>

          <!-- 생년월일 -->
          <div>
            <label class="block mb-1 text-sm font-medium text-gray-700">생년월일</label>
            <input type="date" class="w-full px-4 py-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500" />
          </div>

          <!-- 이메일 -->
          <div>
            <label class="block mb-1 text-sm font-medium text-gray-700">이메일</label>
            <input type="email" placeholder="example@email.com" class="w-full px-4 py-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500" />
          </div>

          <!-- 아이디 -->
          <div>
            <label class="block mb-1 text-sm font-medium text-gray-700">아이디</label>
            <input type="text" placeholder="아이디" class="w-full px-4 py-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500" />
          </div>

          <!-- 비밀번호 -->
          <div>
            <label class="block mb-1 text-sm font-medium text-gray-700">비밀번호</label>
            <input type="password" placeholder="비밀번호" class="w-full px-4 py-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500" />
          </div>

          <!-- 비밀번호 확인 -->
          <div>
            <label class="block mb-1 text-sm font-medium text-gray-700">비밀번호 확인</label>
            <input type="password" placeholder="비밀번호 확인" class="w-full px-4 py-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500" />
          </div>
        </div>

        <!-- 가입 버튼 -->
        <div>
          <button type="submit" class="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 rounded">
            가입하기
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
// 추후 v-model, 유효성 검사 추가 가능
</script>

<style scoped></style>
