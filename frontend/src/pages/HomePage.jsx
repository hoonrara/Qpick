import { useEffect, useState } from "react";
import api from "../units/api";
import QuizSetCard from "../components/QuizSetCard";

function HomePage() {
  const [quizSets, setQuizSets] = useState([]);
  const [search, setSearch] = useState(""); // 🔍 검색어 상태

  useEffect(() => {
    api
      .get("/quizsets", {
        params: { search }, // ✅ 검색 파라미터 전송
      })
      .then((res) => setQuizSets(res.data))
      .catch(() => alert("퀴즈셋 불러오기 실패"));
  }, [search]); // 🔁 검색어 변경 시 자동 호출

  return (
    <div className="flex flex-col items-center p-6 bg-white min-h-screen">
      <h1 className="text-2xl font-bold mb-4">🧠 퀴즈를 선택하세요</h1>

      {/* 🔍 검색 입력창 */}
      <div className="w-full max-w-2xl mb-6">
        <input
          type="text"
          placeholder="퀴즈 제목을 검색하세요"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="w-full border border-gray-300 rounded px-4 py-2"
        />
      </div>

      {/* 퀴즈셋 카드 리스트 */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 w-full max-w-6xl">
        {quizSets.length === 0 ? (
          <p className="text-gray-500">조건에 맞는 퀴즈셋이 없습니다.</p>
        ) : (
          quizSets.map((quizSet) => (
            <QuizSetCard key={quizSet.id} quizSet={quizSet} />
          ))
        )}
      </div>
    </div>
  );
}

export default HomePage;
