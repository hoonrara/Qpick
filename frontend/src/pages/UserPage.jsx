import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { ArrowRight } from "lucide-react";
import { useAuth } from "../context/AuthContext";
import { Link } from "react-router-dom";
import api from "../units/api";

function UserPage() {
  const { user, isLoggedIn } = useAuth();
  const navigate = useNavigate();
  const [histories, setHistories] = useState([]);
  const [myCreatedQuizSets, setMyCreatedQuizSets] = useState([]);

  // ✅ 삭제 핸들러 함수
  const handleDelete = async (quizSetId) => {
    const userId = localStorage.getItem("userId");
    if (!window.confirm("정말 이 퀴즈셋을 삭제하시겠습니까?")) return;

    try {
      await api.delete(`/quizsets/${quizSetId}`, {
        params: { userId }, // 삭제할 때 userId 같이 보내기
      });
      alert("✅ 삭제되었습니다.");

      // 프론트 상태에서도 바로 제거
      setMyCreatedQuizSets((prev) => prev.filter((qs) => qs.id !== quizSetId));
    } catch (err) {
      alert("❌ 삭제 실패: " + (err.response?.data?.message || err.message));
    }
  };

  // ✅ 로그인 안 했으면 로그인 페이지로 리다이렉트
  useEffect(() => {
    if (!isLoggedIn) {
      navigate("/login");
    }
  }, [isLoggedIn]);

  // ✅ 내 푼 퀴즈 + 내가 만든 퀴즈셋 불러오기
  useEffect(() => {
    const userId = localStorage.getItem("userId");
    if (userId) {
      // 최근 푼 퀴즈셋
      api.get(`/answers/history/quizsets/${userId}`)
        .then((res) => setHistories(res.data.slice(0, 3)))
        .catch(() => setHistories([]));

      // 내가 만든 퀴즈셋
      api.get(`/quizsets/created/${userId}`)
        .then((res) => setMyCreatedQuizSets(res.data))
        .catch(() => setMyCreatedQuizSets([]));
    }
  }, []);

  if (!isLoggedIn) return null;

  return (
    <div className="pl-24">
      {/* 유저 기본 정보 */}
      <span className="text-lg text-gray-800 font-semibold">{user?.username}님</span>
      <div className="text-sm text-gray-500 mb-4">
        Qpick에 가입하신 날짜 : {user?.createdAt?.slice(0, 10)}
      </div>

      {/* ✅ 최근에 푼 퀴즈 */}
      <h2 className="text-xl font-bold mt-8 mb-4 flex items-center gap-2">
        📚 최근에 푼 퀴즈
        <Link to="/history" className="text-sm text-purple-600 ml-auto hover:underline">
          전체 보기 →
        </Link>
      </h2>

      {histories.length === 0 ? (
        <p className="text-gray-500 text-sm">아직 푼 퀴즈셋이 없습니다.</p>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {histories.map((item) => (
            <div key={item.quizSetId} className="bg-white border rounded-xl shadow-md p-5">
              <img
                src={item.imageUrl}
                alt={item.title}
                className="w-full h-40 object-cover rounded mb-4"
                onError={(e) => (e.target.src = '/default.jpg')}
              />
              <h3 className="text-lg font-semibold mb-1">{item.title}</h3>
              <p className="text-sm text-gray-600">정답 개수: {item.correctCount}개</p>
              <p className="text-sm text-gray-600">점수: {item.score}점</p>
              <p className="text-xs text-gray-400 mb-3">푼 날짜: {item.solvedAt.slice(0, 10)}</p>

              <button
                onClick={() => navigate(`/quiz/${item.quizSetId}`)}
                className="w-full bg-purple-500 text-white py-2 rounded hover:bg-purple-600 transition"
              >
                다시 풀기
              </button>
            </div>
          ))}
        </div>
      )}

      {/* ✅ 내가 만든 퀴즈셋 */}
      <h2 className="text-xl font-bold mt-10 mb-4">🛠️ 내가 만든 퀴즈</h2>
      {myCreatedQuizSets.length === 0 ? (
        <p className="text-gray-500 text-sm">아직 만든 퀴즈셋이 없습니다.</p>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {myCreatedQuizSets.map((quizSet) => (
            <div key={quizSet.id} className="bg-white border rounded-xl shadow-md p-5">
              <img
                src={quizSet.imageUrl}
                alt={quizSet.title}
                className="w-full h-40 object-cover rounded mb-4"
                onError={(e) => (e.target.src = '/default.jpg')}
              />
              <h3 className="text-lg font-semibold mb-2">{quizSet.title}</h3>
              <p className="text-sm text-gray-500 mb-3">{quizSet.description}</p>

              {/* 풀어보기 버튼 */}
              <button
                onClick={() => navigate(`/quiz/${quizSet.id}`)}
                className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600 transition"
              >
                풀어보기
              </button>

              {/* ✅ 삭제 버튼 */}
              <button
                onClick={() => handleDelete(quizSet.id)}
                className="w-full mt-2 bg-red-500 text-white py-2 rounded hover:bg-red-600 transition"
              >
                삭제하기
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default UserPage;
