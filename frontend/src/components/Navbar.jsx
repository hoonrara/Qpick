import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import logo from '../assets/images/Qpick.svg';

function Navbar() {
  const { isLoggedIn, logout, user } = useAuth();

  const handleLogout = () => {
    logout();
    window.location.href = '/';
  };

  return (
    <nav className="flex justify-between p-4 bg-[#f9f6ff] text-white">
      <div className='p-4'>
        <Link to='/'>
          <img src={logo} alt="사이트 로고" className="h-10 w-auto" />
        </Link>
      </div>

      <div className="flex items-center gap-4 ml-auto pr-4">
        {isLoggedIn ? (
          <div className="flex items-center gap-3">
            {/* ✅ 퀴즈셋 생성 버튼 */}
            <Link to="/quizset/create">
              <button className="px-4 py-2 bg-green-600 text-white font-semibold rounded-lg hover:bg-green-700 shadow transition">
                퀴즈 만들기
              </button>
            </Link>

            {/* 사용자명 */}
            <span className="text-lg text-gray-800 font-semibold">{user?.username}님</span>

            {/* 로그아웃 */}
            <button
              onClick={handleLogout}
              className="flex items-center gap-2 px-4 py-2 bg-orange-600 text-white font-semibold rounded-lg hover:bg-orange-700 shadow transition"
            >
              로그아웃
            </button>
          </div>
        ) : (
          <Link to="/login"
            className="flex items-center gap-2 px-4 py-2 bg-orange-600 text-white font-semibold rounded-lg hover:bg-orange-700 shadow transition"
          >
            로그인
          </Link>
        )}
      </div>
    </nav>
  );
}

export default Navbar;
