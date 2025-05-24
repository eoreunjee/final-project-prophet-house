<template>
  <div class="relative w-full h-[calc(100vh-64px)]">
    <!-- 지도 -->
    <div class="w-full h-full relative">
      <KakaoMap
        :lat="coordinate.lat"
        :lng="coordinate.lng"
        :draggable="true"
        width="100%"
        height="100%"
        class="w-full h-full absolute top-0 left-0 z-0"
      >
        <KakaoMapMarker
          v-for="apt in validAptList"
          :key="apt.aptSeq"
          :lat="parseFloat(apt.latitude)"
          :lng="parseFloat(apt.longitude)"
          :clickable="true"
          :infoWindow="{ content: apt.aptName, visible: visibleRef }"
          @onClickKakaoMapMarker="onClickKakaoMapMarker"
        />
      </KakaoMap>

      <!-- 검색 aside -->
      <aside v-show="showSearch[0]" class="absolute top-0 left-0 z-11 w-[400px] h-full bg-white shadow-right flex flex-col">
        <div class="p-7 bg-white shrink-0 pt-[55px]">
          <button @click="showSearch[0] = false; selectedApt.value = null" class="absolute right-3 top-2 z-10 rounded px-1 text-lg text-gray-400 hover:bg-gray-100">×</button>
          <div class="flex flex-col gap-2 mb-1">
            <input v-model="aptName" placeholder="🔍︎  아파트 이름 검색" class="border px-3 py-2 rounded-[9px] mb-6" />
            <p>* 지역 조건 설정</p>
            <select v-model="selectedSido" @change="loadGugun" class="border px-3 py-2 rounded-[9px]">
              <option value="">시/도</option>
              <option v-for="sido in sidoList" :key="sido" :value="sido">{{ sido }}</option>
            </select>
            <select v-model="selectedGugun" @change="loadDong" :disabled="!selectedSido" class="border px-3 py-2 rounded-[9px]">
              <option value="">구/군</option>
              <option v-for="gugun in gugunList" :key="gugun" :value="gugun">{{ gugun }}</option>
            </select>
            <select v-model="selectedDong" :disabled="!selectedGugun" class="border px-3 py-2 rounded-[9px] mb-7">
              <option value="">동 선택</option>
              <option v-for="dong in dongList" :key="dong" :value="dong">{{ dong }}</option>
            </select>
            <button @click="searchApt" :disabled="!isSearchEnabled" class="bg-blue-600 text-white py-2 rounded hover:bg-blue-700">검색</button>
          </div>
        </div>
        <div class="flex-1 overflow-y-auto px-3 pb-6">
          <RealpricePrediction :apt-list="aptList" :deal-map="dealMap" @select-apt="handleSelectApt" />
        </div>
        
      </aside>
      <button
        v-show="!showSearch[0]"
        @click="showSearch[0] = true"
        class="absolute top-4 left-4 z-15 bg-white border px-3 py-2 rounded text-sm shadow"
      >
        ≡
      </button>

      <!-- 거래내역 + 그래프 aside -->
      <aside v-if="selectedApt" class="absolute top-0 left-[400px] w-[400px] h-full bg-white shadow-lg z-10 flex flex-col p-5 gap-4">
        <p class="text-2xl font-bold">{{ selectedApt.aptName }}</p>
        <button @click="selectedApt = false" class="absolute right-3 top-2 rounded px-1 text-lg text-gray-400 hover:bg-gray-100">×</button>
        
        <!-- TODO ---------------예측 그래프 ------------------------>
        <h2 class="text-lg font-bold">2026년 {{ selectedDong }} m²당 시세 예측 그래프</h2>
        <div v-if="years && avgPrices && isPredicted" class="bg-white-100 h-[260px] items-center justify-center shadow-lg rounded shrink-0">
          <Line :data="chartData" :options="chartOptions" />
          <div class="mt-6 text-center text-base text-gray-700 font-medium">
            2026년 예상 가격(만원/m²): <span class="text-red-600 font-bold">{{ predictedPrice.toLocaleString() }}</span><br>
            예측치 정확도 : <span class="text-blue-600 font-bold">{{ accuracy }}%</span>
          </div>
        </div>
        <!---------------------- 예측 그래프 END -------------------->

        <div class="overflow-y-auto flex-1">
          <div class="flex justify-between items-center mb-1 mt-2">
            <h2 class="text-lg font-bold">거래내역</h2>
            <select v-model="sortOption" class="border rounded px-2 py-1 text-sm">
              <option value="date_desc">날짜순 (최신)</option>
              <option value="date_asc">날짜순 (오래된)</option>
              <option value="price_desc">금액순 (높은)</option>
              <option value="price_asc">금액순 (낮은)</option>
            </select>
          </div>
          <div class="flex gap-2 text-sm mb-3">
            
          </div>
          <div v-if="pagedDeals.length" class="overflow-x-auto border-gray-200 max-h-[50vh] overflow-auto">
            <!-- 테이블 -->
            <table class="min-w-full table-fixed text-xs text-left text-gray-800">
              <thead class="bg-gray-100 text-gray-700 font-semibold">
                <tr>
                  <th class="px-4 py-2 border-b w-[20%] whitespace-nowrap">거래날짜</th>
                  <th class="px-4 py-2 border-b w-[20%] whitespace-nowrap">층수 · 전용면적</th>
                  <th class="px-4 py-2 border-b w-[20%] text-right whitespace-nowrap">거래금액</th>
                  <th class="px-2 py-2 border-b w-[10%] text-center whitespace-nowrap">♥</th>
                </tr>
              </thead>
              <tbody>
                <tr
                  v-for="(deal, idx) in pagedDeals"
                  :key="idx"
                  class="h-10 hover:bg-gray-50"
                >
                  <td class="px-4 py-2 border-b whitespace-nowrap">
                    {{ deal.dealYear }}.{{ deal.dealMonth }}.{{ deal.dealDay }}
                  </td>
                  <td class="px-4 py-2 border-b whitespace-nowrap">
                    {{ deal.floor }}층 · {{ deal.excluUseAr }}㎡
                  </td>
                  <td class="px-4 py-2 border-b whitespace-nowrap text-right">
                    {{ formatNumberWithComma(parseInt(deal.dealAmount.replace(/,/g, ''))) }}원
                  </td>
                  <td class="px-2 py-2 border-b text-center">
                    <button
                      @click="toggleFavorite(deal)"
                      class="text-gray-400 hover:text-red-500 transition-colors duration-200"
                      title="관심 매물 추가"
                    >
                      ♥
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>

            <!-- 페이지네이션 -->
            <div class="h-16 flex justify-center items-center gap-1 bg-white text-sm border-t">
              <button
                v-if="pageGroup[0] > 1"
                @click="goToPage(pageGroup[0] - 1)"
                class="px-2 py-1 rounded bg-gray-200 hover:bg-gray-300"
              >이전</button>

              <button
                v-for="page in pageGroup"
                :key="page"
                @click="goToPage(page)"
                :class="[
                  'px-3 py-1 rounded',
                  currentPage === page ? 'bg-blue-600 text-white' : 'bg-gray-100 text-gray-700 hover:bg-gray-300'
                ]"
              >{{ page }}</button>

              <button
                v-if="pageGroup[pageGroup.length - 1] < totalPages"
                @click="goToPage(pageGroup[pageGroup.length - 1] + 1)"
                class="px-2 py-1 rounded bg-gray-200 hover:bg-gray-300"
              >다음</button>
            </div>
          </div>


          <div v-else class="text-gray-400">거래 내역이 없습니다.</div>
        </div>
      </aside>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import axios from 'axios'
import RealpricePrediction from '@/components/RealpricePrediction.vue'
import { KakaoMap, KakaoMapMarker } from 'vue3-kakao-maps'
import { useKakao } from 'vue3-kakao-maps/@utils'

useKakao(import.meta.env.VITE_KAKAO_MAP_API_KEY)

const coordinate = reactive({ lat: 37.566826, lng: 126.9786567 })
const visibleRef = ref(true)
const showSearch = ref([true, true])
const selectedApt = ref(null)
const sortOption = ref('date_desc')
const aptName = ref('')
const selectedSido = ref('')
const selectedGugun = ref('')
const selectedDong = ref('')
const sidoList = ref([])
const gugunList = ref([])
const dongList = ref([])
const aptList = ref([])
const dealMap = ref({})
const currentPage = ref(1)
const pageSize = 8
const maxVisibleButtons = 5

const handleSelectApt = (apt) => {
  selectedApt.value = apt
  currentPage.value = 1
  coordinate.lat = parseFloat(apt.latitude)
  coordinate.lng = parseFloat(apt.longitude)
}

const validAptList = computed(() =>
  aptList.value.filter(
    apt => apt.latitude && apt.longitude && !isNaN(parseFloat(apt.latitude)) && !isNaN(parseFloat(apt.longitude))
  )
)

watch(() => showSearch.value[0], (isOpen) => {
  if (!isOpen) {
    selectedApt.value = null
  }
})


const isSearchEnabled = computed(() => selectedSido.value && selectedGugun.value && selectedDong.value)

const searchApt = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/search/apt', {
      params: { sido: selectedSido.value, gugun: selectedGugun.value, dong: selectedDong.value }
    })
    aptList.value = response.data.aptList
    dealMap.value = response.data.dealMap
    selectedApt.value = null

    // 아파트 검색 후 예측도 같이 실행
    await getPrediction();
    await getPredictionBar();
  } catch (error) {
    console.error('Error searching apartments:', error)
  }
}

const loadSido = async () => {
  const res = await axios.get('http://localhost:8080/api/search/sido')
  sidoList.value = res.data
}
const loadGugun = async () => {
  const res = await axios.get('http://localhost:8080/api/search/gugun', { params: { sidoName: selectedSido.value } })
  gugunList.value = res.data
  selectedGugun.value = ''
  selectedDong.value = ''
}
const loadDong = async () => {
  const res = await axios.get('http://localhost:8080/api/search/dong', { params: { gugunName: selectedGugun.value } })
  dongList.value = res.data
  selectedDong.value = ''
}

watch(selectedSido, (newSido) => {
  if (!newSido) {
    selectedGugun.value = ''
    selectedDong.value = ''
    gugunList.value = []
    dongList.value = []
  }
})
watch(selectedGugun, (newGugun) => {
  if (!newGugun) {
    selectedDong.value = ''
    dongList.value = []
  }
})

const onClickKakaoMapMarker = () => {
  visibleRef.value = !visibleRef.value
}

const sortedDeals = computed(() => {
  const deals = dealMap.value[selectedApt.value?.aptSeq] || []
  return [...deals].sort((a, b) => {
    const dateA = new Date(`${a.dealYear}-${a.dealMonth}-${a.dealDay}`)
    const dateB = new Date(`${b.dealYear}-${b.dealMonth}-${b.dealDay}`)
    const amountA = parseInt(a.dealAmount.replace(/,/g, ''))
    const amountB = parseInt(b.dealAmount.replace(/,/g, ''))
    switch (sortOption.value) {
      case 'date_asc': return dateA - dateB
      case 'date_desc': return dateB - dateA
      case 'price_asc': return amountA - amountB
      case 'price_desc': return amountB - amountA
      default: return 0
    }
  })
})

const pagedDeals = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return sortedDeals.value.slice(start, start + pageSize)
})

const totalPages = computed(() => Math.ceil(sortedDeals.value.length / pageSize))

const pageGroup = computed(() => {
  const start = Math.floor((currentPage.value - 1) / maxVisibleButtons) * maxVisibleButtons + 1
  const end = Math.min(start + maxVisibleButtons - 1, totalPages.value)
  return Array.from({ length: end - start + 1 }, (_, i) => start + i)
})

const goToPage = (page) => {
  if (page < 1 || page > totalPages.value) return
  currentPage.value = page
}

const formatNumberWithComma = (amount) => {
  if (typeof amount !== 'number' || isNaN(amount)) return ''
  amount *= 10000
  return amount.toLocaleString('ko-KR')
}

/* TODO --------------------------예측 그래프------------------------------ */
import { Line } from 'vue-chartjs'
import { Chart, LineElement, PointElement, CategoryScale, LinearScale, Tooltip, Legend } from 'chart.js'
import { fetchPredictionBar } from '@/api/predict'
import { fetchPrediction } from '@/api/predict'

Chart.register(LineElement, PointElement, CategoryScale, LinearScale, Tooltip, Legend)

const loading = ref(false);
const error = ref(null);
const regionDongName = ref('');
const isPredicted = ref([])
const years = ref([])
const avgPrices = ref([])
const prediction = ref(null);
const predictedPrice = ref(0)
const accuracy = ref(0)

const lineColors = computed(() =>
  isPredicted.value.map(pred => pred ? 'rgba(255,99,132,1)' : 'rgba(54,162,235,1)')
)
const pointColors = computed(() =>
  isPredicted.value.map(pred => pred ? 'rgba(255,99,132,1)' : 'rgba(54,162,235,1)')
)

const chartData = computed(() => ({
  labels: years.value,
  datasets: [{
    label: '연평균 ㎡당 실거래가',
    data: avgPrices,
    borderColor: lineColors.value,
    backgroundColor: 'rgba(54,162,235,0.1)',
    pointBackgroundColor: pointColors.value,
    pointBorderColor: pointColors.value,
    pointRadius: 5,
    pointHoverRadius: 7,
    fill: false,
    tension: 0.1
  }]
}))

const chartOptions = {
  responsive: true,
  plugins: {
    legend: { display: false }
  }
}

// 예측 요청 시 regionDongName 사용
async function getPrediction() {
  loading.value = true;
  error.value = null;
  try {
    const response = await fetchPrediction({
      region_dong_name: regionDongName.value,
      target_year: 2026
    });
    console.log('regionDongName:', regionDongName.value);

    prediction.value = response.data;
    await getPredictionBar();
  }  catch (e) {
    // 여기!
    console.error('예측 에러:', e.response?.data?.detail || e.message);
    error.value = e.response?.data?.detail || e.message; // 필요하다면 에러 메시지 상태로 저장
  }  finally {
    loading.value = false;
  }
}


async function getPredictionBar() {
  const response = await fetchPredictionBar({
    region_dong_name: regionDongName.value,
    target_year: 2026
  })
  console.log('예측 API 응답:', response.data)
  years.value = response.data.years
  avgPrices.value = response.data.avgPrices
  isPredicted.value = response.data.isPredicted
  predictedPrice.value = response.data.predictedPrice
  accuracy.value = response.data.accuracy
}

/** ---------------------------- 예측 그래프 END -----------------------------*/

loadSido()
</script>

<style scoped>
.shadow-left {
  box-shadow: -2px 0 4px rgba(0, 0, 0, 0.1);
}
</style>
