import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../units/api";
import QuizCard from "../components/QuizCard"; // ✅ QuizCard 불러오기

function QuizPage() {
  const [quizzes, setQuizzes] = useState([]);
  const [current, setCurrent] = useState(0);
  const [answers, setAnswers] = useState([]);
  const { quizSetId } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    api.get(`/quiz/set/${quizSetId}`)
      .then((res) => setQuizzes(res.data))
      .catch((err) => {
        console.error("❌ 퀴즈 불러오기 실패:", err);
        alert("퀴즈를 불러올 수 없습니다.");
      });
  }, [quizSetId]);

  const handleSubmit = async (userAnswer) => {
    const newAnswers = [
      ...answers,
      {
        quizId: quizzes[current].quizId,
        userAnswer,
      },
    ];

    if (current === quizzes.length - 1) {
      try {
        const userId = localStorage.getItem("userId") || 0;
        const res = await api.post(`/answers/submit?userId=${userId}`, newAnswers);
        localStorage.setItem("result", JSON.stringify(res.data));
        navigate("/result");
      } catch (err) {
        alert("제출 실패: " + err.message);
      }
    } else {
      setCurrent(current + 1);
      setAnswers(newAnswers);
    }
  };

  return (
    <div className="flex justify-center pt-16 px-4 bg-white min-h-screen">
      {quizzes.length > 0 ? (
        <div className="w-full max-w-xl">
          <QuizCard
            quiz={quizzes[current]}
            onSubmit={handleSubmit}
          />
        </div>
      ) : (
        <p className="text-gray-500 text-sm mt-10">퀴즈 정보가 없습니다.</p>
      )}
    </div>
  );
}

export default QuizPage;
