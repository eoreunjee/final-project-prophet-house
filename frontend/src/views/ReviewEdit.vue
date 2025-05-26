<template>
  <div class="max-w-3xl mx-auto p-6">
    <h2 class="text-2xl font-bold text-gray-800 mb-6">후기 수정</h2>

    <form @submit.prevent="submitEdit" class="space-y-4">
      <div class="flex gap-2">
        <select v-model="selectedSido" @change="onSidoChange" class="border px-2 py-1 rounded w-1/3">
            <option disabled value="">시도 선택</option>
            <option v-for="s in sidoList" :key="s" :value="s">{{ s }}</option>
        </select>

        <select v-model="selectedGugun" @change="onGugunChange" class="border px-2 py-1 rounded w-1/3">
            <option disabled value="">구군 선택</option>
            <option v-for="g in gugunList" :key="g" :value="g">{{ g }}</option>
        </select>

        <select v-model="selectedDong" class="border px-2 py-1 rounded w-1/3">
            <option disabled value="">동 선택</option>
            <option v-for="d in dongList" :key="d.dongCode" :value="d.dongName">{{ d.dongName }}</option>
        </select>
      </div>

      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">거래 유형</label>
        <select v-model="form.dealType" class="w-full border rounded px-3 py-2">
          <option value="매매">매매</option>
          <option value="전세">전세</option>
          <option value="월세">월세</option>
        </select>
      </div>

      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">후기 내용</label>
        <textarea v-model="form.content" rows="6" class="w-full border rounded px-3 py-2 resize-none"></textarea>
      </div>

      <div class="text-right">
        <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded">수정 완료</button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { userId } from '@/utils/auth'

const route = useRoute()
const router = useRouter()
const reviewId = route.params.id

const form = ref({
  location: '',
  dealType: '',
  content: '',
  userId: ''
})


const sidoList = ref([])
const gugunList = ref([])
const dongList = ref([])

const selectedSido = ref('')
const selectedGugun = ref('')
const selectedDong = ref('')

async function loadSido() {
  const res = await axios.get("http://192.168.205.75:8080/api/search/sido")
  sidoList.value = res.data
}

async function loadGugun(sido) {
  const res = await axios.get(`http://192.168.205.75:8080/api/search/gugun?sidoName=${sido}`)
  gugunList.value = res.data
}

async function loadDong(sido, gugun) {
  const res = await axios.get(`http://192.168.205.75:8080/api/search/dong?sidoName=${sido}&gugunName=${gugun}`)
  dongList.value = res.data
}


onMounted(async () => {
  const res = await axios.get(`http://192.168.205.75:8080/api/reviews/${reviewId}`)
  const data = res.data

  if (data.userId !== userId.value) {
    alert('수정 권한이 없습니다.')
    router.push(`/reviews/${reviewId}`)
    return
  }

  // 💥 주소 파싱
  const [sidoValue, gugunValue, dongValue] = data.location.split(' ')
  selectedSido.value = sidoValue
  selectedGugun.value = gugunValue
  selectedDong.value = dongValue

  await loadSido()
  await loadGugun(sidoValue)
  await loadDong(sidoValue, gugunValue)

  form.value = {
    location: data.location,
    dealType: data.dealType,
    content: data.content,
    userId: data.userId
  }
})

async function onSidoChange() {
  selectedGugun.value = ''
  selectedDong.value = ''
  gugunList.value = []
  dongList.value = []

  await loadGugun(selectedSido.value)
}

async function onGugunChange() {
  selectedDong.value = ''
  dongList.value = []

  await loadDong(selectedSido.value, selectedGugun.value)
}

async function submitEdit() {
  try {
    await axios.put(`http://192.168.205.75:8080/api/reviews/${reviewId}`, {
      location: `${selectedSido.value} ${selectedGugun.value} ${selectedDong.value}`,
      dealType: form.value.dealType,
      content: form.value.content
    })
    alert("수정이 완료되었습니다.")
    router.push(`/reviews/detail/${reviewId}`)
  } catch (err) {
    alert("수정 실패: " + err.response?.data || err.message)
  }
}
</script>

<style scoped></style>
