import { useNavigate } from 'react-router-dom';

function QuizSetCard({ quizSet }) {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/quiz/${quizSet.id}`);
  };

  return (
    <div
      onClick={handleClick}
      className="cursor-pointer bg-violet-100 rounded-2xl border border-gray-200 hover:border-indigo-400 shadow-md hover:shadow-lg transition-transform duration-200 hover:-translate-y-1 group overflow-hidden"
    >
      {/* 퀴즈 이미지 */}
      {quizSet.imageUrl && (
        <img
          src={quizSet.imageUrl}
          alt={quizSet.title}
          className="w-full h-44 object-cover group-hover:scale-105 transition-transform duration-300"
        />
      )}

      {/* 텍스트 영역 */}
      <div className="p-5">
        {/* 퀴즈 제목 */}
        <h2 className="text-xl font-semibold text-gray-800 mb-2">
          {quizSet.title}
        </h2>

        {/* 퀴즈 설명 */}
        <p className="text-sm text-gray-700">
          {quizSet.description}
        </p>
      </div>
    </div>
  );
}

export default QuizSetCard;
