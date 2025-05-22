<template>
  <div class="flex gap-6 h-full">

    <!-- 매물 리스트 -->
    <section class="overflow-y-auto pr-2 space-y-3 custom-scroll">
      <div
        v-for="apt in aptList"
        :key="apt.aptSeq"
        class="bg-white border rounded-lg p-4 shadow-sm"
      >
        <div class="flex justify-between items-center mb-3">
          <div class="font-semibold text-lg text-gray-800">{{ apt.aptName + " "+ dealMap[apt.aptSeq][0].aptDong}}</div>
        </div>

        <div v-if="dealMap[apt.aptSeq] && dealMap[apt.aptSeq].length" class="text-sm text-gray-600">
          {{ dealMap[apt.aptSeq][0].floor }}층 / 전용 면적: {{ dealMap[apt.aptSeq][0].excluUseAr }}㎡ / 
          거래일자: {{ dealMap[apt.aptSeq][0].dealYear }}.{{ dealMap[apt.aptSeq][0].dealMonth }}.{{ dealMap[apt.aptSeq][0].dealDay }} /
          가격: {{ dealMap[apt.aptSeq][0].dealAmount }}만원
        </div>
        <div v-else class="text-sm text-gray-400">거래 정보 없음</div>

        <!-- 미래 시세 (예시용) -->
        <div class="text-sm font-medium text-blue-600 mt-2">예상 미래 시세: 분석 예정</div>
      </div>
    </section>

  </div>
</template>



<script setup>
import { defineProps } from 'vue'

defineProps({
  aptList: Array,
  dealMap: Object
})
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
