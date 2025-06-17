import { useState, useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../units/api";
import { resizeImage } from "../utils/imageUtils";

function QuizBulkCreatePage() {
  const { quizSetId } = useParams();
  const navigate = useNavigate();

  const [quizzes, setQuizzes] = useState(
    Array.from({ length: 10 }, () => ({
      title: "",
      answer: "",
      imageFile: null,
      imageUrl: "",
      previewImage: "", // 미리보기 이미지 URL을 위한 상태 추가
    }))
  );

  const fileInputRef = useRef([]);

  const handleInputChange = (index, field, value) => {
    const updated = [...quizzes];
    updated[index][field] = value;
    setQuizzes(updated);
  };

  const handleFileChange = (index, file) => {
    const updated = [...quizzes];
    updated[index].imageFile = file;

    // 이미지 미리보기 업데이트
    const reader = new FileReader();
    reader.onloadend = () => {
      updated[index].previewImage = reader.result; // 미리보기 URL 설정
      setQuizzes(updated); // 상태 업데이트
    };
    reader.readAsDataURL(file); // 파일을 Data URL로 읽기
  };

  const handleUploadImages = async () => {
    const uploaded = await Promise.all(
      quizzes.map(async (q, i) => {
        if (!q.imageFile) return { ...q };

        const resized = await resizeImage(q.imageFile, 800);
        const formData = new FormData();
        formData.append("file", resized);

        try {
          const res = await api.post("/s3/upload", formData);
          return { ...q, imageUrl: res.data };
        } catch (e) {
          alert(`${i + 1}번째 이미지 업로드 실패`);
          return q;
        }
      })
    );
    setQuizzes(uploaded);
    alert("✅ 이미지 업로드 완료!");
  };

  const handleSubmit = async () => {
    const ready = quizzes.every((q) => q.title && q.answer && q.imageUrl);
    if (!ready) {
      alert("모든 퀴즈 항목을 입력하고 이미지 업로드를 완료해주세요.");
      return;
    }

    const requestList = quizzes.map(({ title, answer, imageUrl }) => ({
      title,
      answer,
      imageUrl,
    }));

    try {
      await api.post(`/quiz/bulk/${quizSetId}`, requestList);
      alert("✅ 퀴즈 10개 등록 완료!");
      navigate("/");
    } catch (err) {
      alert("❌ 등록 실패");
    }
  };

  const handleFileButtonClick = (index) => {
    fileInputRef.current[index].click();
  };

  return (
    <div className="max-w-4xl mx-auto p-6 bg-white rounded shadow">
      <h1 className="text-2xl font-bold mb-6">🧮 퀴즈 10개 일괄 등록</h1>

      {quizzes.map((quiz, index) => (
        <div key={index} className="mb-8 border-b pb-4">
          <h2 className="text-lg font-semibold mb-2">Q{index + 1}</h2>

          <input
            type="text"
            placeholder="문제 제목"
            value={quiz.title}
            onChange={(e) => handleInputChange(index, "title", e.target.value)}
            className="w-full p-2 border rounded mb-2"
          />

          <input
            type="text"
            placeholder="정답"
            value={quiz.answer}
            onChange={(e) => handleInputChange(index, "answer", e.target.value)}
            className="w-full p-2 border rounded mb-2"
          />

          {/* 버튼으로 파일 선택 */}
          <button
            onClick={() => handleFileButtonClick(index)}
            className="w-full bg-purple-600 hover:bg-purple-700 text-white py-2 rounded mb-2"
          >
            이미지 선택
          </button>
          <input
            ref={(el) => (fileInputRef.current[index] = el)}
            type="file"
            accept="image/*"
            onChange={(e) => handleFileChange(index, e.target.files[0])}
            style={{ display: "none" }} // 버튼이 클릭될 때만 파일 선택
          />

          {/* 이미지 미리보기 */}
          {quiz.previewImage && (
            <img
              src={quiz.previewImage}
              alt={`미리보기 ${index + 1}`}
              className="w-40 h-40 object-cover rounded border"
            />
          )}
        </div>
      ))}

      <div className="flex gap-4">
        <button
          onClick={handleUploadImages}
          className="bg-purple-600 text-white px-6 py-2 rounded hover:bg-purple-700"
        >
          이미지 업로드
        </button>

        <button
          onClick={handleSubmit}
          className="bg-green-600 text-white px-6 py-2 rounded hover:bg-green-700"
        >
          퀴즈 10개 등록
        </button>
      </div>
    </div>
  );
}

export default QuizBulkCreatePage;
