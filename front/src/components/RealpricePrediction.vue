<template>
  <!-- 실거래가 예측 안내 -->
  <section class="bg-blue-50 border-l-4 border-blue-400 text-blue-700 px-6 py-4 rounded mb-6">
    매매 실거래가 예측 정보를 확인하실 수 있습니다.
  </section>

  <!-- 매물 카드 리스트 -->
  <section class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
    <div v-for="n in 6" :key="n" class="bg-white border rounded-lg p-5 shadow-sm">
      <div class="mb-2 font-semibold text-gray-800">강남 신축 아파트</div>
      <p class="text-sm text-gray-600 mb-1">서울특별시 강남구 테헤란로 123</p>
      <p class="text-sm text-gray-600">가격: 9억 8000만원</p>
      <p class="text-sm text-gray-600">면적: 84.12㎡ (25평)</p>
      <p class="text-sm text-gray-600">구조: 3룸 / 2욕실</p>
      <p class="text-sm text-gray-600">층수: 12층 / 총 20층</p>
      <p class="text-sm mt-2 font-medium text-blue-600">예상 미래 시세: 10억 5000만원</p>
      <div class="mt-4 flex justify-between items-center">
        <button class="text-sm text-blue-600 hover:underline">상세보기</button>
        <button class="px-3 py-1 bg-blue-100 text-blue-600 text-sm rounded">연락하기</button>
      </div>
    </div>
  </section>
</template>

<script setup>

</script>

<style lang="scss" scoped>

</style>