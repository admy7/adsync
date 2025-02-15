import { createBrowserRouter, Navigate, Outlet } from "react-router-dom";
import { Layout } from "./Layout";
import { SignIn } from "./components/auth/SignIn";
import { SignUp } from "./components/auth/SignUp";
import { useAuth } from "./providers/AuthProvider.tsx";
import { Dashboard } from "./components/dashboard/Dashboard.tsx";

const ProtectedRoute = () => {
  const { isAuthenticated, logout } = useAuth();

  if (!isAuthenticated()) {
    logout();
    return <Navigate to="/signin" replace />;
  }

  return <Outlet />;
};

const PublicRoute = () => {
  const { isAuthenticated } = useAuth();

  if (isAuthenticated()) {
    return <Navigate to="/" replace />;
  }

  return <Outlet />;
};

export const router = createBrowserRouter([
  {
    element: <PublicRoute />,
    children: [
      {
        path: "signin",
        element: <SignIn />,
      },
      {
        path: "signup",
        element: <SignUp />,
      },
    ],
  },
  {
    element: <ProtectedRoute />,
    children: [
      {
        element: <Layout />,
        children: [
          {
            path: "/",
            element: <Dashboard />,
          },
        ],
      },
    ],
  },
]);
