<template>
  <div class="max-w-7xl mx-auto px-4 py-10">
    <!-- 제목 -->
    <h1 class="text-2xl font-bold text-gray-800 mb-6">지역별 매물 검색</h1>

    <!-- 검색 옵션 박스 -->
    <section class="bg-white border rounded-lg p-6 shadow mb-8">
      <h2 class="text-lg font-semibold mb-4 text-gray-700">검색 옵션</h2>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4 items-end">
        <!-- 거래유형 -->
        <div>
          <label class="block text-sm text-gray-600 mb-1">거래 유형</label>
          <div class="flex gap-2">
            <button class="px-4 py-1 rounded border border-gray-300 text-sm">매매</button>
            <button class="px-4 py-1 rounded border border-gray-300 text-sm">전세</button>
            <button class="px-4 py-1 rounded border border-gray-300 text-sm">월세</button>
          </div>
        </div>

        <div class="flex gap-4 flex-nowrap">
          <!-- 시/도 -->
          <div class="w-[160px]">
            <label class="block text-sm text-gray-600 mb-1">시/도</label>
            <select class="w-full border px-3 py-2 rounded text-sm">
              <option>시/도 선택</option>
            </select>
          </div>

          <!-- 군/구 -->
          <div class="w-[180px]">
            <label class="block text-sm text-gray-600 mb-1">군/구</label>
            <select class="w-full border px-3 py-2 rounded text-sm">
              <option>군/구 선택</option>
            </select>
          </div>

          <!-- 동 -->
          <div class="w-[180px]">
            <label class="block text-sm text-gray-600 mb-1">동</label>
            <select class="w-full border px-3 py-2 rounded text-sm">
              <option>동 선택</option>
            </select>
          </div>
        </div>
      </div>

      <!-- 지도 숨기기 버튼 -->
      <div class="mt-6">
        <button class="text-sm text-blue-600 hover:underline">지도 숨기기</button>
      </div>
    </section>
    <section>
      <KakaoMap
        :lat="coordinate.lat"
        :lng="coordinate.lng"
        :draggable="true"
        width="100%"
        class="px-6 py-4 rounded mb-6">
        <KakaoMapMarker :lat="coordinate.lat" :lng="coordinate.lng"></KakaoMapMarker>
      </KakaoMap>
    </section>
    <!-- 지도 자리 -->
    <!-- <section class="mb-10 h-[500px] bg-gray-600">
      
    </section> -->

    <!-- 실거래가 예측 안내 -->
    <section class="bg-blue-50 border-l-4 border-blue-400 text-blue-700 px-6 py-4 rounded mb-6">
      미래 실거래가 예측 정보를 확인하실 수 있습니다.
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
  </div>
</template>

<script setup>
import { useKakao } from 'vue3-kakao-maps/@utils';
import { KakaoMap,KakaoMapMarker } from 'vue3-kakao-maps';

useKakao(import.meta.env.VITE_KAKAO_MAP_API_KEY);

const coordinate = {
  lat: 37.566826,
  lng: 126.9786567
};
</script>

<style scoped></style>
