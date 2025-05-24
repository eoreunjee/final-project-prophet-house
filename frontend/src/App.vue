<template>
  <div id="app" class="min-h-screen flex flex-col">
    <!-- Header -->
    <TheHeader class="bg-white px-6 py-3 shadow-md z-20 fixed" />

    <!-- Main Content -->
    <main class="pt-16 flex-1">
      <router-view />
    </main>

    <!-- Footer -->
    <TheFooter v-if="!$route.meta.hideFooter" class="bg-gray-100 text-center py-4 text-sm text-gray-600" />
  </div>
</template>

<script setup>
import  TheHeader  from '@/components/TheHeader.vue'
import  TheFooter  from '@/components/TheFooter.vue'
</script>

<style scoped>
/* 필요 시 추가 스타일 */
</style>
