<template>
  <div class="relative w-full h-[calc(100vh-80px)]"> <!-- 전체를 relative로 감싸기 -->

    <!-- 지도 전체 배경 -->
    <KakaoMap
      :lat="coordinate.lat"
      :lng="coordinate.lng"
      :draggable="true"
      width="100%"
      height="100%"
      class="w-full h-full absolute top-0 left-0 z-0">
      <KakaoMapMarker
        v-for="apt in aptList"
        :key="apt.aptSeq"
        :lat="parseFloat(apt.latitude)"
        :lng="parseFloat(apt.longitude)" />
    </KakaoMap>

    <!-- 왼쪽: 검색 사이드바 (지도 위에 뜸) -->
    <aside
      v-show="showSearch"
      class="absolute top-0 left-0 z-10 w-[400px] h-full bg-white border-r p-6 overflow-y-auto shadow-lg opacity-85">
      <h2 class="text-2xl font-bold mb-6">매매 실거래가 검색</h2>
      <div class="flex justify-end mb-4">
        <button @click="toggleSearch" class="text-sm text-blue-600 hover:underline">검색 닫기</button>
      </div>
      <div class="flex flex-col gap-4">
        <!-- 검색 폼들 그대로 -->
        <select v-model="selectedSido" @change="loadGugun" class="border px-3 py-2 rounded">
          <option value="">시/도</option>
          <option v-for="sido in sidoList" :key="sido" :value="sido">{{ sido }}</option>
        </select>

        <select v-model="selectedGugun" @change="loadDong" :disabled="!selectedSido" class="border px-3 py-2 rounded">
          <option value="">구/군</option>
          <option v-for="gugun in gugunList" :key="gugun" :value="gugun">{{ gugun }}</option>
        </select>

        <select v-model="selectedDong" :disabled="!selectedGugun" class="border px-3 py-2 rounded">
          <option value="">동 선택</option>
          <option v-for="dong in dongList" :key="dong" :value="dong">{{ dong }}</option>
        </select>

        <input v-model="aptName" placeholder="건물 이름 검색" class="border px-3 py-2 rounded" />
        <button @click="searchApt" :disabled="!isSearchEnabled" class="bg-blue-600 text-white py-2 rounded hover:bg-blue-700">
          검색
        </button>
      </div>
    </aside>

    <!-- 검색 열기 버튼 (닫혀 있을 때만 보임) -->
    <button
      v-show="!showSearch"
      @click="toggleSearch"
      class="absolute top-4 left-4 z-20 bg-white border px-3 py-1 rounded text-sm shadow">
      검색 열기
    </button>
  </div>
</template>



<script setup>
import { useKakao } from 'vue3-kakao-maps/@utils';
import { KakaoMap,KakaoMapMarker } from 'vue3-kakao-maps';
import { ref, computed, reactive } from 'vue';
import axios from 'axios'
import RealpricePrediction from '@/components/RealpricePrediction.vue'

useKakao(import.meta.env.VITE_KAKAO_MAP_API_KEY);

const coordinate = reactive({
  lat: 37.566826,
  lng: 126.9786567
});

const showSearch = ref(true);
const toggleSearch = () => {
  showSearch.value = !showSearch.value;
};

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
    const response = await axios.get('http://localhost:8080/api/search/apt', {
      params: {
        sido: selectedSido.value,
        gugun: selectedGugun.value,
        dong: selectedDong.value
      }
    })
    
    aptList.value = response.data.aptList
    dealMap.value = response.data.dealMap

    if (aptList.value.length > 0) {
      coordinate.lat = parseFloat(aptList.value[0].latitude)
      coordinate.lng = parseFloat(aptList.value[0].longitude)
    }
  } catch (error) {
    console.error('Error searching apartments:', error)
  }
}

loadSido()
</script>

<style scoped></style>
