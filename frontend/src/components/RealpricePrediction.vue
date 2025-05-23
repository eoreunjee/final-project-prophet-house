<template>
  <div class="flex gap-6 h-full">
    <!-- 매물 리스트 -->
    <section class="overflow-y-auto pr-2 space-y-3 w-full custom-scroll">
      <div
        v-for="apt in aptList"
        :key="apt.aptSeq"
        class="bg-white border rounded-lg p-4 shadow-sm cursor-pointer hover:bg-gray-50"
        @click="moveToMarker(apt)"
      >
        <div class="flex justify-between items-center mb-3">
          <div class="font-semibold text-lg text-gray-800">
            {{ apt.aptName + " " + dealMap[apt.aptSeq][0].aptDong }}
          </div>
          <button
            @click.stop="openDealPopup(apt.aptSeq)"
            class="text-sm text-blue-600 hover:underline"
          >
            상세보기
          </button>
        </div>

        <div v-if="dealMap[apt.aptSeq] && dealMap[apt.aptSeq].length" class="text-sm text-gray-600">
          <div class="text-sm font-medium text-blue-600 mt-2">평균 거래가: {{ getFormattedAverageAmount(apt.aptSeq) }}</div>
        </div>
      </div>
    </section>

    <!-- 거래내역 팝업 -->
    <div
      v-if="showPopup"
      class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50 custom-scroll"
    >
      <div class="bg-white p-6 rounded shadow-md w-[520px] max-h-[80vh] overflow-y-auto relative">

        <!-- 헤더 -->
        <div class="sticky top-0 bg-white z-10 pb-3 mb-3 border-b">
          <div class="flex justify-between items-center mb-2">
            <h2 class="text-lg font-bold">거래 내역</h2>
            <button @click="closePopup" class="text-sm text-red-500 hover:underline">닫기</button>
          </div>

          <!-- 정렬 옵션 -->
          <div class="flex gap-2 text-sm">
            <label class="font-medium text-gray-700">정렬:</label>
            <select v-model="sortOption" class="border rounded px-2 py-1 text-sm">
              <option value="date_desc">날짜순 (최신)</option>
              <option value="date_asc">날짜순 (오래된)</option>
              <option value="price_desc">금액순 (높은)</option>
              <option value="price_asc">금액순 (낮은)</option>
            </select>
          </div>
        </div>

        <!-- 거래 목록 -->
        <div v-if="pagedDeals.length">
          <div
            v-for="(deal, idx) in pagedDeals"
            :key="idx"
            class="text-sm p-3 mb-2 border rounded bg-gray-50 shadow-sm"
          >
            <div class="flex justify-between font-medium text-gray-800">
              <span>{{ deal.dealYear }}.{{ deal.dealMonth }}.{{ deal.dealDay }}</span>
              <span>{{ formatNumberWithComma(parseInt(deal.dealAmount.replace(/,/g, ''))) }}원</span>
            </div>
            <div class="text-gray-600 text-sm mt-1">
              {{ deal.floor }}층 · {{ deal.excluUseAr }}㎡
            </div>
          </div>

          <!-- 페이지네이션 -->
          <div class="flex justify-center items-center gap-1 mt-4 text-sm">
          <!-- 이전 그룹 이동 -->
          <button
            v-if="pageGroup[0] > 1"
            @click="goToPage(pageGroup[0] - 1)"
            class="px-2 py-1 rounded bg-gray-200 hover:bg-gray-300"
          >
            ◀
          </button>

          <!-- 페이지 번호 버튼 -->
          <button
            v-for="page in pageGroup"
            :key="page"
            @click="goToPage(page)"
            :class="[
              'px-3 py-1 rounded',
              currentPage === page
                ? 'bg-blue-600 text-white'
                : 'bg-gray-100 text-gray-700 hover:bg-gray-300'
            ]"
          >
            {{ page }}
          </button>

          <!-- 다음 그룹 이동 -->
          <button
            v-if="pageGroup[pageGroup.length - 1] < totalPages"
            @click="goToPage(pageGroup[pageGroup.length - 1] + 1)"
            class="px-2 py-1 rounded bg-gray-200 hover:bg-gray-300"
          >
            ▶
          </button>
        </div>
        </div>

        <div v-else class="text-gray-400">거래 내역이 없습니다.</div>
      </div>
    </div>


  </div>
</template>



<script setup>
import { defineProps, ref, computed } from 'vue'

const props = defineProps({
  aptList: Array,
  dealMap: Object
})

const showPopup = ref(false)
const selectedDeals = ref([])
const sortOption = ref('date_desc') // 기본 정렬: 최신순


const sortedDeals = computed(() => {
  return [...selectedDeals.value].sort((a, b) => {
    const dateA = new Date(`${a.dealYear}-${a.dealMonth}-${a.dealDay}`)
    const dateB = new Date(`${b.dealYear}-${b.dealMonth}-${b.dealDay}`)
    const amountA = parseInt(a.dealAmount.replace(/,/g, ''))
    const amountB = parseInt(b.dealAmount.replace(/,/g, ''))

    switch (sortOption.value) {
      case 'date_asc':
        return dateA - dateB
      case 'date_desc':
        return dateB - dateA
      case 'price_asc':
        return amountA - amountB
      case 'price_desc':
        return amountB - amountA
      default:
        return 0
    }
  })
})

const formatNumberWithComma = (amount) => {
  if (typeof amount !== 'number' || isNaN(amount)) return ''
  amount*=10000
  return amount.toLocaleString('ko-KR')
}


const getFormattedAverageAmount = (aptSeq) => {
  const deals = props.dealMap[aptSeq]
  if (!deals || deals.length === 0) return ''

  const sum = deals.reduce((acc, deal) => {
    const amount = parseInt(deal.dealAmount.replace(/,/g, ''))
    return acc + amount
  }, 0)

  const avg = Math.round(sum / deals.length)
  return formatNumberWithComma(avg)
}




const openDealPopup = (aptSeq) => {
  const deals = props.dealMap[aptSeq] || []
  selectedDeals.value = deals
  showPopup.value = true
  currentPage.value = 1 // ✅ 페이지 초기화
}


const closePopup = () => {
  showPopup.value = false
  selectedDeals.value = []
}

const emit = defineEmits(['move-map'])

const moveToMarker = (apt) => {
  emit('move-map', {
    lat: parseFloat(apt.latitude),
    lng: parseFloat(apt.longitude)
  })
}

const currentPage = ref(1)
const pageSize = 10
const maxVisibleButtons = 5

// 거래내역 정렬된 결과 → 현재 페이지용으로 잘라냄
const pagedDeals = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return sortedDeals.value.slice(start, start + pageSize)
})

const totalPages = computed(() =>
  Math.ceil(sortedDeals.value.length / pageSize)
)

const pageGroup = computed(() => {
  const start = Math.floor((currentPage.value - 1) / maxVisibleButtons) * maxVisibleButtons + 1
  const end = Math.min(start + maxVisibleButtons - 1, totalPages.value)

  const pages = []
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  return pages
})

const goToPage = (page) => {
  if (page < 1 || page > totalPages.value) return
  currentPage.value = page
}

</script>

<style scoped>
/* Chrome, Edge, Safari */
.custom-scroll::-webkit-scrollbar {
  width: 6px; /* 스크롤바 너비 */
}

.custom-scroll::-webkit-scrollbar-track {
  background: transparent;
}

.custom-scroll::-webkit-scrollbar-thumb {
  background-color: rgba(0, 0, 0, 0.2); /* 스크롤바 색상 */
  border-radius: 3px;
}
</style>
