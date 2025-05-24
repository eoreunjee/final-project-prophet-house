<template>
  <!-- <main class="bg-gray-50"> -->
    <!-- Hero Section -->
    <section class="relative h-[500px] flex items-center justify-center mb-16 overflow-hidden">
      <!-- 배경 이미지 (object-cover로 비율 유지) -->
      <img
        src="../assets/main_apartment.png"
        alt="Hero Background"
        class="absolute inset-0 w-full h-full object-cover object-top"
      />
      <!-- 어두운 오버레이(가독성 향상용, 선택사항) -->
      <div class="absolute inset-0 bg-black bg-opacity-40"></div>
      <!-- 텍스트 및 버튼 (z-10으로 이미지 위에) -->
      <div class="relative z-10 text-center max-w-3xl mx-auto">
        <h1 class="text-3xl md:text-4xl font-bold text-white mb-4">
          최적의 주거 공간을 찾는 여정을 시작하세요
        </h1>
        <p class="text-white text-base md:text-lg mb-6">
          믿을 수 있는 정보와 함께 당신에게 맞는 집을 찾아보세요. 실제 거래 정보와 전세사기 발생 지역 확인,
          그리고 다양한 정보를 제공합니다.
        </p>
      </div>
      
    </section>


    <!-- 서비스 소개 Section -->
    <section class="max-w-5xl mx-auto mb-20">
      <h2 class="text-center text-2xl font-bold text-gray-800 mb-10">
        믿을 수 있는 부동산 정보 서비스
      </h2>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div class="p-6 bg-white rounded shadow border text-center">
          <div class="text-blue-600 text-3xl mb-2">🏠</div>
          <h3 class="font-semibold mb-1">정확한 매물 정보</h3>
          <p class="text-sm text-gray-600">
            국토교통부에서 제공하는 실제로 거래된 매물 정보 검색 서비스를 제공합니다.
          </p>
        </div>
        <div class="p-6 bg-white rounded shadow border text-center">
          <div class="text-blue-600 text-3xl mb-2">📊</div>
          <h3 class="font-semibold mb-1">시세 예측 및 분석</h3>
          <p class="text-sm text-gray-600">
            검색 지역의 시세와 AI를 활용한 1년 뒤의 미래 시세 예측 서비스를 제공합니다.
          </p>
        </div>
        <div class="p-6 bg-white rounded shadow border text-center">
          <div class="text-blue-600 text-3xl mb-2">🛡️</div>
          <h3 class="font-semibold mb-1">안전한 거래 정보</h3>
          <p class="text-sm text-gray-600">
            지역별 전세사기 피해 발생 건수 시각화를 통해 안전한 거래를 돕습니다.
          </p>
        </div>
      </div>
    </section>

    <!-- 뉴스 영역 -->
    <NewsSection />
  <!-- </main> -->
</template>

<script setup>
import NewsSection from '@/components/NewsSection.vue'
</script>

<style scoped></style>
