import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useState,
} from "react";
import apiBaseUrl from "../api/apiBaseUrl";
import { useAuth } from "./AuthContext";

const EmployerContext = createContext();

function EmployerProvider({ children }) {
  const { user } = useAuth();
  const email = user?.email;
  const jwtToken = localStorage.getItem("jwtToken");

  const [companyDetails, setCompanyDetails] = useState(null);

  const fetchCompanyDetails = useCallback(async () => {
    if (email && jwtToken) {
      try {
        const response = await apiBaseUrl.get(`/employer/profile`, {
          params: { usernameOrEmail: email },
          headers: {
            Authorization: `Bearer ${jwtToken}`,
          },
        });
        setCompanyDetails({ ...response.data });
      } catch (error) {
        console.error("Error fetching company details:", error);
      }
    }
  }, [email, jwtToken]); // Dependencies for useCallback

  useEffect(() => {
    fetchCompanyDetails();
  }, [fetchCompanyDetails]); // Dependencies for useEffect

  return (
    <EmployerContext.Provider value={{ companyDetails, fetchCompanyDetails }}>
      {children}
    </EmployerContext.Provider>
  );
}

function useEmployer() {
  const context = useContext(EmployerContext);
  if (context === undefined) {
    throw new Error("useEmployer must be used within an EmployerProvider");
  }
  return context;
}

export { EmployerProvider, useEmployer };
