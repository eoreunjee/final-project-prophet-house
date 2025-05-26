import { ref } from 'vue'

export const username = ref(null)
export const userId = ref(null) // ✅ 사용자 ID 저장용

export function setUsernameFromToken() {
  const token = localStorage.getItem('token')?.replace('Bearer ', '');
  if (!token || !token.includes('.') || token.split('.').length !== 3) {
    console.warn('❌ JWT 형식 아님:', token);
    return;
  }

  try {
    const base64Payload = token.split('.')[1];

    // base64url → base64 변환 후 디코딩
    const decoded = base64Payload
      .replace(/-/g, '+')
      .replace(/_/g, '/')
      .padEnd(base64Payload.length + (4 - base64Payload.length % 4) % 4, '=');

    const bytes = Uint8Array.from(atob(decoded), c => c.charCodeAt(0));
    const payload = JSON.parse(new TextDecoder().decode(bytes));

    username.value = payload.name;
    userId.value = payload.sub || payload.id;
  } catch (e) {
    console.error('JWT 파싱 실패:', e);
    username.value = null;
    userId.value = null;
  }
}
