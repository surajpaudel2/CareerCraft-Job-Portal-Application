import { useEffect } from "react";

export default function useAuthError(
  isAuthenticated,
  errorMessage,
  setErrorMessage
) {
  useEffect(() => {
    if (isAuthenticated) {
      setErrorMessage("");
    } else if (errorMessage) {
      setErrorMessage(errorMessage);
    }
  }, [isAuthenticated, errorMessage, setErrorMessage]);
}
