import { useNavigate } from 'react-router-dom';
import logo from '../assets/images/Qpick.svg';
import kakaoLogin from '../assets/images/kakaoLogin.png';
import naverLogin from '../assets/images/naverLogin.png';
import googleLogin from '../assets/images/google.png';

function LoginPage() {
  const navigate = useNavigate();

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-50">
      <div className="text-center w-full max-w-[420px] min-h-[550px] py-12 px-8 bg-white border border-gray-200 rounded-xl shadow">

        {/* Qpick 로고 */}
        <img
          src={logo}
          alt="Qpick 로고"
          onClick={() => navigate('/')}
          className="h-20 mt-6 mx-auto mb-20 cursor-pointer transition hover:scale-105"
        />


        {/* 로그인 버튼 그룹 - 별도 테두리 박스 */}
        <div className="border border-gray-200 rounded-lg p-4 flex flex-col gap-6">
          {/* 카카오 로그인 */}
          <a href="http://localhost:8080/oauth2/authorization/kakao">
            <img
              src={kakaoLogin}
              alt="카카오 로그인"
              className="w-full h-[58px] object-contain transition-transform duration-200 hover:scale-105"
            />
          </a>

          {/* 네이버 로그인 */}
          <a href="http://localhost:8080/oauth2/authorization/naver">
            <img
              src={naverLogin}
              alt="네이버 로그인"
              className="w-full h-[62px] object-contain transition-transform duration-200 hover:scale-105"
            />
          </a>

          {/* 구글 로그인 */}
          <a href="http://localhost:8080/oauth2/authorization/google">
            <img
              src={googleLogin}
              alt="구글 로그인"
              className="w-full h-[57px] object-contain transition-transform duration-200 hover:scale-105"
            />
          </a>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;
