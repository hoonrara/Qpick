import { useState } from 'react';

function QuizCard({ quiz, onSubmit }) {
  const [answer, setAnswer] = useState('');
  const [error, setError] = useState(false); // 🔴 에러 상태 관리

  const handleSubmit = () => {
    if (!answer.trim()) {
      setError(true); // 입력 안 했으면 에러 상태 ON
      return;
    }

    setError(false); // 제출 성공 시 에러 OFF
    onSubmit(answer.trim());
    setAnswer('');
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      handleSubmit();
    }
  };

  return (
    <div className="flex flex-col items-center justify-center bg-violet-50 rounded-2xl shadow-lg p-8 w-full max-w-xl text-center border border-violet-100 mt-10">
      <p className="text-2xl font-bold mb-6">{quiz.title}</p>

      <img
        src={quiz.imageUrl}
        alt="퀴즈 이미지"
        onError={(e) => (e.target.src = '/default.jpg')}
        className="w-64 h-64 object-cover rounded mb-6"
      />

      <input
        type="text"
        value={answer}
        onChange={(e) => {
          setAnswer(e.target.value);
          setError(false); // 입력 중이면 에러 해제
        }}
        onKeyDown={handleKeyDown}
        placeholder="정답을 입력하세요"
        className={`border p-3 rounded w-full max-w-xs text-center text-base 
          ${error ? 'border-red-500' : 'border-gray-300'}`}
      />

      {error && (
        <p className="text-red-500 text-sm mt-2">정답을 입력해주세요.</p>
      )}

      <button
        onClick={handleSubmit}
        className="mt-6 bg-purple-500 hover:bg-purple-600 text-white font-medium px-6 py-2 rounded transition"
      >
        제출하기
      </button>
    </div>
  );
}

export default QuizCard;
