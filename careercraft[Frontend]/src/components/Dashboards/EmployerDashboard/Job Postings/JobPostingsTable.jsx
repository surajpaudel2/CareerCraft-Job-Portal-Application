import { useEffect, useState } from "react";
import { useEmployer } from "../../../../contexts/EmployerContext";
import JobPostingRow from "./JobPostingsRow";
import { useNavigate } from "react-router-dom";
import apiBaseUrl from "../../../../api/apiBaseUrl";

export default function JobPostingsTable({ setActiveJobForm }) {
  const { companyDetails, fetchCompanyDetails } = useEmployer();
  const { postedJobs: jobs } = companyDetails;
  const [jobPostings, setJobPostings] = useState(jobs);

  const navigate = useNavigate();

  useEffect(() => {}, [companyDetails, fetchCompanyDetails]);

  async function deleteJobPosting(id) {
    const jwtToken = localStorage.getItem("jwtToken");

    try {
      // Use template literal correctly to include `id` in the query string
      const response = await apiBaseUrl.post(`/job/delete?id=${id}`, null, {
        headers: {
          Authorization: `Bearer ${jwtToken}`,
        },
      });

      const { success } = response.data;
      if (success) {
        console.log("hello");
        await fetchCompanyDetails();
      }

      // Fetch updated company details
    } catch (error) {
      console.log("Error deleting job posting:", error);
    }
  }

  /*
    Function to edit the job postings. 
    -> Takes the id and on the basis of id find the job to edit. 
    -> Fills all the requested edit job in the form
  */
  function editJobPosting(id) {
    console.log(id);
    const jobToEdit = jobs.find((job) => job.id === id);

    setActiveJobForm(true);
    navigate("/employer/job-postings", { state: { job: jobToEdit } });
  }

  return jobs && jobs.length > 0 ? (
    <table className="w-full border-collapse bg-white shadow rounded-lg">
      <thead>
        <tr className="bg-teal-100 text-teal-800">
          <th className="p-4 text-left">Job Title</th>
          <th className="p-4 text-left">Location</th>
          <th className="p-4 text-left">Status</th>
          <th className="p-4 text-center">Actions</th>
        </tr>
      </thead>
      <tbody>
        {jobPostings.map((job) => (
          <JobPostingRow
            key={job.id}
            job={job}
            onDelete={deleteJobPosting}
            onEdit={editJobPosting}
          />
        ))}
      </tbody>
    </table>
  ) : (
    <h2 className="text-center text-gray-500">No jobs available</h2>
  );
}
