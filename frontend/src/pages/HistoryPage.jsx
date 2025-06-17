import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../units/api';

function HistoryPage() {
  const [histories, setHistories] = useState([]);
  const userId = localStorage.getItem('userId');
  const navigate = useNavigate();

  useEffect(() => {
    if (!userId) return;

    api.get(`/answers/history/quizsets/${userId}`)
      .then((res) => setHistories(res.data))
      .catch((err) => {
        console.error('❌ 기록 불러오기 실패', err);
        alert('기록을 불러올 수 없습니다.');
      });
  }, [userId]);

  return (
    <div className="p-6">
      {/* ✅ 제목과 돌아가기 버튼을 한 줄에 정렬 */}
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold">📚 내 퀴즈 기록</h1>
        <button
          onClick={() => navigate("/user")}
          className="text-sm font-medium text-purple-600 hover:underline"
        >
          ← 돌아가기
        </button>
      </div>

      {histories.length === 0 ? (
        <p className="text-gray-600">아직 푼 퀴즈셋이 없습니다.</p>
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
              <h2 className="text-lg font-semibold text-gray-800 mb-1">{item.title}</h2>
              <p className="text-sm text-gray-600">정답 개수: {item.correctCount}개</p>
              <p className="text-sm text-gray-600">점수: {item.score}점</p>
              <p className="text-xs text-gray-400 mb-3">푼 날짜: {item.solvedAt.slice(0, 10)}</p>

              <button
                onClick={() => window.location.href = `/quiz/${item.quizSetId}`}
                className="w-full bg-purple-500 text-white py-2 rounded hover:bg-purple-600 transition"
              >
                다시 풀기
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );

}

export default HistoryPage;
