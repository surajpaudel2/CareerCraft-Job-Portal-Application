import Logo from "../../utilities/Logo";
import FormDivider from "../FormDivider";
import GoogleLoginForm from "../GoogleLoginForm";
import FormButton from "../FormButton";
import { useReducer, useState } from "react";
import apiBaseUrl from "../../../api/apiBaseUrl";
import FormField from "../FormField";
import { Alert, Progress } from "flowbite-react";
import { useNavigate } from "react-router-dom";
import userSignUpValidation from "../../../validations/UserSignupVladation";
import {
  completeFormUpload,
  formUploadProgress,
  startFormUpload,
} from "../../../utils/formUploadProgress";

// Initial value and error messages for each field for the form.
const initialState = {
  fullName: { value: "", error: "" },
  username: { value: "", error: "" },
  email: { value: "", error: "" },
  password: { value: "", error: "" },
  confirmPassword: { value: "", error: "" },
};

// Value of the state (fullname, username, email, password, confirmPassword) will be changed with the help of these action.
const ACTION_TYPES = {
  CHANGE: "CHANGE",
  VALIDATE: "VALIDATE",
  RESET: "RESET",
};

// Form Fields Configuration
/*
  This array contains the configuration for each field in the form.
  Each object represents a single form field and includes:
  - `label`: The text to display as the label for the field.
  - `type`: The HTML input type (e.g., text, email, password).
  - `name`: The name attribute for the input element, used to identify the field in form data.
  - `placeholder`: A placeholder text that provides guidance on what to input.

  This structure allows for dynamic rendering of form fields, making the form reusable and easy to maintain.
*/
const formFields = [
  {
    label: "Full Name",
    type: "text",
    name: "fullName",
    placeholder: "John Doe",
  },
  {
    label: "Username",
    type: "text",
    name: "username",
    placeholder: "john_doe",
  },
  {
    label: "Email",
    type: "email",
    name: "email",
    placeholder: "john@example.com",
  },
  {
    label: "Password",
    type: "password",
    name: "password",
    placeholder: "••••••••",
  },
  {
    label: "Confirm Password",
    type: "password",
    name: "confirmPassword",
    placeholder: "••••••••",
  },
];

/*
  This reducer function is responsible for managing the state of the user form data.

  Key Features:
  A) When Change Occurs in any of the field of the form
  -----------------------------------------
    ->Copies the existing state and updates only the specific field where the event was triggered.
  -> After updating the specific field, it returns the updated state, ensuring no data is lost in other fields.

  B) When On Blur Event is triggered by and of the textfield in the form 
  --------------------------------------------
    -> When on blur event is triggered handleOnBlur() function will be called and that function will call userSignUp() function to check the validation of the current textfied, where it will return error message if any validation error occurs. 
    -> Copies the existing state and updates only the specific field error if the error occured where the event was triggered due to which in ui error will be reflected if occured in the particular textfield.

  C) When action will have ACTION_TYPES.RESET value
  -------------------------------------------------------
    -> Simply all the textfield values will be reset to blank values i.e. state have value of the initial state that we set.
*/
function reducer(state, action) {
  const { field, value, error } = action;

  switch (action.type) {
    case ACTION_TYPES.CHANGE:
      return {
        ...state,
        [field]: { value, error: "" },
      };
    case ACTION_TYPES.VALIDATE:
      return {
        ...state,
        [field]: { ...state[field], error },
      };
    case ACTION_TYPES.RESET:
      return { ...initialState };
    default:
      return state;
  }
}

export default function Signup() {
  const [state, dispatch] = useReducer(reducer, initialState); //useReducer() for all the form data.
  const [fromKey, setFormKey] = useState(0);
  const [errorMessage, setErrorMessage] = useState(""); // State responsible for displaying the error message if any error occurs.

  const [progress, setProgress] = useState(0); // State to manage the progress bar value, initial value is 0.
  const [loading, setLoading] = useState(false); // State to manage the progress bar while submitting the form.

  const navigate = useNavigate();

  /*Function responsible for changing the data of the textfield, if any change occurs. 
      -> It takes event object and extract the value name and value from the event object and calls the dispatch function returened by the useReducer() to change the values of the state object.
  */
  function handleOnChange(event) {
    const { name, value } = event.target;

    dispatch({
      type: ACTION_TYPES.CHANGE,
      field: name,
      value: value,
    });
  }

  /*
    -> This function is responsible for capturing the event if the focus is lost from the textfied. 
    -> Extrct the name and value from the event object and validate the textfield data using the userSignUpValidation() form and if any error got occured, calls the dispatch() function provided by userReducer() hook in order to reflect the error message in the UI.
  */
  function handleOnBlur(event) {
    const { name, value } = event.target;
    const error = userSignUpValidation(name, value, state);
    if (error) {
      dispatch({
        type: ACTION_TYPES.VALIDATE,
        field: name,
        error,
      });
    }
  }

  /*
    -> This function is responsible for registering the user by sending the data in the form of json object by using the axios.
      -> First it will check for the all the fields data one more time by checking whether all the data entered by the user is valid or not. 
      -> If the data are valid prepares the form data and use apiBaseUrl(Internally using the axios) to send the data to the server.
  */
  async function handleSubmit(event) {
    event.preventDefault();
    let isValid = true;

    // Validate each field one last time before submitting
    for (const field in state) {
      const error = userSignUpValidation(field, state[field].value, state);
      if (error) {
        isValid = false;
        dispatch({
          type: ACTION_TYPES.VALIDATE,
          field,
          error,
        });
      }
    }

    if (isValid) {
      // Form is valid; prepare the form data
      const formData = {
        fullName: state.fullName.value,
        username: state.username.value,
        email: state.email.value,
        password: state.password.value,
      };

      startFormUpload(setLoading, setProgress); // Function that will set the initial value for the loading state and the progress state in order to start the progress bar animation

      // Submit data using axios with progress tracking
      apiBaseUrl
        .post("/auth/signup", formData, {
          onUploadProgress: (progressEvent) => {
            formUploadProgress(progressEvent, setProgress);
          },
        })
        .then(() => {
          completeFormUpload(setLoading, setProgress); // Function that is responsible to complete the progress bar animation and responsible to set setLoading and setProgress to the initial value

          const email = state.email.value;

          setFormKey((prev) => prev + 1); // Reset form
          dispatch({ type: ACTION_TYPES.RESET }); // Clear state
          navigate("/otp-verification", { state: { email: email } }); // Redirect
        })
        .catch((error) => {
          completeFormUpload(setLoading, setProgress);

          if (error.response) {
            const { message } = error.response.data;
            setErrorMessage(message);
          } else {
            setErrorMessage("Sign up failed. Please try again later.");
          }
        });
    } else {
      setErrorMessage("Form contains errors.");
    }
  }

  return (
    <section className="py-4 md:py-8 dark:bg-gray-800">
      <div className="flex flex-col items-center justify-center px-6 py-8 mx-auto md:h-screen lg:py-0">
        <Logo />
        <div className="w-full bg-white rounded-lg shadow dark:border sm:max-w-md xl:p-0 dark:bg-gray-800 dark:border-gray-700">
          <div className="p-6 space-y-4 md:space-y-6 sm:p-8">
            {loading && <Progress progress={progress} color="teal" />}

            {errorMessage && <Alert color="failure"> {errorMessage}</Alert>}

            <h1 className="text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl dark:text-white">
              Create your account
            </h1>

            {/* Google Sign-Up Button */}
            <GoogleLoginForm />
            {/* Divider */}
            <FormDivider />
            {/* Sign-Up Form */}
            <form
              key={fromKey}
              className="space-y-4 md:space-y-6"
              method="POST"
              onSubmit={handleSubmit}
            >
              {formFields.map((field) => {
                return (
                  <FormField
                    key={field.name}
                    label={field.label}
                    type={field.type}
                    name={field.name}
                    placeholder={field.placeholder}
                    value={state[field.name].value}
                    error={state[field.name].error}
                    handleOnChange={handleOnChange}
                    handleOnBlur={handleOnBlur}
                  />
                );
              })}
              <FormButton name="Sign Up" />

              <SignInPrompt
                message="Already have an account?"
                linkText="Sign in"
                linkHref="/login"
              />
            </form>
          </div>
        </div>
      </div>
    </section>
  );
}

function SignInPrompt({ message, linkText, linkHref = "#" }) {
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
