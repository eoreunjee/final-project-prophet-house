<template>
  <div class="flex justify-center items-center min-h-screen bg-gray-50 overflow-hidden">
    <fieldset class="w-full max-w-sm p-6 bg-white rounded-lg shadow-md border border-gray-200">
      <legend class="text-2xl black font-bold text-center mb-4">로그인</legend>
      <form class="space-y-4">
        <p>
          <input
            type="text"
            placeholder="아이디"
            class="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </p>
        <p>
          <input
            type="password"
            placeholder="비밀번호"
            class="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </p>
        <p>
          <button
            type="submit"
            class="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 rounded"
          >
            로그인
          </button>
        </p>
      </form>
    </fieldset>
  </div>
</template>

<script setup></script>

<style scoped></style>
