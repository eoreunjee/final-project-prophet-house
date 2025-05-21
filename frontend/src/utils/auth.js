import { ref } from 'vue'

export const username = ref(null)
export const userId = ref(null) // ✅ 사용자 ID 저장용

export function setUsernameFromToken() {
  const token = localStorage.getItem('token');
  if (!token) {
    username.value = null;
    userId.value = null;
    return;
  }

  try {
    const base64Payload = token.split('.')[1];
    const bytes = Uint8Array.from(atob(base64Payload), c => c.charCodeAt(0));
    const payload = JSON.parse(new TextDecoder().decode(bytes));

    username.value = payload.name;     // ex) "홍길동"
    userId.value = payload.id || payload.sub; // ex) "ssafy123"
  } catch (e) {
    console.error('JWT 파싱 실패:', e);
    username.value = null;
    userId.value = null;
  }
}
