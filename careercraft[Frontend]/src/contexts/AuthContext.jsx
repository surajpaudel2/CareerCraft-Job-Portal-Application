import { createContext, useContext, useReducer } from "react";
import apiBaseUrl from "../api/apiBaseUrl";

/*
  Reducer function for managing authentication state.
  Handles actions like LOGIN, LOGIN_FAILED, and LOGOUT to update the state accordingly.
  - LOGIN: Updates the state with user details and sets isAuthenticated to true.
  - LOGIN_FAILED: Resets the state to unauthenticated.
  - LOGOUT: Resets the user and isAuthenticated state.
*/
function reducer(state, action) {
  switch (action.type) {
    case "LOGIN":
      return { ...state, user: action.payload, isAuthenticated: true };
    case "LOGIN_FAILED":
      return { ...state, user: null, isAuthenticated: false };
    case "LOGOUT":
      return { ...state, user: null, isAuthenticated: false };
    default:
      throw new Error("Problem with authentication");
  }
}

// Create a context for managing authentication-related data and functions.
const AuthContext = createContext();

/*
  AuthProvider component wraps the application and provides authentication-related state
  and functions to its children via AuthContext.
*/
export default function AuthProvider({ children }) {
  // Initial state for authentication.
  const initialState = {
    isAuthenticated: false,
    user: null,
  };

  // useReducer hook for managing complex state logic related to authentication.
  const [{ isAuthenticated, user }, dispatch] = useReducer(
    reducer,
    initialState
  );

  /*
    Function to handle successful login.
    - Updates the state with user data and sets isAuthenticated to true.
    - Stores the JWT token in localStorage for future API requests.
  */
  function handleLoginSuccess(response) {
    const { success, user, token } = response.data;

    if (success) {
      dispatch({
        type: "LOGIN",
        payload: user,
      });

      localStorage.setItem("jwtToken", token); // Store token in localStorage.
    } else {
      dispatch({
        type: "LOGIN_FAILED",
      });
    }
  }

  /*
    Function to handle user login through Google OAuth.
    - Reuses the `handleLoginSuccess` function for updating state and storing token.
  */
  function saveGoogleUser(response) {
    handleLoginSuccess(response);
  }

  /*
    Function to handle form-based login.
    - Sends login credentials (username/email and password) to the server.
    - On success, calls `handleLoginSuccess` to update state and store the JWT token.
    - On failure, logs detailed error messages for debugging.
  */
  async function handleLogin(usernameOrEmail, password, setProgress) {
    const formData = new URLSearchParams();
    formData.append("usernameOrEmail", usernameOrEmail);
    formData.append("password", password);

    try {
      setProgress(0);
      const response = await apiBaseUrl.post("/auth/login", formData, {
        onUploadProgress: (progressEvent) => {
          // Calculate and update progress percentage
          const progressPercentage = Math.round(
            (progressEvent.loaded * 100) / progressEvent.total
          );
          setProgress(progressPercentage); // Update progress state
        },
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        withCredentials: true, // Ensures cookies are sent with the request.
      });
      handleLoginSuccess(response);
      setProgress(100);
    } catch (error) {
      if (error.response) {
        setProgress(0);

        // Server responded with an error status code.
        console.error("Server responded with status:", error.response.status);
        console.error("Response data:", error.response.data);
      } else if (error.request) {
        // Request was sent, but no response was received.
        console.error("No response received from server:", error.request);
      } else {
        // Error occurred while setting up the request.
        console.error("Error setting up request:", error.message);
      }
    }
  }

  /*
    Function to handle logout.
    - Resets authentication state to unauthenticated.
    - Clears user data from the state.
    - Optionally, removes stored tokens (if implemented in the future).
  */
  function handleLogout() {
    dispatch({ type: "LOGOUT" });
    // Optionally, remove any stored tokens if needed.
  }

  // Function to fetch the User Details.
  async function fetchUser(usernameOrEmail) {
    try {
      const response = await apiBaseUrl.get("/user/profile", {
        params: { usernameOrEmail },
        headers: {
          Authorization: `Bearer ${localStorage.getItem("jwtToken")}`,
        },
      });
      if (response.data) {
        dispatch({ type: "LOGIN", payload: response.data });
      }
    } catch (error) {
      console.error("Error fetching user data:", error);
      if (error.response?.status === 401) handleLogout();
    }
  }

  return (
    <AuthContext.Provider
      value={{
        handleLogin, // Function for handling form login.
        handleLogout, // Function for logging out the user.
        saveGoogleUser, // Function for handling Google OAuth login.
        isAuthenticated, // Boolean indicating if the user is logged in.
        user, // User details if authenticated.
        fetchUser, // Function for fetching the user details.
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

// Custom hook for consuming the AuthContext in child components.
function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error(
      "Authentication Context is used outside the AuthContext Provider"
    );
  }
  return context;
}

export { AuthContext, useAuth };
