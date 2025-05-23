import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios';
import './assets/main.css'; // Tailwind 연결
import { setUsernameFromToken } from './utils/auth'
import '@/assets/fonts.css'
const app = createApp(App)

setUsernameFromToken();

axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, error => {
  return Promise.reject(error);
});

app.use(router)

app.mount('#app')
