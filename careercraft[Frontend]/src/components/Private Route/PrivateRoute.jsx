import { useEmployerAuth } from "./hooks/useEmployerAuth";

export default function PrivateRoute({ children }) {
  const { isAuthenticated, hasEmployerRole } = useEmployerAuth();

  return isAuthenticated && hasEmployerRole ? children : null; // Or a fallback component if needed
}
