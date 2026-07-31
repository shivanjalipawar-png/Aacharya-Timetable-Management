import { createContext, useContext, useState } from "react";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);

  // TEMPORARY FRONTEND LOGIN
  // This will later be replaced by backend authentication.
  const login = (email, password) => {
    // Temporary Admin account
    if (
      email === "admin@acharya.com" &&
      password === "admin123"
    ) {
      const adminUser = {
        id: 1,
        name: "Admin",
        email: "admin@acharya.com",
        role: "admin",
      };

      setUser(adminUser);

      return {
        success: true,
        user: adminUser,
      };
    }

    // Temporary Viewer account
    if (
      email === "viewer@acharya.com" &&
      password === "viewer123"
    ) {
      const viewerUser = {
        id: 2,
        name: "User",
        email: "viewer@acharya.com",
        role: "viewer",
      };

      setUser(viewerUser);

      return {
        success: true,
        user: viewerUser,
      };
    }

    return {
      success: false,
      message: "Invalid email or password.",
    };
  };

  const logout = () => {
    setUser(null);
  };

  const isAdmin = user?.role === "admin";

  return (
    <AuthContext.Provider
      value={{
        user,
        login,
        logout,
        isAdmin,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error(
      "useAuth must be used inside AuthProvider"
    );
  }

  return context;
}