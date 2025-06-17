import { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../units/api';

function SocialSuccessPage() {
  const navigate = useNavigate();
  const [params] = useSearchParams();
  const { login } = useAuth();

  useEffect(() => {
    const token = params.get('token');

    if (!token) {
      alert('로그인 실패: 토큰 없음');
      navigate('/login');
      return;
    }

    // ✅ 토큰 저장
    login(token);
    localStorage.setItem('token', token);

    // ✅ userId 저장 요청
    api.get('/users/me/info', {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        const userId = res.data.userId;

        // ✅ 이거 반드시 실행되어야 함
        localStorage.setItem('userId', userId);

        console.log('[✅ 저장됨] userId:', userId);
        navigate('/');
      })
      .catch(() => {
        alert('유저 정보 불러오기 실패');
        navigate('/login');
      });
  }, []);

  return (
    <div className="flex items-center justify-center h-screen text-lg">
      🔐 로그인 처리 중입니다...
    </div>
  );
}

export default SocialSuccessPage;
