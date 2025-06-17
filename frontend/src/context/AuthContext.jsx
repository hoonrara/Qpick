import { createContext, useContext, useEffect, useState } from 'react';
import api from '../units/api';

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [user, setUser] = useState(null);

  const isLoggedIn = !!token;

  useEffect(() => {
    if (token) {
      api.get('/users/me/info')
        .then(res => setUser(res.data))
        .catch(() => {
          setUser(null);
          setToken(null);
          localStorage.removeItem('token');
        });
    }
  }, [token]);

  const login = (newToken) => {
    localStorage.setItem('token', newToken);
    setToken(newToken);
  };

  const logout = () => {
    localStorage.removeItem('token');
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ token, user, isLoggedIn, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

// ✅ 어디서든 사용할 수 있도록 hook으로 export
export function useAuth() {
  return useContext(AuthContext);
}
