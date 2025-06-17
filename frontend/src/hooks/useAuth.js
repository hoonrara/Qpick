import { useEffect, useState } from 'react';
import api from '../api';

export function useAuth() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) return;

    const fetchUser = async () => {
      try {
        const res = await api.get('/users/me/info');
        setUser(res.data);
      } catch (err) {
        setUser(null);
        localStorage.removeItem('token');
      }
    };

    fetchUser();
  }, []);

  return { user };
}
