import { HiPlus } from "react-icons/hi";
import { useState } from "react";
import JobPostingsTable from "./JobPostingsTable";
import JobForm from "../../../Forms/JobForm/JobForm";
import { useLocation, useNavigate } from "react-router-dom";

export default function JobPostings() {
  const [activeJobForm, setActiveJobForm] = useState(false); // State responsible to display the job form to show or not.
  const [successMessage, setSuccessMessage] = useState(null);

  const location = useLocation();
  const navigate = useNavigate();

  function handleClickOnAddNewJob() {
    navigate(location.pathname, { replace: true, state: {} });
    setActiveJobForm(true);
  }
  function handleClickOnBackButton() {
    setActiveJobForm(false);
  }

  return (
    <div className="p-6 bg-white shadow-lg rounded-lg">
      {successMessage && (
        <p className="text-center mb-8 text-teal-400 text-2xl font-semibold">
          {successMessage}
        </p>
      )}
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-semibold text-teal-600">Job Postings</h2>

        {activeJobForm ? (
          <button
            onClick={handleClickOnBackButton}
            className="flex items-center px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 transition duration-300"
          >
            Back
          </button>
        ) : (
          <button
            onClick={handleClickOnAddNewJob}
            className="flex items-center px-4 py-2 bg-teal-600 text-white rounded-md hover:bg-teal-700 transition duration-300"
          >
            <HiPlus className="mr-2" />
            Add New Job
          </button>
        )}
      </div>

      {activeJobForm ? (
        <JobForm
          setActiveJobForm={setActiveJobForm}
          setSuccessMessage={setSuccessMessage}
        />
      ) : (
        <>
          <JobPostingsTable setActiveJobForm={setActiveJobForm} />
        </>
      )}
    </div>
  );
}
