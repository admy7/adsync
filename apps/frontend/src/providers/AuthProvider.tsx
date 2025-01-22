import React, { createContext, useCallback, useContext, useState } from "react";
import { jwtDecode } from "jwt-decode";
import { client } from "../api";

interface AuthContextType {
  login: (email: string, password: string) => void;
  logout: () => void;
  isAuthenticated: () => boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [activeToken, setToken] = useState<string | null>(localStorage.getItem("token"));

  const login = async (email: string, password: string) => {
    try {
      const { token } = await client.loginUser({ email, password });
      setToken(token);
      localStorage.setItem("token", token);
    } catch (error) {
      console.error(error);
    }
  };

  const logout = () => {
    setToken(null);
    localStorage.removeItem("token");
  };

  const isAuthenticated = useCallback(() => {
    if (!activeToken) return false;

    const { exp } = jwtDecode<{ exp: number }>(activeToken);
    return Date.now() < exp * 1000;
  }, [activeToken]);

  return (
    <AuthContext.Provider value={{ login, logout, isAuthenticated }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used within an AuthProvider");
  return context;
};
