function ResultCard({ answer, index }) {
    const isCorrect = answer.isCorrect === true;
  
    return (
      <div
        className={`flex flex-col items-center justify-center rounded-2xl shadow-lg p-8 w-full max-w-xl text-center border
          ${isCorrect ? 'bg-green-100 border-green-200' : 'bg-red-100 border-red-200'}`}
      >
        {/* 문제 제목 */}
        <p className="text-xl font-semibold mb-6">
          Q{index + 1}. {answer.title}
        </p>
  
        {/* 이미지 */}
        <img
          src={answer.imageUrl}
          alt={`퀴즈 이미지 ${index + 1}`}
          onError={(e) => (e.target.src = '/default.jpg')}
          className="w-64 h-64 object-cover rounded border mb-6"
        />
  
        {/* 정답/오답 및 실제 정답 */}
        <div className="mt-4">
          <p className={`text-2xl font-bold ${isCorrect ? 'text-green-700' : 'text-red-700'}`}>
            {isCorrect ? '정답!' : '오답!'}
          </p>
  
          {answer.correctAnswer && (
            <p className="text-3xl font-bold text-gray-800 mt-2">{answer.correctAnswer}</p>
          )}
        </div>
      </div>
    );
  }
  
  export default ResultCard;
  