import { GoogleLogin } from "@react-oauth/google";
import apiBaseUrl from "../../api/apiBaseUrl";
import { useAuth } from "../../contexts/AuthContext";

const loginErrorMessage = "Login Failed. Please try Again";

export default function GoogleLoginForm({ setErrorMessage }) {
  const { saveGoogleUser } = useAuth();

  async function handleGoogleLoginSuccess(response) {
    try {
      const token = response.credential;

      const backendResponse = await apiBaseUrl.post("/auth/google-login", {
        token,
      });

      saveGoogleUser(backendResponse);
    } catch (error) {
      setErrorMessage(loginErrorMessage);
      console.log(error);
    }
  }

  return (
    <div>
      <GoogleLogin
        onSuccess={handleGoogleLoginSuccess}
        onError={() => {
          setErrorMessage(loginErrorMessage);
        }}
      />
    </div>
  );
}
