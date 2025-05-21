<template>
  <div class="max-w-2xl mx-auto p-6">
    <h1 class="text-2xl font-bold text-gray-800 mb-6">후기 작성</h1>

    <form @submit.prevent="submitReview" class="bg-white border rounded shadow-sm p-6 space-y-5">
      <!-- 거래 유형 선택 -->
      <div>
        <label for="dealType" class="block text-sm font-medium text-gray-700 mb-1">거래 유형</label>
        <select v-model="form.dealType" id="dealType" required class="w-full border rounded px-3 py-2">
          <option value="">선택</option>
          <option value="매매">매매</option>
          <option value="전세">전세</option>
          <option value="월세">월세</option>
        </select>
      </div>

      <!-- 시도 / 구군 / 동 선택 -->
      <div>
        <label class="block mb-1">시도</label>
        <select v-model="selectedSido" @change="fetchGugun" class="w-full border rounded px-3 py-2">
          <option value="">선택</option>
          <option v-for="sido in sidoList" :key="sido" :value="sido">{{ sido }}</option>
        </select>
      </div>

      <div>
        <label class="block mb-1">구군</label>
        <select v-model="selectedGugun" @change="fetchDong" :disabled="!selectedSido" class="w-full border rounded px-3 py-2">
          <option value="">선택</option>
          <option v-for="gugun in gugunList" :key="gugun" :value="gugun">{{ gugun }}</option>
        </select>
      </div>

      <div>
        <label class="block mb-1">동</label>
        <select v-model="selectedDong" :disabled="!selectedGugun" class="w-full border rounded px-3 py-2">
          <option value="">선택</option>
          <option v-for="dong in dongList" :key="dong" :value="dong">{{ dong }}</option>
        </select>
      </div>

      <!-- 후기 내용 -->
      <div>
        <label for="content" class="block text-sm font-medium text-gray-700 mb-1">후기 내용</label>
        <textarea
          id="content"
          v-model="form.content"
          required
          rows="5"
          maxlength="500"
          placeholder="후기를 남겨주세요 (최대 500자)"
          class="w-full border rounded px-3 py-2 resize-none"
        ></textarea>
      </div>

      <!-- 버튼 -->
      <div class="flex justify-end gap-3">
        <button type="button" @click="cancel" class="px-4 py-2 border rounded">취소</button>
        <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded">등록</button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { userId } from '@/utils/auth'

const router = useRouter()

const form = ref({
  dealType: '',
  content: ''
})

const sidoList = ref([])
const gugunList = ref([])
const dongList = ref([])

const selectedSido = ref('')
const selectedGugun = ref('')
const selectedDong = ref('')

onMounted(fetchSido)

async function fetchSido() {
  const res = await axios.get('http://localhost:8080/api/search/sido')
  sidoList.value = res.data
}

async function fetchGugun() {
  selectedGugun.value = ''
  selectedDong.value = ''
  dongList.value = []
  const res = await axios.get('http://localhost:8080/api/search/gugun', {
    params: { sidoName: selectedSido.value }
  })
  gugunList.value = res.data
}

async function fetchDong() {
  selectedDong.value = ''
  const res = await axios.get('http://localhost:8080/api/search/dong', {
    params: { gugunName: selectedGugun.value }
  })
  dongList.value = res.data
}

async function submitReview() {
  const location = `${selectedSido.value} ${selectedGugun.value} ${selectedDong.value}`

  try {
    await axios.post('http://localhost:8080/api/reviews', {
      userId: userId.value,
      dealType: form.value.dealType,
      location,
      content: form.value.content
    })
    alert('후기가 등록되었습니다!')
    router.push('/reviews/list')
  } catch (err) {
    alert('후기 등록 중 오류 발생')
    console.error(err)
  }
}

function cancel() {
  router.back()
}
</script>
