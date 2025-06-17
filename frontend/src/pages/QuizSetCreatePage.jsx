import { useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../units/api";
import { resizeImage } from "../utils/imageUtils";

function QuizSetCreatePage() {
  const navigate = useNavigate();
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [imageFile, setImageFile] = useState(null);
  const [imageUrl, setImageUrl] = useState("");
  const [uploading, setUploading] = useState(false);

  const fileInputRef = useRef(null);

  // 이미지 파일 선택
  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setImageFile(file);
      setImageUrl(URL.createObjectURL(file)); // 미리보기용 URL 생성
    }
  };

  // 이미지 업로드 및 퀴즈셋 등록
  const handleAction = async () => {
    if (!imageFile) {
      fileInputRef.current.click();
      return;
    }

    if (uploading) return;

    setUploading(true);

    try {
      const resized = await resizeImage(imageFile, 800);
      const formData = new FormData();
      formData.append("file", resized);

      const uploadRes = await api.post("/s3/upload", formData);
      setImageUrl(uploadRes.data);
      alert("✅ 이미지 업로드 성공");

      const userId = localStorage.getItem("userId");
      if (!userId) {
        alert("로그인 정보를 찾을 수 없습니다.");
        return;
      }

      if (!title || !description || !imageUrl) {
        return alert("모든 항목을 입력하고 이미지 업로드를 완료해주세요.");
      }

      const quizRes = await api.post("/quizsets", {
        title,
        description,
        imageUrl: uploadRes.data,
        userId: Number(userId),  // userId
      });

      const newId = quizRes.data.id;
      alert("✅ 퀴즈셋 생성 완료!");
      navigate(`/quiz/create/${newId}`);
    } catch (error) {
      console.error("Error during upload and submit", error);
      alert("❌ 이미지 업로드 및 퀴즈셋 등록 실패");
    } finally {
      setUploading(false);
    }
  };

  return (
    <div className="max-w-xl mx-auto p-6 bg-white rounded shadow">
      <h1 className="text-2xl font-bold mb-6">📂 퀴즈 만들기</h1>

      <input
        type="text"
        placeholder="퀴즈 제목"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        className="w-full p-3 mb-3 border rounded"
      />

      <input
        type="text"
        placeholder="설명"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
        className="w-full p-3 mb-3 border rounded"
      />

      {/* 파일 선택 버튼 */}
      <button
        onClick={() => fileInputRef.current.click()}
        className="w-full bg-purple-300 text-white py-2 rounded mb-4 hover:bg-purple-400"
      >
        썸네일 선택
      </button>

      {/* 숨겨진 파일 input */}
      <input
        type="file"
        accept="image/*"
        ref={fileInputRef}
        onChange={handleFileChange}
        style={{ display: "none" }}
      />

      {/* 이미지 미리보기 */}
      {imageUrl && (
        <img
          src={imageUrl}
          alt="대표 이미지"
          className="w-full h-60 object-cover rounded mb-4 border"
        />
      )}

      <button
        onClick={handleAction}
        className={`w-full text-white py-2 rounded 
    ${uploading || !title || !description || !imageFile
            ? "bg-purple-200 cursor-not-allowed"
            : "bg-purple-300 hover:bg-purple-400"}
  `}
        disabled={uploading || !title || !description || !imageFile}
      >
        {uploading ? "업로드 및 등록 중..." : "퀴즈 만들러 가기"}
      </button>
    </div>
  );
}

export default QuizSetCreatePage;
