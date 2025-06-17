// src/components/Sidebar.jsx
import { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { Home, User, LogIn, ChevronLeft, ChevronRight, LogOut } from "lucide-react";
import { useAuth } from "../context/AuthContext";

export default function Sidebar() {
  const location = useLocation();
  const [collapsed, setCollapsed] = useState(false);
  const [isHiding, setIsHiding] = useState(false);
  const { isLoggedIn, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  // ✅ key를 명시적으로 부여
  const navItems = [
    { key: "home", path: "/", label: "Home", icon: <Home size={24} /> },
    { key: "user", path: "/user", label: "User", icon: <User size={24} /> },
    !isLoggedIn
      ? { key: "login", path: "/login", label: "Login", icon: <LogIn size={24} /> }
      : { key: "logout", label: "Logout", action: handleLogout, icon: <LogOut size={24} /> }
  ];

  const handleCollapse = () => {
    setIsHiding(true);
    setTimeout(() => {
      setCollapsed(true);
      setIsHiding(false);
    }, 300);
  };

  if (collapsed && !isHiding) {
    return (
      <div className="fixed top-1/2 left-2 -translate-y-1/2 z-50 transition-all duration-300">
        <button
          onClick={() => setCollapsed(false)}
          className="bg-[#E0CCFF] p-2 rounded-full shadow hover:bg-[#d1baff] transition"
        >
          <ChevronRight size={20} />
        </button>
      </div>
    );
  }

  return (
    <div
      className={`
        fixed top-1/2 left-0 -translate-y-1/2 z-40
        w-20 bg-[#F6EDFF] rounded-xl py-4 px-3 shadow-md
        flex flex-col items-center gap-6
        transition-all duration-300 ease-in-out
        ${collapsed ? "-translate-x-full opacity-0 pointer-events-none" : "translate-x-0 opacity-100"}
      `}
    >
      <div className="w-full flex justify-center mb-4">
        <button
          onClick={handleCollapse}
          className="bg-[#E0CCFF] p-2 rounded-full shadow hover:bg-[#d1baff] transition"
        >
          <ChevronLeft size={20} />
        </button>
      </div>

      <div className="flex flex-col items-center gap-6 mt-2">
        {navItems.map(({ key, path, label, icon, action }) => {
          const isActive = path && location.pathname === path;

          // ✅ Logout 버튼은 <button>
          if (action) {
            return (
              <button
                key={key}
                onClick={action}
                className="flex flex-col items-center p-2 rounded-lg w-16 transition text-sm text-gray-600 hover:bg-purple-100"
              >
                {icon}
                <span className="mt-1 text-center">{label}</span>
              </button>
            );
          }

          return (
            <Link
              key={key}
              to={path}
              className={`
                flex flex-col items-center p-2 rounded-lg w-16 transition text-sm
                ${isActive
                  ? "bg-white text-purple-600 font-semibold shadow"
                  : "text-gray-600 hover:bg-purple-100"}
              `}
            >
              {icon}
              <span className="mt-1 text-center">{label}</span>
            </Link>
          );
        })}
      </div>
    </div>
  );
}
