import { useEffect } from "react";

import { useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../../../contexts/AuthContext";

export function useEmployerAuth() {
  const jwtToken = localStorage.getItem("JwtToken");
  const { isAuthenticated, user } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    // Redirect to login if not authenticated or missing JWT
    if (!isAuthenticated || !jwtToken) {
      navigate("/login");
    } else if (location.pathname.startsWith("/employer")) {
      const hasEmployerRole = user?.roles?.some(
        (role) => role.name === "ROLE_EMPLOYER"
      );

      if (!hasEmployerRole) {
        navigate("/register-employer");
      }
    }
  }, [isAuthenticated, jwtToken, location, navigate, user]);

  return {
    isAuthenticated,
    hasEmployerRole: user?.roles?.some((role) => role.name === "ROLE_EMPLOYER"),
  };
}
