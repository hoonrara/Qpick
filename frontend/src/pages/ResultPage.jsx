import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ResultCard from '../components/ResultCard';

function ResultPage() {
  const [result, setResult] = useState(null);
  const [currentIndex, setCurrentIndex] = useState(0);
  const navigate = useNavigate();

  useEffect(() => {
    const saved = localStorage.getItem('result');
    if (!saved) {
      alert('결과 정보가 없습니다.');
      navigate('/');
      return;
    }
    setResult(JSON.parse(saved));
  }, []);

  if (!result) return <div className="text-center mt-10 text-xl">로딩 중...</div>;

  const handlePrev = () => {
    if (currentIndex > 0) setCurrentIndex(currentIndex - 1);
  };

  const handleNext = () => {
    if (currentIndex < result.answers.length - 1) setCurrentIndex(currentIndex + 1);
  };

  const currentAnswer = result.answers[currentIndex];

  return (
    <div className="flex flex-col items-center justify-center min-h-screen p-6 bg-gray-50">
      <h1 className="text-4xl font-bold mb-3">🎉 퀴즈 완료!</h1>
      <p className="text-2xl mb-6 text-gray-700">
        총 점수: <span className="font-semibold">{result.score}점</span> (맞춘 개수: {result.correctCount})
      </p>

      {/* 카드 + 좌우 버튼 */}
      <div className="flex items-center justify-center gap-6 w-full max-w-5xl">
        <button
          onClick={handlePrev}
          disabled={currentIndex === 0}
          className="bg-purple-200 text-purple-800 hover:bg-purple-300 disabled:opacity-30
            rounded-full w-12 h-12 text-2xl font-bold shadow-md transition"
        >
          &lt;
        </button>

        <ResultCard answer={currentAnswer} index={currentIndex} />

        <button
          onClick={handleNext}
          disabled={currentIndex === result.answers.length - 1}
          className="bg-purple-200 text-purple-800 hover:bg-purple-300 disabled:opacity-30
            rounded-full w-12 h-12 text-2xl font-bold shadow-md transition"
        >
          &gt;
        </button>
      </div>

      {/* 현재 번호 */}
      <p className="mt-6 text-lg font-medium text-gray-600">
        {currentIndex + 1} / {result.answers.length}
      </p>

      {/* 처음으로 버튼 */}
      <button
        onClick={() => navigate('/')}
        className="mt-8 px-8 py-3 bg-purple-200 text-purple-900 font-semibold rounded hover:bg-purple-300 transition text-lg"
      >
        처음으로
      </button>
    </div>
  );
}

export default ResultPage;
