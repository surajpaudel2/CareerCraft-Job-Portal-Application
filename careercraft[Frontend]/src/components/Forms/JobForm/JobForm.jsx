import { Alert, Progress } from "flowbite-react";
import apiBaseUrl from "../../../api/apiBaseUrl";
import { useReducer, useState, useEffect } from "react";
import FormField from "../FormField";
import FormButton from "../FormButton";
import { useAuth } from "../../../contexts/AuthContext";
import { useLocation } from "react-router-dom";
import jobFormValidation from "../../../validations/JobFormValidation";
import {
  completeFormUpload,
  formUploadProgress,
  startFormUpload,
} from "../../../utils/formUploadProgress";
import { useEmployer } from "../../../contexts/EmployerContext";

// Initial state for job form fields, containing default values and error placeholders
const initialJobState = {
  jobTitle: { value: "", error: "" },
  description: { value: "", error: "" },
  salary: { value: "", error: "" },
  location: { value: "", error: "" },
  requirements: { value: [], error: "" },
  status: { value: "Active", error: "" },
  jobType: { value: [], error: "" }, // Add initial state for jobTypes
};

// Action types for managing job form state transitions
const JOB_ACTION_TYPES = {
  CHANGE: "CHANGE", // Update a field value
  VALIDATE: "VALIDATE", // Handle validation errors for a field
  RESET: "RESET", // Reset the form to its initial state
  ADD_REQUIREMENT: "ADD_REQUIREMENT", // Add a new empty requirement input
  REMOVE_REQUIREMENT: "REMOVE_REQUIREMENT", // Remove a specific requirement by index
  UPDATE_REQUIREMENT: "UPDATE_REQUIREMENT", // Update the value of a specific requirement
};

// Static configurations for job form fields to support rendering dynamically
// Static configurations for job form fields to support rendering dynamically
const jobTypes = [
  "Full Time",
  "Part Time",
  "Temporary",
  "Contract",
  "Internship",
  "Freelance",
  "Seasonal",
  "Volunteer",
  "Per Diem",
  "On Call",
  "Remote",
  "Hybrid",
  "Gig Work",
  "Commission Based",
  "Apprenticeship",
  "Shift Work",
];

const jobOptions = ["Active", "Pending", "Closed"];

const jobFormFields = [
  {
    label: "Job Title",
    type: "text",
    name: "jobTitle",
    placeholder: "Software Engineer",
    value: "",
  },
  {
    label: "Description",
    type: "textarea",
    name: "description",
    placeholder: "Job description",
  },
  {
    label: "Salary",
    type: "number",
    name: "salary",
    placeholder: "50000",
  },
  {
    label: "Location",
    type: "text",
    name: "location",
    placeholder: "New York",
  },
  {
    label: "Status",
    type: "select",
    name: "status",
    options: jobOptions,
  },
  {
    label: "Job Types",
    type: "checkbox",
    name: "jobType",
    options: jobTypes, // Dynamically add the job types
  },
];

/*
  Reducer function to manage the state of the job form. 
  Purpose:
  - Handles field value changes, validation errors, and actions for dynamic requirements.
  Features:
  - Supports resetting the form to its initial state.
  - Manages dynamic lists of requirements (add, update, remove).
  - Ensures each field's state is updated with its value and error status.
*/
function jobReducer(state, action) {
  switch (action.type) {
    case JOB_ACTION_TYPES.CHANGE:
      console.log(state, " From the reducer function");
      return {
        ...state,
        [action.field]: { value: action.value, error: "" },
      };
    case JOB_ACTION_TYPES.VALIDATE:
      return {
        ...state,
        [action.field]: { ...state[action.field], error: action.error },
      };
    case JOB_ACTION_TYPES.ADD_REQUIREMENT:
      return {
        ...state,
        requirements: { value: [...state.requirements.value, ""] },
      };
    case JOB_ACTION_TYPES.REMOVE_REQUIREMENT:
      return {
        ...state,
        requirements: {
          value: state.requirements.value.filter(
            (_, index) => index !== action.index
          ),
        },
      };
    case JOB_ACTION_TYPES.UPDATE_REQUIREMENT:
      return {
        ...state,
        requirements: {
          value: state.requirements.value.map((req, index) =>
            index === action.index ? action.value : req
          ),
        },
      };
    case JOB_ACTION_TYPES.RESET:
      return initialJobState;
    default:
      return state;
  }
}

/*
  Component to manage the dynamic 'Job Requirements' section.
  Purpose:
  - Allows users to add, remove, and update job requirements dynamically.
  Features:
  - Each requirement is rendered as an input field.
  - Includes "Add" and "Remove" buttons to dynamically manage the requirements list.
*/
function JobRequirements({ requirements, dispatch }) {
  const handleAddRequirement = () => {
    dispatch({ type: JOB_ACTION_TYPES.ADD_REQUIREMENT });
  };

  const handleRemoveRequirement = (index) => {
    dispatch({ type: JOB_ACTION_TYPES.REMOVE_REQUIREMENT, index });
  };

  const handleUpdateRequirement = (index, value) => {
    dispatch({ type: JOB_ACTION_TYPES.UPDATE_REQUIREMENT, index, value });
  };

  return (
    <div>
      <h3 className="text-lg font-semibold mb-2">Job Requirements</h3>
      {requirements.value.map((requirement, index) => (
        <div key={index} className="flex items-center space-x-2 mb-2">
          <input
            type="text"
            value={requirement}
            onChange={(e) => handleUpdateRequirement(index, e.target.value)}
            placeholder="Enter a requirement"
            className="border p-2 rounded w-full"
          />
          <button
            type="button"
            onClick={() => handleRemoveRequirement(index)}
            className="text-red-500"
          >
            Remove
          </button>
        </div>
      ))}
      <button
        type="button"
        onClick={handleAddRequirement}
        className="bg-teal-500 text-white px-4 py-2 rounded mt-2"
      >
        Add Requirement
      </button>
    </div>
  );
}

/*
  Main JobForm component for creating or editing job posts.
  Purpose:
  - Handles job form state management, validations, and API integrations for job creation or updates.
  How it works:
  - Initializes the form state using `useReducer`.
  - Updates the form dynamically based on whether the job is being edited or added.
  - Validates inputs on blur and before submission.
  - Submits job data to the API and handles success/error responses.
  Features:
  - Dynamically loads job details if editing an existing job.
  - Manages input fields and validation for job fields and requirements.
  - Provides error handling and resets the form state on successful submission.
*/
export default function JobForm({ setActiveJobForm, setSuccessMessage }) {
  const location = useLocation();
  const [state, dispatch] = useReducer(jobReducer, initialJobState);

  const jobToEdit = location.state?.job;

  const [loading, setLoading] = useState(false);
  const [progress, setProgress] = useState(0);

  const { fetchCompanyDetails } = useEmployer();

  if (jobToEdit) {
    // Assign `title` to `jobTitle` to align with the form's state structure
    jobToEdit.jobTitle = jobToEdit.title;
  }

  // Initialize form state with jobToEdit if available, otherwise reset
  useEffect(() => {
    if (jobToEdit) {
      for (const key in jobToEdit) {
        if (key in initialJobState) {
          let value;

          // Handle specific cases for fields
          if (key === "requirements") {
            value = jobToEdit[key] || [];
          } else if (key === "status") {
            value = jobToEdit[key] || "Active"; // Default to "Active" if undefined
          } else if (key === "jobType") {
            value = Array.isArray(jobToEdit[key]) ? jobToEdit[key] : [];
          } else {
            value = jobToEdit[key] || ""; // Default to an empty string for other fields
          }

          // Dispatch the field update
          dispatch({
            type: JOB_ACTION_TYPES.CHANGE,
            field: key,
            value: value,
          });
        }
      }
    } else {
      dispatch({ type: JOB_ACTION_TYPES.RESET });
    }
  }, [jobToEdit]);

  console.log(state);

  const [errorMessage, setErrorMessage] = useState("");
  const { user } = useAuth();
  const employerEmail = user.email;

  // Handles input value changes
  function handleOnChange(event) {
    const { name, value } = event.target;
    dispatch({
      type: JOB_ACTION_TYPES.CHANGE,
      field: name,
      value,
    });
  }

  // Handles input validation when a field loses focus
  function handleOnBlur(event) {
    const { name, value } = event.target;
    const error = jobFormValidation(name, value);
    if (error) {
      dispatch({
        type: JOB_ACTION_TYPES.VALIDATE,
        field: name,
        error,
      });
    }
  }

  // Handles form submission for job creation or updates
  async function handleSubmit(event) {
    event.preventDefault();
    let isValid = true;

    // Validate all form fields before submission
    for (const field in state) {
      const error = jobFormValidation(field, state[field].value);
      if (error) {
        isValid = false;
        dispatch({ type: JOB_ACTION_TYPES.VALIDATE, field, error });
      }
    }

    if (!isValid) {
      setErrorMessage("Please fix the errors in the form.");
      return;
    }

    // Prepare the job data
    const jobData = {
      title: state.jobTitle.value,
      description: state.description.value,
      salary: parseFloat(state.salary.value),
      location: state.location.value,
      requirements: state.requirements.value.filter(Boolean), // Exclude empty requirements
      jobType: state.jobType.value,
      status: state.status.value,
      employerEmail: employerEmail,
    };

    if (jobToEdit) {
      jobData["id"] = jobToEdit["id"]; // Add ID for updates
    }

    startFormUpload(setLoading, setProgress);

    const jwtToken = localStorage.getItem("jwtToken");
    const endpoint = jobToEdit ? `/job/update` : `/job/create`;
    const method = jobToEdit ? "put" : "post"; // Use PUT for update and POST for create

    try {
      // Make the API request
      const response = await apiBaseUrl[method](endpoint, jobData, {
        headers: {
          Authorization: `Bearer ${jwtToken}`,
          "Content-Type": "application/json",
        },
        onUploadProgress: (progressEvent) => {
          // Calculate and update progress percentage
          formUploadProgress(progressEvent, setProgress);
        },
      });

      if (response.data.success) {
        completeFormUpload(setLoading, setProgress);
        await fetchCompanyDetails();
        setActiveJobForm(false); // Close the form on success
        if (jobToEdit) {
          setSuccessMessage("Job Updated Successfully!");
        } else {
          setSuccessMessage("Job Created Successfully!");
        }
      } else {
        setErrorMessage("Failed to post the job. Please try again.");
      }
    } catch (error) {
      setErrorMessage(
        error.response?.data?.message || "Job posting failed. Please try again."
      );
      console.error("Error posting job:", error);
    } finally {
      setLoading(false); // Ensure loading state is reset
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
              {jobToEdit ? "Edit Job" : "Add New Job"}
            </h1>
            <form className="space-y-4 md:space-y-6" onSubmit={handleSubmit}>
              {jobFormFields.map((field) => (
                <FormField
                  key={field.name}
                  label={field.label}
                  type={field.type}
                  name={field.name}
                  placeholder={field.placeholder}
                  value={state[field.name]?.value || ""}
                  error={state[field.name]?.error || ""}
                  options={field.options || []}
                  multiSelect={field.multiSelect || false}
                  handleOnChange={handleOnChange}
                  handleOnBlur={handleOnBlur}
                />
              ))}
              <JobRequirements
                requirements={state.requirements}
                dispatch={dispatch}
              />
              <FormButton name="Save Job" />
            </form>
          </div>
        </div>
      </div>
    </section>
  );
}
