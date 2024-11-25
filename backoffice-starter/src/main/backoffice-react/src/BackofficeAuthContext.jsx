import React, { createContext, useContext, useState, useEffect } from "react";

const BackofficeAuthContext = createContext(null);

export const BackofficeAuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    // Check if the user is authenticated on initial load
    checkAuth();
  }, []);

  const checkAuth = async () => {};

  const refreshAccessToken = async () => {};

  const login = async (credentials) => {};

  const logout = async () => {};

  const register = async (userInfo) => {};

  return (
    <BackofficeAuthContext.Provider
      value={{ isAuthenticated, login, logout, register }}
    >
      {children}
    </BackofficeAuthContext.Provider>
  );
};

export const useBackofficeAuth = () => useContext(BackofficeAuthContext);
