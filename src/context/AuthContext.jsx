import React from 'react';
import { createContext, useContext, useMemo, useState } from 'react';
import { endpoints } from '../services/api';

const AuthContext = createContext(null);

function decodeJwt(token) {
  try {
    const part = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
    return JSON.parse(decodeURIComponent(atob(part).split('').map(c => '%' + c.charCodeAt(0).toString(16).padStart(2, '0')).join('')));
  } catch { return {}; }
}

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('edupulse_token'));
  const [user, setUser] = useState(() => {
    try { return JSON.parse(localStorage.getItem('edupulse_user')) || null; } catch { return null; }
  });

  const login = async (username, password) => {
    const response = await endpoints.login({ username, password });
    const body = response.data;
    const jwt = typeof body === 'string' ? body : (body.token || body.accessToken || body.jwt);
    if (!jwt) throw new Error('Login response did not contain a token. Check the Auth Service response format.');
    const claims = decodeJwt(jwt);
    const currentUser = { username: claims.sub || username, role: claims.role || 'USER', studentId: claims.studentId ?? null };
    localStorage.setItem('edupulse_token', jwt);
    localStorage.setItem('edupulse_user', JSON.stringify(currentUser));
    setToken(jwt); setUser(currentUser);
  };

  const logout = () => {
    localStorage.removeItem('edupulse_token'); localStorage.removeItem('edupulse_user');
    setToken(null); setUser(null);
  };

  const value = useMemo(() => ({ token, user, login, logout, isAuthenticated: Boolean(token) }), [token, user]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export const useAuth = () => useContext(AuthContext);
