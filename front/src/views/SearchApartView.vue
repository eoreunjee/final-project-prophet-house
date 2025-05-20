<template>
  <div class="max-w-7xl mx-auto px-4 py-10">
    <!-- 제목 -->
    <h1 class="text-2xl font-bold text-gray-800 mb-6">지역별 매물 검색</h1>

    <!-- 검색 옵션 박스 -->
    <section class="relative bg-white border rounded-lg p-6 shadow mb-8">
    <h2 class="text-lg font-semibold mb-4 text-gray-700">검색 옵션</h2>
    <div class="flex flex-wrap items-center gap-4">
      <!-- 거래 유형 -->
      <div class="flex items-center gap-1">
        <button
          v-for="type in ['매매', '전세', '월세']"
          :key="type"
          class="px-4 py-1 rounded border border-gray-300 text-sm transition"
          :class="selectedType === type ? 'bg-blue-100 text-blue-600 border-blue-400' : 'bg-white text-gray-700'"
          @click="selectedType = type"
        >{{ type }}</button>
      </div>


      <!-- 시/도 -->
      <div>
        <select 
          v-model="selectedSido" 
          @change="loadGugun"
          class="w-40 px-2 py-1 border border-gray-300 rounded text-sm"
        >
          <option value="">시/도</option>
          <option v-for="sido in sidoList" :key="sido" :value="sido">{{ sido }}</option>
        </select>
      </div>

      <!-- 군/구 -->
      <div>
        <select 
          v-model="selectedGugun" 
          @change="loadDong"
          :disabled="!selectedSido"
          class="w-40 px-2 py-1 border border-gray-300 rounded text-sm"
        >
          <option value="">구/군</option>
          <option v-for="gugun in gugunList" :key="gugun" :value="gugun">{{ gugun }}</option>
        </select>
      </div>

      <!-- 동 -->
      <div>
        <select 
          v-model="selectedDong" 
          :disabled="!selectedGugun"
          class="w-40 px-2 py-1 border border-gray-300 rounded text-sm"
        >
          <option value="">동 선택</option>
          <option v-for="dong in dongList" :key="dong" :value="dong">{{ dong }}</option>
        </select>
      </div>

      <!-- 건물 이름 검색 -->
      <div class="flex-1 min-w-[180px]">
        <input
          v-model="aptName"
          type="text"
          class="w-full px-4 py-2 border border-gray-300 rounded text-sm focus:outline-none focus:ring-1 focus:ring-blue-100"
          placeholder="건물 이름 검색"
        />
      </div>

      <!-- 검색 버튼 -->
      <button
        @click="searchApt"
        class="bg-[#338af3] hover:bg-[#2476c9] text-white font-semibold px-10 py-2 rounded transition absolute right-6 bottom-4"
        :disabled="!isSearchEnabled"
      >검색</button>
    </div>

    <!-- 지도 숨기기 버튼 -->
    <div class="mt-4">
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
import { ref, computed } from 'vue';
import axios from 'axios'
import RealpricePrediction from '@/components/RealpricePrediction.vue'
import JeonsaeSagi from '@/components/JeonsaeSagi.vue'

useKakao(import.meta.env.VITE_KAKAO_MAP_API_KEY);

const coordinate = {
  lat: 37.566826,
  lng: 126.9786567
};

const showMap = ref(true);
const selectedType = ref('매매');

// Reactive states
const selectedSido = ref('')
const selectedGugun = ref('')
const selectedDong = ref('')
const sidoList = ref([])
const gugunList = ref([])
const dongList = ref([])
const aptList = ref([])
const dealMap = ref({})

// Computed properties
const isSearchEnabled = computed(() => {
  return selectedSido.value && selectedGugun.value && selectedDong.value
})

// Methods
const loadSido = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/search/sido')
    sidoList.value = response.data
  } catch (error) {
    console.error('Error loading sido:', error)
  }
}

const loadGugun = async () => {
  if (!selectedSido.value) return
  try {
    const response = await axios.get('http://localhost:8080/api/search/gugun', {
      params: { sidoName: selectedSido.value }
    })
    gugunList.value = response.data
    selectedGugun.value = ''
    selectedDong.value = ''
  } catch (error) {
    console.error('Error loading gugun:', error)
  }
}

const loadDong = async () => {
  if (!selectedGugun.value) return
  try {
    const response = await axios.get('http://localhost:8080/api/search/dong', {
      params: { gugunName: selectedGugun.value }
    })
    dongList.value = response.data
    selectedDong.value = ''
  } catch (error) {
    console.error('Error loading dong:', error)
  }
}

const searchApt = async () => {
  try {
    const response = await axios.get('/api/search/apt', {
      params: {
        sido: selectedSido.value,
        gugun: selectedGugun.value,
        dong: selectedDong.value
      }
    })
    
    aptList.value = response.data.aptList
    dealMap.value = response.data.dealMap
  } catch (error) {
    console.error('Error searching apartments:', error)
  }
}

loadSido()
</script>

<style scoped></style>
