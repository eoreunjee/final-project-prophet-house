<template>
  <div class="flex gap-6 h-full">
    <!-- 매물 리스트 -->
    <section class="overflow-y-auto pr-2 space-y-3 w-[400px] custom-scroll">
      <template v-if="aptList.length > 0">
        <div
          v-for="apt in displayedAptList"
          :key="apt.aptSeq"
          class="bg-white border rounded-lg p-4 shadow-sm cursor-pointer hover:bg-gray-50"
          @click="selectApt(apt)"
        >
          <div class="justify-between items-center">
            <div class="font-semibold text-lg text-gray-800 pb-2">
              {{ apt.aptName }}
            </div>
            <p class="text-xs text-gray-600">
              {{ apt.sidoName + ' ' + apt.gugunName + ' ' + apt.dongName }}
              {{ apt.roadNm + ' ' + apt.roadNmBonbun }}
              {{ apt.roadNmBubun === '0' ? '' : '-' + apt.roadNmBubun }}
            </p>
          </div>
          <!-- <div v-if="dealMap[apt.aptSeq]?.length" class="text-sm text-gray-600">
            <div class="text-sm font-medium text-blue-600 mt-2">
              평균 거래가: {{ getFormattedAverageAmount(apt.aptSeq) }}원
            </div>
          </div> -->
        </div>
        <!-- 👇 이 부분 추가 -->
        <div v-if="displayedAptList.length < aptList.length" class="text-center my-4">
          <button
            @click="loadMore"
            class="px-4 py-2 rounded hover:bg-gray-200"
          >
            ↓ 매물 더보기
          </button>
        </div>
      </template>

      <template v-else>
        <div class="text-center text-gray-400 text-base mt-20 px-4">
          <div class="inline-block px-4 py-3">
            <span class="font-semibold text-gray-600">검색된 매물이 없습니다.</span>
          </div>
        </div>
      </template>
    </section>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, ref, computed, watch } from 'vue'

const props = defineProps({
  aptList: Array
})

const emit = defineEmits(['select-apt', 'load-more'])

const loadMore = () => {
  displayedCount.value += 10
  emit('load-more')
}

const displayedCount = ref(10) // ⭐ 처음에 10개만 보여줌

const displayedAptList = computed(() => {
  return props.aptList.slice(0, displayedCount.value)
})

const selectApt = (apt) => {
  emit('select-apt', apt)
}

watch(() => props.aptList, () => {
  displayedCount.value = 10
})
</script>

<style scoped>
.custom-scroll::-webkit-scrollbar {
  width: 6px;
}
.custom-scroll::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scroll::-webkit-scrollbar-thumb {
  background-color: rgba(0, 0, 0, 0.2);
  border-radius: 3px;
}
</style>
