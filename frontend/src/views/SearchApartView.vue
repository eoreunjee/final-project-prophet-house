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
        />

        <!-- <template v-if="showExperienceOverlay">
          <KakaoMapCustomOverlay
            v-for="(data, region) in experienceData"
            :key="region"
            :lat="regionCoords[region].lat"
            :lng="regionCoords[region].lng"
          >
            <div
              class="rounded-full text-white text-xs flex items-center justify-center"
              :style="{
                width: experienceToRadius(data) + 'px',
                height: experienceToRadius(data) + 'px',
                backgroundColor: experienceToColor(data),
                opacity: 0.6,
                transform: 'translate(-50%, -50%)',
                position: 'absolute',
                border: '1px solid #222'
              }"
            >
              {{ region }}
            </div>
          </KakaoMapCustomOverlay>
        </template> -->
      </KakaoMap>

      <!-- <button @click="showExperienceOverlay = !showExperienceOverlay"
              class="absolute top-4 right-4 z-15 bg-white border px-3 py-2 rounded text-sm shadow">
        전세사기 경험률 {{ showExperienceOverlay ? '숨기기' : '보기' }}
      </button> -->


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
              <option v-for="dong in dongList" :key="dong.dongCode" :value="dong.dongCode">{{ dong.dongName }}</option>
            </select>
            <button @click="searchApt" :disabled="!isSearchEnabled" class="bg-blue-600 text-white py-2 rounded hover:bg-blue-700">검색</button>
          </div>
        </div>
        <!-- 로딩 화면 -->
        <div v-if="isSearchingApt" class="flex justify-center items-center py-8">
          <svg class="animate-spin h-6 w-6 text-blue-500" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"></path>
          </svg>
          <span class="ml-3 text-sm text-gray-500">검색 중입니다...</span>
        </div>
        <div v-else class="flex-1 overflow-y-auto px-3 pb-6">
          <RealpricePrediction
            :apt-list="aptSearchList"
            @select-apt="handleSelectApt"
            @load-more="handleLoadMore"
            />
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
        <div>
          <span class="text-2xl font-bold">{{ selectedApt.aptName }}</span>
          <p class="text-xs text-gray-600">
            {{ selectedApt.sidoName + ' ' +
              selectedApt.gugunName + ' ' +
              selectedApt.dongName + ' ' + 
              selectedApt.roadNm + ' ' + selectedApt.roadNmBonbun
            }}
            {{ selectedApt.roadNmBubun === '0' ? '' : '-' + selectedApt.roadNmBubun }}
            {{ selectedApt.dongCode }}
          </p>
        </div>
        <button @click="selectedApt = false" class="absolute right-3 top-2 rounded px-1 text-lg text-gray-400 hover:bg-gray-100">×</button>
        
        <!-- TODO ---------------예측 그래프 ------------------------>
        <h2 class="text-lg font-bold text-transparent bg-clip-text bg-gradient-to-r from-sky-400 to-purple-700">
          2026년 {{ selectedApt.dongName }} m²당 AI 시세 예측
        </h2>

        <div v-if="!isLoggedIn" class="bg-gray-200 h-[260px] flex items-center justify-center shadow-lg rounded shrink-0">
          <p class="text-gray-500 text-center">로그인이 필요합니다</p>
        </div>

        <!-- 예측 로딩 중 -->
        <div v-else-if="isLoadingPrediction" class="flex justify-center items-center h-[260px] bg-gray-100 rounded shadow">
          <svg class="animate-spin h-6 w-6 text-blue-500" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"></path>
          </svg>
          <span class="ml-3 text-sm text-gray-600">예측 결과 불러오는 중...</span>
        </div>
        
        <!-- 예측 실패 시 -->
        <div v-else-if="predictionFailed" class="bg-gray-100 h-[260px] flex items-center justify-center shadow-lg rounded shrink-0">
          <p class="text-gray-600 text-center">⚠️ 데이터 준비 중입니다.</p>
        </div>

        <!-- 예측 완료 시 -->
        <div v-else-if="isLoggedIn && years && avgPrices && isPredicted" class="bg-white-100 h-[260px] items-center justify-center shadow-lg rounded shrink-0">
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
                  <th class="px-4 py-2 border-b w-[15%] whitespace-nowrap">거래날짜</th>
                  <th class="px-4 py-2 border-b w-[15%] whitespace-nowrap">층수 · 전용면적</th>
                  <th class="px-4 py-2 border-b w-[15%] text-right whitespace-nowrap">거래금액</th>
                  <!-- <th class="px-2 py-2 border-b w-[5%] text-center whitespace-nowrap">찜</th> -->
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
                  <!-- <td class="px-2 py-2 border-b text-center">
                    <button
                      @click="toggleFavorite(deal)"
                      class="text-gray-400 hover:text-red-500 transition-colors duration-200"
                      title="관심 매물 추가"
                    >
                      ♥
                    </button>
                  </td> -->
                </tr>
              </tbody>
            </table>

            <!-- 페이지네이션 -->
            <div class="h-12 flex justify-center items-center gap-1 bg-white text-sm">
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
import { userId } from '@/utils/auth'

useKakao(import.meta.env.VITE_KAKAO_MAP_API_KEY)

const coordinate = reactive({ lat: 37.566826, lng: 126.9786567 })
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
const currentPage = ref(1)
const pageSize = 8
const maxVisibleButtons = 5
const isSearchingApt = ref(false)
const isLoadingPrediction = ref(false)

const predictionFailed = ref(false)

// 아파트 목록 관련
const aptSearchList = ref([])
const totalAptCount = ref(0)
const aptSearchPage = ref(1)
const aptPageSize = 20

// 선택된 아파트의 거래내역
const selectedAptDeals = ref([])

// 로그인 확인
const isLoggedIn = ref(!!userId.value)

const validAptList = computed(() =>
  aptSearchList.value.filter(
    apt => apt.latitude && apt.longitude && !isNaN(parseFloat(apt.latitude)) && !isNaN(parseFloat(apt.longitude))
  )
)

watch(() => showSearch.value[0], (isOpen) => {
  if (!isOpen) {
    selectedApt.value = null
  }
})

const handleLoadMore = async () => {
  aptSearchPage.value += 1
  await fetchAptSearchList() // 기존에 push로 붙이도록 되어 있음
}

const isSearchEnabled = computed(() => {
  return aptName.value.trim() !== '' || selectedDong.value !== '';
});

async function fetchAptSearchList(reset = false) {
  const response = await axios.get('http://192.168.205.75:8080/api/search/apt', {
    params: {
      dongCode: selectedDong.value || null,
      aptName: aptName.value.trim() || null,
      page: aptSearchPage.value,
      size: aptPageSize
    }
  })

  if (reset) {
    aptSearchList.value = response.data.aptList
  } else {
    aptSearchList.value.push(...response.data.aptList)
  }
  totalAptCount.value = response.data.totalCount
}

const handleSelectApt = async (apt) => {
  selectedApt.value = apt
  currentPage.value = 1
  coordinate.lat = parseFloat(apt.latitude)
  coordinate.lng = parseFloat(apt.longitude)

  // regionDongName 구성
  let dongCodePart = selectedDong.value ? selectedDong.value.slice(0, 5) : apt.dongCode?.slice(0, 5)
  let dongNamePart = apt.dongName?.trim()

  if (!dongCodePart || !dongNamePart) {
    console.error('🚫 regionDongName 생성 실패: 동코드 또는 동이름 누락')
    return
  }

  regionDongName.value = `${dongCodePart}_${dongNamePart}`
  await fetchSelectedAptDeals(apt.aptSeq)

  // ✅ 이름으로만 검색한 경우에만 예측 다시 호출
  if (!selectedDong.value) {
    isLoadingPrediction.value = true
    try {
      console.log("🔍 호출 전 regionDongName:", regionDongName.value)
      await getPrediction()
    } catch (error) {
      console.error('예측 요청 실패:', error)
    } finally {
      isLoadingPrediction.value = false
    }
  }
}


// 아파트 검색 함수
async function searchApt() {
  console.log("📦 searchApt() 호출됨")
  isSearchingApt.value = true
  try {
    aptSearchPage.value = 1
    await fetchAptSearchList(true)
    selectedApt.value = null

  } catch (error) {
    console.error('검색 실패:', error)
  } finally {
    isSearchingApt.value = false
  }
}

// ✅ 동으로 검색된 경우 자동 예측 (아파트 리스트가 준비되면 실행)
watch(aptSearchList, async (newList) => {
  if (selectedDong.value && newList.length > 0) {
    const apt = newList[0]
    selectedApt.value = apt
    regionDongName.value = `${selectedDong.value.slice(0, 5)}_${apt.dongName}`

    await fetchSelectedAptDeals(apt.aptSeq)
    try {
      isLoadingPrediction.value = true
      await getPrediction()
      await getPredictionBar()
    } catch (error) {
      console.error('예측 요청 실패:', error)
    } finally {
      isLoadingPrediction.value = false
    }
  }
})


const fetchSelectedAptDeals = async (aptSeq) => {
  try {
    const response = await axios.get('http://192.168.205.75:8080/api/search/deals', {
      params: { aptSeq }
    })
    selectedAptDeals.value = response.data
  } catch (e) {
    console.error('거래내역 불러오기 실패:', e)
    selectedAptDeals.value = []
  }
}


const loadSido = async () => {
  const res = await axios.get('http://192.168.205.75:8080/api/search/sido')
  sidoList.value = res.data
}
const loadGugun = async () => {
  const res = await axios.get('http://192.168.205.75:8080/api/search/gugun', { params: { sidoName: selectedSido.value } })
  gugunList.value = res.data
  selectedGugun.value = ''
  selectedDong.value = ''
}
const loadDong = async () => {
  const res = await axios.get('http://192.168.205.75:8080/api/search/dong', { params: { gugunName: selectedGugun.value } })
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

const sortedDeals = computed(() => {
  return [...selectedAptDeals.value].sort((a, b) => {
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
    data: avgPrices.value,
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
  predictionFailed.value = false; // 초기화

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
    predictionFailed.value = true; // 실패 상태로 표시
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


  const rawYears = response.data.years;
  const rawAvgPrices = response.data.avgPrices;
  const rawIsPredicted = response.data.isPredicted;

  console.log("🔥 years:", rawYears);
  console.log("🔥 avgPrices:", rawAvgPrices);
  console.log("🔥 isPredicted:", rawIsPredicted);

  // ✅ 숫자만 필터링해서 할당
  years.value = Array.isArray(rawYears) ? rawYears.filter(y => typeof y === 'number') : [];
  avgPrices.value = Array.isArray(rawAvgPrices) ? rawAvgPrices.filter(p => typeof p === 'number') : [];
  isPredicted.value = Array.isArray(rawIsPredicted) ? rawIsPredicted : [];

  predictedPrice.value = response.data.predictedPrice;
  accuracy.value = response.data.accuracy;

}

/** ---------------------------- 예측 그래프 END -----------------------------*/

loadSido()
</script>

<style scoped>
.shadow-left {
  box-shadow: -2px 0 4px rgba(0, 0, 0, 0.1);
}
</style>
