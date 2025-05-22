<template>
  <div class="flex gap-6 h-full">
    <!-- 왼쪽: 아파트 리스트 -->
    <div class="w-2/3">
      <!-- 실거래가 예측 안내 -->
      <section class="bg-blue-50 border-l-4 border-blue-400 text-blue-700 px-6 py-4 rounded mb-6">
        매매 실거래가 예측 정보를 확인하실 수 있습니다.
      </section>

      <!-- 매물 리스트 -->
      <section class="h-[600px] overflow-y-auto pr-2 space-y-4">
        <div
          v-for="apt in aptList"
          :key="apt.aptSeq"
          class="bg-white border rounded-lg p-4 shadow-sm"
        >
          <div class="flex justify-between items-center mb-2">
            <div class="font-semibold text-lg text-gray-800">{{ apt.aptName + " "+ dealMap[apt.aptSeq][0].aptDong}}</div>
          </div>
          <!-- <div class="text-sm text-gray-600 mb-1"></div> -->

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

    <!-- 오른쪽: 그래프 표시 -->
    <div class="w-1/3 bg-white border rounded-lg p-4 shadow-sm h-fit sticky top-20">
      <h2 class="text-lg font-semibold mb-4">2026년 평당 시세 예측 그래프</h2>
      <div class="h-80 bg-gray-100 flex items-center justify-center text-gray-400">
        그래프 영역
      </div>
    </div>
  </div>
</template>



<script setup>
import { defineProps } from 'vue'

defineProps({
  aptList: Array,
  dealMap: Object
})
</script>

<style lang="scss" scoped>

</style>