<template>
  <main class="bg-gray-50 py-12 px-4">
    <!-- Hero Section -->
    <section class="relative h-80 flex items-center justify-center mb-16 rounded-lg overflow-hidden">
      <!-- 배경 이미지 (object-cover로 비율 유지) -->
      <img
        src="https://i.namu.wiki/i/BnK_E5QmDZZBbXqcmhe_2QowGD6omkPOxLVGsdK6TbMtyfsEIsvE9f__XKA8mQ1cClLBtTRNOdsBxFxNwziyuw.webp"
        alt="Hero Background"
        class="absolute inset-0 w-full h-full object-cover"
      />
      <!-- 어두운 오버레이(가독성 향상용, 선택사항) -->
      <div class="absolute inset-0 bg-black bg-opacity-40"></div>
      <!-- 텍스트 및 버튼 (z-10으로 이미지 위에) -->
      <div class="relative z-10 text-center max-w-3xl mx-auto">
        <h1 class="text-3xl md:text-4xl font-bold text-white mb-4">
          최적의 주거 공간을 찾는 여정을 시작하세요
        </h1>
        <p class="text-white text-base md:text-lg mb-6">
          믿을 수 있는 정보와 함께 당신에게 맞는 집을 찾아보세요. 실시간 시세와 전세사기 위험도 확인,
          그리고 다양한 매물 정보를 제공합니다.
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
            검증된 매물 정보와 매매, 전세 등 다양한 거래 유형별 검색 서비스를 제공합니다.
          </p>
        </div>
        <div class="p-6 bg-white rounded shadow border text-center">
          <div class="text-blue-600 text-3xl mb-2">📊</div>
          <h3 class="font-semibold mb-1">시세 예측 및 분석</h3>
          <p class="text-sm text-gray-600">
            미래 시세 예측 및 지역 가격 트렌드를 분석하여 투자 및 거주 판단에 도움을 드립니다.
          </p>
        </div>
        <div class="p-6 bg-white rounded shadow border text-center">
          <div class="text-blue-600 text-3xl mb-2">🛡️</div>
          <h3 class="font-semibold mb-1">안전한 거래 정보</h3>
          <p class="text-sm text-gray-600">
            전세사기 피해정보와 위험지역 알림을 통해 안전한 거래를 돕습니다.
          </p>
        </div>
      </div>
    </section>

    <!-- 뉴스 영역 -->
    <section class="max-w-6xl mx-auto mb-20">
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-xl font-bold text-gray-800">부동산 뉴스</h2>
        <router-link to="/news" class="text-blue-600 text-sm hover:underline"
          >전체 뉴스 보기</router-link
        >
      </div>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div v-for="n in 6" :key="n" class="p-4 bg-white rounded border shadow-sm">
          <h3 class="font-semibold text-gray-800 mb-1">뉴스 제목 {{ n }}</h3>
          <p class="text-sm text-gray-600 mb-2">
            이곳에 간단한 뉴스 요약이 들어갑니다. 실제 뉴스 데이터로 교체하세요.
          </p>
          <p class="text-xs text-gray-400">2025-05-19</p>
        </div>
      </div>
    </section>

  </main>
</template>

<script setup>
// 실제 뉴스 리스트는 props 또는 API로 대체 가능
</script>

<style scoped></style>
