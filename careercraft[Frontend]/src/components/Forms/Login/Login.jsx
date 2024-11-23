import Logo from "../../utilities/Logo";
import ForgotPassword from "../ForgotPassword";
import FormDivider from "../FormDivider";
import GoogleLoginForm from "../GoogleLoginForm";
import Input from "../Input";
import Label from "../Label";
import RememberMe from "../RememberMe";
import FormButton from "../FormButton";
import { useAuth } from "../../../contexts/AuthContext";
import { useEffect, useState } from "react";
import { Alert, Progress } from "flowbite-react";
import useAuthError from "./hooks/useAuthError";
import { useNavigate } from "react-router-dom";

const loginErrorMessage = "Invalid username or password. Please try Again"; // Error message to be displayed if login fails.

/*
  This component is responsible to handle the login
    Key features : 
      -> Sends server the request with either username or password, 
      -> Upon the successful login redirects to the home page, if the login is failed display the error message (It uses custom hook as well to display the error message)
      -> Upon the successful login sets the "isAuthenticated" state as true and fill the user details in the "user" object of the Authentication Context, by using Authentication Context "handleLogin" method.
*/
export default function Login() {
  // Getting the isAuthenticated state and handleLogin from Authentication Context.
  const { isAuthenticated, handleLogin } = useAuth();

  const [usernameOrEmail, setUsernameOrEmail] = useState(""); // State to fetch the user data on the basis of username or email.
  const [password, setPassword] = useState("");

  // Error message for the login failed.
  const [errorMessage, setErrorMessage] = useState("");

  const [progress, setProgress] = useState(0);
  const [loading, setLoading] = useState(false);

  // Resuable hook responsible for setting the error message if user fails to login.
  useAuthError(isAuthenticated, errorMessage, setErrorMessage);

  const navigate = useNavigate();

  useEffect(() => {
    if (isAuthenticated) {
      navigate("/"); // Navigating to home page if the authentication is success, or say if the user is already logged in.
      setErrorMessage("");
    }
  }, [isAuthenticated, navigate]);

  // normal function for handling the change of textfield [EMAIL]
  function handleUsernameOrEmailOnChange(event) {
    setUsernameOrEmail(event.target.value);
  }

  // normal function for handling the change of textfield [PASSWORD]
  function handlePasswordOnChange(event) {
    setPassword(event.target.value);
  }

  /*
    This function is responsible for handling the submission of login form, by calling the handleLogin() of AuthContext. 
  */
  function handleOnFormSubmit(event) {
    event.preventDefault();
    setLoading(true);
    handleLogin(usernameOrEmail, password, setProgress);
    if (!isAuthenticated) {
      setErrorMessage(loginErrorMessage);
    }
    setLoading(false);
  }

  return (
    <>
      <section className="py-4 md:py-8 dark:bg-gray-800">
        <div className="flex flex-col items-center justify-center px-6 py-8 mx-auto md:h-screen lg:py-0">
          <Logo />
          <div className="w-full bg-white rounded-lg shadow dark:border sm:max-w-md xl:p-0 dark:bg-gray-800 dark:border-gray-700">
            <div className="p-6 space-y-4 md:space-y-6 sm:p-8">
              {loading && <Progress progress={progress} color="teal" />}

              {/* Error message to display for the failure of login */}
              {errorMessage && <Alert color="failure"> {errorMessage}</Alert>}

              <h1 className="text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl dark:text-white">
                Sign in to your account
              </h1>

              <GoogleLoginForm setErrorMessage={setErrorMessage} />
              <FormDivider />
              <form
                className="space-y-4 md:space-y-6"
                method="POST"
                onSubmit={handleOnFormSubmit}
              >
                <FormField
                  label="Username or Email"
                  type="text"
                  name="usernameOrEmail"
                  id="usernameOrEmail"
                  placeholder="Enter your Username or Email"
                  value={usernameOrEmail}
                  handleOnChange={handleUsernameOrEmailOnChange}
                />
                <FormField
                  label="Password"
                  type="password"
                  name="password"
                  id="password"
                  placeholder="Enter your password"
                  value={password}
                  handleOnChange={handlePasswordOnChange}
                />
                <FormActions />
                <FormButton name="Log In" />

                <SignUpPrompt
                  message="Don’t have an account yet?"
                  linkText="Sign up"
                  linkHref="/signup"
                />
              </form>
            </div>
          </div>
        </div>
      </section>
    </>
  );
}

function FormField({
  label,
  type,
  name,
  id,
  placeholder,
  value,
  handleOnChange,
}) {
  return (
    <div>
      <Label htmlFor={id} title={label} />
      <Input
        type={type}
        name={name}
        id={id}
        placeholder={placeholder}
        value={value}
        handleOnChange={handleOnChange}
      />
    </div>
  );
}

function FormActions() {
  return (
    <div className="flex items-center justify-between">
      <RememberMe />
      <ForgotPassword />
    </div>
  );
}

function SignUpPrompt({ message, linkText, linkHref = "#" }) {
  return (
    <p className="text-sm font-light text-gray-500 dark:text-gray-400">
      {message}{" "}
      <a
        href={linkHref}
        className="font-medium text-teal-600 hover:underline dark:text-teal-500"
      >
        {linkText}
      </a>
    </p>
  );
}
