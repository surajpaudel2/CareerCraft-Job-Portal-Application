import { useState, useEffect, useRef, useCallback } from "react";
import { useJobs } from "../../contexts/JobContext";
import SearchForm from "../LandingPage/SearchForm";
import { useLocation, useNavigate } from "react-router-dom";
import jobSearchData from "./jobSearchDefaultData";
import JobCard from "./JobCard";
import JobDetailsBox from "./JobDetailsBox";

export default function Jobs() {
  const location = useLocation();
  const [formData, setFormData] = useState(jobSearchData);
  const { jobs, fetchJobs } = useJobs();

  console.log(jobs);

  useEffect(() => {
    setFormData(
      location.state === null ? jobSearchData : location.state.formData
    );
  }, [location.state]);

  useEffect(() => {
    async function fetchJobsData() {
      try {
        await fetchJobs(formData);
      } catch (error) {
        console.log(error);
      }
    }

    fetchJobsData();
  }, [formData]);

  const jj = jobs[0];

  return (
    <>
      <SearchForm />
      {jobs.length < 1 ? (
        <h2>No jobs available</h2>
      ) : (
        <div className="flex justify-center mt-8">
          <div className="flex gap-10">
            <div className="flex flex-col gap-5 p-4">
              {jobs.map((job, ind) => (
                <JobCard
                  key={ind}
                  jobTitle={job["title"]}
                  companyName={job["companyName"]}
                  logoUrl={job["logoUrl"]}
                  description={job["description"]}
                  location={job["location"]}
                  salary={job["salary"]}
                  jobTypes={job["jobTypes"]}
                  requirements={job["requirements"]}
                />
              ))}
            </div>
            <JobDetailsBox
              jobTitle={jj["title"]}
              companyName={jj["companyName"]}
              description={jj["description"]}
            />
          </div>
        </div>
      )}
    </>
  );
}
