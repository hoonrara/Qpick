import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
});

// ✅ 요청 시 토큰 자동 헤더 설정
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// ✅ 401 응답 시: 로그인 페이지가 아닐 때만 이동
api.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      const isOnLoginPage = window.location.pathname === '/login';
      if (!isOnLoginPage) {
        localStorage.clear();
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export default api;
