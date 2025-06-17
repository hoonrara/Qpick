import { BrowserRouter as Router, Routes, Route, useLocation } from "react-router-dom";
import { lazy, Suspense } from "react";
import { AuthProvider } from "./context/AuthContext";
import Navbar from "./components/Navbar";
import Sidebar from "./components/Sidebar";
import ProtectedRoute from "./components/ProtectedRoute";

// Lazy-loaded pages
const HomePage = lazy(() => import("./pages/HomePage"));
const UserPage = lazy(() => import("./pages/UserPage"));
const QuizPage = lazy(() => import("./pages/QuizPage"));
const ResultPage = lazy(() => import("./pages/ResultPage"));
const HistoryPage = lazy(() => import("./pages/HistoryPage"));
const LoginPage = lazy(() => import("./pages/LoginPage"));
const SocialSuccessPage = lazy(() => import("./pages/SocialSuccessPage"));
const QuizBulkCreatePage = lazy(() => import("./pages/QuizBulkCreatePage"));
const QuizSetCreatePage = lazy(() => import("./pages/QuizSetCreatePage")); // ✅ 퀴즈셋 생성

function AppContent() {
  const location = useLocation();

  const hiddenRoutes = ["/social/success"];
  const showSidebar = !hiddenRoutes.includes(location.pathname);
  const showNavbar = !hiddenRoutes.includes(location.pathname);

  return (
    <>
      {showNavbar && <Navbar />}
      <div className="flex min-h-screen">
        {showSidebar && <Sidebar />}
        <div className="flex-1 p-6">
          <Suspense fallback={<div className="text-center mt-10">로딩 중...</div>}>
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/user" element={
                <ProtectedRoute>
                  <UserPage />
                </ProtectedRoute>
              } />
              <Route path="/quiz/:quizSetId" element={<QuizPage />} />
              <Route path="/result" element={<ResultPage />} />
              <Route path="/history" element={<HistoryPage />} />
              <Route path="/login" element={<LoginPage />} />
              <Route path="/social/success" element={<SocialSuccessPage />} />

              {/* ✅ 퀴즈셋 생성 페이지 */}
              <Route path="/quizset/create" element={
                <ProtectedRoute>
                  <QuizSetCreatePage />
                </ProtectedRoute>
              } />

              {/* ✅ 퀴즈 10개 등록 페이지 */}
              <Route path="/quiz/create/:quizSetId" element={
                <ProtectedRoute>
                  <QuizBulkCreatePage />
                </ProtectedRoute>
              } />
            </Routes>
          </Suspense>
        </div>
      </div>
    </>
  );
}

function App() {
  return (
    <AuthProvider>
      <Router>
        <AppContent />
      </Router>
    </AuthProvider>
  );
}

export default App;
