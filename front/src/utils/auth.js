import { ref } from 'vue'

export const username = ref(null)

export function setUsernameFromToken() {
  const token = localStorage.getItem('token');
  if (!token) {
    username.value = null;
    return;
  }

  try {
    const base64Payload = token.split('.')[1];
    const bytes = Uint8Array.from(atob(base64Payload), c => c.charCodeAt(0));
    const payload = JSON.parse(new TextDecoder().decode(bytes));

    username.value = payload.name; // ✅ 한글 이름 정상 출력
  } catch (e) {
    console.error('JWT 파싱 실패:', e);
    username.value = null;
  }
}

