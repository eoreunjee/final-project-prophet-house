<template>
  <div class="max-w-7xl mx-auto px-4 py-10">
    <!-- 제목 -->
    <h1 class="text-2xl font-bold text-gray-800 mb-6">지역별 매물 검색</h1>

    <!-- 검색 옵션 박스 -->
    <section class="bg-white border rounded-lg p-6 shadow mb-8">
      <h2 class="text-lg font-semibold mb-4 text-gray-700">검색 옵션</h2>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4 items-end">
        <!-- 이름 검색 -->
        <div>
          <input
            type="text"
            class="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-1 focus:ring-blue-100"
            placeholder="건물 이름 검색">
        </div>

        <!-- 거래유형 -->
        <div>
          <label class="block text-sm text-gray-600 mb-1">거래 유형</label>
          <div class="flex gap-2">
            <button
              class="px-4 py-1 rounded border border-gray-300 text-sm"
              :class="selectedType === '매매' ? 'bg-blue-100 text-blue-600 border-blue-400' : ''"
              @click="selectedType = '매매'"
            >매매</button>
            <button
              class="px-4 py-1 rounded border border-gray-300 text-sm"
              :class="selectedType === '전세' ? 'bg-blue-100 text-blue-600 border-blue-400' : ''"
              @click="selectedType = '전세'"
            >전세</button>
            <button
              class="px-4 py-1 rounded border border-gray-300 text-sm"
              :class="selectedType === '월세' ? 'bg-blue-100 text-blue-600 border-blue-400' : ''"
              @click="selectedType = '월세'"
            >월세</button>
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
        <button
          class="text-sm text-blue-600 hover:underline"
          @click="showMap = !showMap"
        >
          {{ showMap ? '지도 숨기기' : '지도 보기' }}
        </button>
      </div>
    </section>

    <section v-show="showMap">
      <KakaoMap
        :lat="coordinate.lat"
        :lng="coordinate.lng"
        :draggable="true"
        width="100%"
        class="px-6 py-4 rounded mb-6 shadow mb-8">
        <KakaoMapMarker :lat="coordinate.lat" :lng="coordinate.lng"></KakaoMapMarker>
      </KakaoMap>
    </section>
    <!-- 지도 자리 -->
    <!-- <section class="mb-10 h-[500px] bg-gray-600">
      
    </section> -->
    <section v-if="selectedType === '매매'">
      <RealpricePrediction />
    </section>
    <section v-else-if="selectedType === '전세' || selectedType === '월세'">
      <JeonsaeSagi />
    </section>
    
  </div>
</template>

<script setup>
import { useKakao } from 'vue3-kakao-maps/@utils';
import { KakaoMap,KakaoMapMarker } from 'vue3-kakao-maps';
import { ref } from 'vue';
import RealpricePrediction from '@/components/RealpricePrediction.vue'
import JeonsaeSagi from '@/components/JeonsaeSagi.vue'

useKakao(import.meta.env.VITE_KAKAO_MAP_API_KEY);

const coordinate = {
  lat: 37.566826,
  lng: 126.9786567
};

const showMap = ref(true);
const selectedType = ref('매매');
</script>

<style scoped></style>
