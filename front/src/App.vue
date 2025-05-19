<template>
  <div id="app" class="min-h-screen flex flex-col">
    <!-- Header -->
    <header class="bg-white px-6 py-6 shadow">
      <div class="max-w-full mx-auto w-full flex justify-between items-center font-bold">
        <!-- 왼쪽: 로고 + 메뉴 -->
        <div class="flex items-center space-x-8">
          <h1 class="text-xl text-[#4DA1F5] font-bold whitespace-nowrap">
            <router-link to="/">Prophet House</router-link>
          </h1>
          <nav class="flex space-x-6 text-sm whitespace-nowrap">
            <router-link
              to="/searchApart"
              class="text-gray-800 hover:text-[#4DA1F5] hover:no-underline no-underline"
            >지역별 매물 검색</router-link>
            <router-link
              to="/reviews"
              class="text-gray-800 hover:text-[#4DA1F5] hover:no-underline no-underline"
            >후기 게시판</router-link>
          </nav>
        </div>

        <!-- 오른쪽: 로그인/회원가입 -->
        <div class="flex items-center space-x-4 text-sm">
          <router-link
            to="/login"
            class="text-gray-800 hover:text-[#4DA1F5] hover:no-underline no-underline"
          >로그인</router-link>
          <router-link
            to="/signup"
            class="text-gray-800 hover:text-[#4DA1F5] hover:no-underline no-underline"
          >회원가입</router-link>
        </div>
      </div>
    </header>


    <!-- Main Content -->
    <main class="flex-1">
      <router-view />
    </main>

    <!-- Footer -->
    <footer class="bg-gray-100 text-center py-4 text-sm text-gray-600">
      © 2025 부동산 플랫폼. All rights reserved.
    </footer>
  </div>
</template>

<script setup>
// 특별한 로직 없음
</script>

<style scoped>
/* 필요 시 추가 스타일 */
</style>
