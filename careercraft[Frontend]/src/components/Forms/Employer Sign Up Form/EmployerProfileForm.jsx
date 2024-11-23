import { useReducer, useState } from "react";
import apiBaseUrl from "../../../api/apiBaseUrl";
import FormField from "../FormField";
import { Alert, Progress } from "flowbite-react";
import { useNavigate } from "react-router-dom";
import FormButton from "../FormButton";
import { useAuth } from "../../../contexts/AuthContext";
import {
  completeFormUpload,
  formUploadProgress,
  startFormUpload,
} from "../../../utils/formUploadProgress";

/*
  Job form initital state where all the values and errors are blank.
*/
const initialState = {
  companyName: { value: "", error: "" },
  industry: { value: "", error: "" },
  description: { value: "", error: "" },
  location: { value: "", error: "" },
  logoFile: { value: null, error: "" },
};

// Value that will tell us what to do with the state either to handle onchange, or validate or reset.
const ACTION_TYPES = {
  CHANGE: "CHANGE",
  VALIDATE: "VALIDATE",
  RESET: "RESET",
};

/*
  This array defines the configuration for form fields used to collect company details.

    Each object in the array represents a single form field and contains the following properties:
    ->  `label`: The display label for the form field.
    ->  `type`: The input type of the field (e.g., text, textarea).
    ->  `name`: The unique name identifier for the field, used for form data submission and state management.
    ->  `placeholder`: The placeholder text displayed inside the field as a hint to the user.

*/
const formFields = [
  {
    label: "Company Name",
    type: "text",
    name: "companyName",
    placeholder: "Tech Innovations",
  },
  {
    label: "Industry",
    type: "text",
    name: "industry",
    placeholder: "Information Technology",
  },
  {
    label: "Description",
    type: "textarea",
    name: "description",
    placeholder: "A brief description of your company",
  },
  {
    label: "Location",
    type: "text",
    name: "location",
    placeholder: "New York, USA",
  },
];

function validateField(name, value) {
  switch (name) {
    case "companyName":
      if (!value) return "Company name is required";
      if (value.length > 100)
        return "Company name should not exceed 100 characters";
      break;
    case "industry":
      if (!value) return "Industry is required";
      break;
    case "description":
      if (value.length > 1000)
        return "Description should not exceed 1000 characters";
      break;
    case "location":
      if (!value) return "Location is required";
      if (value.length > 100)
        return "Location should not exceed 100 characters";
      break;
    default:
      return "";
  }
  return "";
}

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

export default function EmployerProfileForm() {
  const [state, dispatch] = useReducer(reducer, initialState);
  const [formKey, setFormKey] = useState(0);
  const [errorMessage, setErrorMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const [progress, setProgress] = useState(0);

  const { user, fetchUser } = useAuth();
  const usernameOrEmail = user["email"];

  const navigate = useNavigate();

  function handleOnChange(event) {
    const { name, value, type, files } = event.target;

    dispatch({
      type: ACTION_TYPES.CHANGE,
      field: name,
      value: type === "file" ? files[0] : value, // Use the file object for logoFile
    });
  }

  function handleOnBlur(event) {
    const { name, value } = event.target;
    const error = validateField(name, value);
    if (error) {
      dispatch({
        type: ACTION_TYPES.VALIDATE,
        field: name,
        error,
      });
    }
  }

  async function handleSubmit(event) {
    event.preventDefault(); // Prevent default form submission behavior
    let isValid = true;

    // Validate each field before submitting
    for (const field in state) {
      const error = validateField(field, state[field].value);
      if (error) {
        isValid = false;
        dispatch({
          type: ACTION_TYPES.VALIDATE,
          field,
          error,
        });
      }
    }

    if (!isValid) {
      setErrorMessage("Form contains errors.");
      return;
    }

    // Prepare the FormData
    const formData = new FormData();
    formData.append("usernameOrEmail", usernameOrEmail);
    formData.append("companyName", state.companyName.value);
    formData.append("industry", state.industry.value);
    formData.append("description", state.description.value);
    formData.append("location", state.location.value);
    if (state.logoFile.value) {
      formData.append("logoFile", state.logoFile.value); // Attach logo file
    }

    startFormUpload(setLoading, setProgress);

    try {
      // Submit the form using Axios
      const jwtToken = localStorage.getItem("jwtToken");
      const response = await apiBaseUrl.post("/employer/register", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${jwtToken}`,
        },
        onUploadProgress: (progressEvent) =>
          formUploadProgress(progressEvent, setProgress),
      });

      completeFormUpload(setLoading, setProgress);

      const { success } = response.data;

      if (success) {
        await fetchUser(user["email"]); // Allowing sequential execution after the fresh user data is fetched again.
        navigate("/employer/dashboard"); // Navigating to the employer dashboard after the successful registration of the employer profile.
        return;
      }

      // Reset form if registration fails
      setFormKey((prev) => prev + 1);
      dispatch({ type: ACTION_TYPES.RESET });
    } catch (error) {
      completeFormUpload(setLoading, setProgress);

      if (error.response) {
        const { message } = error.response.data || {};
        setErrorMessage(message || "An unknown error occurred.");
      } else {
        setErrorMessage(
          "Employer registration failed. Please try again later."
        );
      }
    }
  }

  return (
    <section className="py-4 md:py-8 dark:bg-gray-800">
      <div className="flex flex-col items-center justify-center px-6 py-8 mx-auto md:h-screen lg:py-0">
        <div className="w-full bg-white rounded-lg shadow dark:border sm:max-w-md xl:p-0 dark:bg-gray-800 dark:border-gray-700">
          <div className="p-6 space-y-4 md:space-y-6 sm:p-8">
            {loading && <Progress progress={progress} color="teal" />}

            {errorMessage && <Alert color="failure">{errorMessage}</Alert>}

            <h1 className="text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl dark:text-white">
              Register Employer Profile
            </h1>

            {/* Employer Profile Form */}
            <form
              key={formKey}
              className="space-y-4 md:space-y-6"
              method="POST"
              onSubmit={handleSubmit}
              encType="multipart/form-data" // Important for file upload
            >
              {formFields.map((field) => (
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
              ))}
              {/* Logo File Upload */}
              <div>
                <label
                  htmlFor="logoFile"
                  className="block text-sm font-medium text-gray-700"
                >
                  Logo File
                </label>
                <input
                  type="file"
                  id="logoFile"
                  name="logoFile"
                  onChange={handleOnChange}
                  className="mt-1 p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-teal-500 w-full"
                />
              </div>
              <FormButton name="Register" />
            </form>
          </div>
        </div>
      </div>
    </section>
  );
}
