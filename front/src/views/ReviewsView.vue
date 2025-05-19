<template>
  <div class="max-w-6xl mx-auto p-6">
    <!-- 제목과 글쓰기 버튼 -->
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-800">후기 게시판</h1>
      <button class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded font-medium">
        후기 작성
      </button>
    </div>

    <!-- 후기 리스트 -->
    <div class="space-y-4">
      <!-- 후기 1 -->
      <div class="p-4 border rounded shadow-sm bg-white">
        <div class="flex justify-between items-center mb-2">
          <span class="font-semibold text-gray-700">홍길동</span>
          <span class="text-sm text-gray-400">2025-05-19</span>
        </div>
        <p class="text-gray-800">
          이 사이트 덕분에 전세 사기 안 당하고 좋은 집 구했어요! 정말 감사합니다 👍
        </p>
      </div>

      <!-- 후기 2 -->
      <div class="p-4 border rounded shadow-sm bg-white">
        <div class="flex justify-between items-center mb-2">
          <span class="font-semibold text-gray-700">김부동</span>
          <span class="text-sm text-gray-400">2025-05-17</span>
        </div>
        <p class="text-gray-800">주변 시세보다 저렴한 매물을 찾을 수 있어서 만족스러웠습니다.</p>
      </div>

      <!-- 후기 3 -->
      <div class="p-4 border rounded shadow-sm bg-white">
        <div class="flex justify-between items-center mb-2">
          <span class="font-semibold text-gray-700">이후기</span>
          <span class="text-sm text-gray-400">2025-05-14</span>
        </div>
        <p class="text-gray-800">사용자 인터페이스가 직관적이고 검색이 쉬워요. 추천합니다!</p>
      </div>
    </div>
  </div>
</template>

<script setup>
// 향후: 후기 목록 API로 받아오기 / 작성 페이지로 라우팅
</script>

<style scoped></style>
