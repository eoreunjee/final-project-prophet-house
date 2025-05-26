<template>
  <LMap
    :zoom="7"
    :center="[36.5, 127.8]"
    style="height: 90vh; width: 100%;"
    :preferCanvas="false"
  >
    <LTileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
    <LGeoJson
      :geojson="geojson"
      :options-style="styleFeature"
      :options-onEachFeature="onEachFeature"
    />
  </LMap>
</template>

<script setup>
import { ref } from 'vue'
import { LMap, LTileLayer, LGeoJson } from '@vue-leaflet/vue-leaflet'
import 'leaflet/dist/leaflet.css'
import L from 'leaflet'
import geojsonData from '@/assets/skorea-provinces-geo.json'

// leaflet 기본 마커 아이콘 세팅
import markerIcon from 'leaflet/dist/images/marker-icon.png'
import markerShadow from 'leaflet/dist/images/marker-shadow.png'
L.Icon.Default.mergeOptions({
  iconUrl: markerIcon,
  shadowUrl: markerShadow,
})

// 피해 건수 데이터
const scamCounts = {
  "서울": 5543, "경기": 4400, "대전": 2763, "인천": 2738,
  "부산": 2246, "전남": 738, "대구": 453, "경북": 286,
  "경남": 266, "세종": 263, "광주": 255, "전북": 219,
  "충남": 212, "강원": 183, "충북": 177, "울산": 141,
  "제주": 66
}

// 영어 → 한글 지역명 맵핑
const nameMap = {
  "Seoul": "서울", "Gyeonggi-do": "경기", "Daejeon": "대전",
  "Incheon": "인천", "Busan": "부산", "Jeollanam-do": "전남",
  "Daegu": "대구", "Gyeongsangbuk-do": "경북", "Gyeongsangnam-do": "경남",
  "Sejong": "세종", "Gwangju": "광주", "Jeollabuk-do": "전북",
  "Chungcheongnam-do": "충남", "Gangwon-do": "강원",
  "Chungcheongbuk-do": "충북", "Ulsan": "울산", "Jeju": "제주"
}

// 색상 스케일
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

// 스타일 지정
function styleFeature(feature) {
  const engName = feature.properties.NAME_1
  const korName = nameMap[engName]
  const count = scamCounts[korName]
  return {
    fillColor: getColor(count ?? 0),
    weight: 1,
    opacity: 1,
    color: 'white',
    dashArray: '3',
    fillOpacity: 0.7
  }
}

// 툴팁 바인딩
function onEachFeature(feature, layer) {
  const engName = feature.properties.NAME_1
  const korName = nameMap[engName]
  const count = scamCounts[korName]
  const content = `<b>${korName}</b><br/>사기 피해 건수: ${count ?? '없음'}건`
  layer.bindTooltip(content, {
    sticky: true,
    direction: 'top',
    offset: [0, -10]
  })
}

const geojson = ref(geojsonData)
</script>

<style scoped>
/* 툴팁 명확히 보이도록 보강 */
.leaflet-tooltip {
  background-color: white;
  border: 1px solid #333;
  padding: 6px 8px;
  color: black;
  font-size: 13px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
}
</style>
