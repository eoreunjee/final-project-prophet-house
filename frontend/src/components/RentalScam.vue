<template>
  <div class="relative w-full h-full">
    <!-- 🧭 설명 토글 버튼 -->
    <button
      @click="showInfo = !showInfo"
      class="absolute top-4 right-4 z-50 flex items-center gap-2 bg-white hover:bg-gray-100 text-gray-700 px-3 py-1.5 border rounded-full shadow transition duration-200 text-sm"
    >
      <span v-if="!showInfo">ℹ️ 설명 보기</span>
      <span v-else>❌ 설명 닫기</span>
    </button>

    <!-- 📝 설명 박스 -->
    <transition name="fade">
      <div
        v-if="showInfo"
        class="w-[320px] absolute top-16 right-4 z-40 bg-white border border-gray-300 px-5 py-4 rounded-xl text-sm text-gray-800 shadow-xl leading-relaxed backdrop-blur-sm"
      >
        <h2 class="font-semibold text-base mb-2 text-gray-900">📌 데이터 출처 및 안내</h2>
        <p>
          본 지도는 국토교통부 전세사기피해지원위원회(2024년 10월 기준)와 통계청 인구주택총조사(2020년),
          국토연구원 「지도로 보는 전세사기피해 발생 현황」, 각 시·도 공공데이터포털(광주광역시, 울산광역시 등)에서
          제공한 자료를 바탕으로 제작되었습니다.
        </p>
      </div>
    </transition>

    <!-- 지도 -->
    <div id="map" class="z-10" style="height: 91vh; width: 100%;" ref="mapContainer"></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'
import geojsonData from '@/assets/skorea-provinces-geo.json'
import rentalScamData from '@/assets/rentalScamData.json'

const showInfo = ref(true)
const mapContainer = ref(null)

function getColor(count) {
  if (count > 5000) return '#800026'
  if (count > 2000) return '#BD0026'
  if (count > 1000) return '#E31A1C'
  if (count > 500) return '#FC4E2A'
  if (count > 200) return '#FD8D3C'
  if (count > 150) return '#FEB24C'
  if (count > 100) return '#FED976'
  return '#FFEDA0'
}

onMounted(() => {
  const map = L.map(mapContainer.value, {
    // zoomControl: false,
    // scrollWheelZoom: false,
    // doubleClickZoom: false,
    // touchZoom: false,
    // boxZoom: false,
    // dragging: false
  }).setView([36.5, 127.8], 7)

  // 배경 없이 지도만 보이게 하려면 타일 레이어 생략
  // L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map)

  // 범례
  const legend = L.control({ position: 'bottomright' })
  legend.onAdd = function () {
    const div = L.DomUtil.create('div', 'info legend')
    const grades = [0, 100, 150, 200, 500, 1000, 2000, 5000]
    const labels = []

    for (let i = 0; i < grades.length; i++) {
      const from = grades[i]
      const to = grades[i + 1]
      labels.push(
        `<i style="background:${getColor(from + 1)}"></i> ${from}${to ? '&ndash;' + to : '+'}`
      )
    }

    div.innerHTML = `<strong>피해 건수</strong><br>` + labels.join('<br>')
    return div
  }
  legend.addTo(map)

  // GeoJSON 경계 및 툴팁
  L.geoJSON(geojsonData, {
    style: (feature) => {
      const engName = feature.properties.NAME_1
      const korName = rentalScamData.nameMap[engName]
      const count = rentalScamData.scamCounts[korName] || 0
      return {
        fillColor: getColor(count),
        weight: 1,
        color: 'white',
        dashArray: '3',
        fillOpacity: 0.7
      }
    },
    onEachFeature: (feature, layer) => {
      const engName = feature.properties.NAME_1
      const korName = rentalScamData.nameMap[engName]
      const count = rentalScamData.scamCounts[korName]
      const content = `<b>${korName}</b><br/>전세사기 피해 건수: ${count ?? '없음'}건`
      layer.bindTooltip(content, {
        sticky: true,
        direction: 'top',
        offset: [0, -10]
      })
    }
  }).addTo(map)
})
</script>

<style scoped>
#map {
  background-color: #f4f4f4;
}

::v-deep(.info.legend) {
  background: white;
  padding: 10px;
  line-height: 18px;
  color: #333;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
  border-radius: 5px;
  font-size: 13px;
}
::v-deep(.info.legend i) {
  width: 18px;
  height: 18px;
  float: left;
  margin-right: 8px;
  opacity: 0.8;
}

/* 설명 fade 애니메이션 */
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
</style>
