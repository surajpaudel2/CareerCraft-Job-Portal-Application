// Creating job context

import { createContext, useContext, useState } from "react";
import apiBaseUrl from "../api/apiBaseUrl";

const JobContext = createContext();

function JobProvider({ children }) {
  const [jobs, setJobs] = useState([]);
  const [remainingJobs, setRemainingJobs] = useState();

  async function fetchJobs(data) {
    try {
      const response = await apiBaseUrl.post("/job/search", data);
      const { jobs, remainingCount } = response.data;
      console.log(jobs, " jobs");
      console.log(remainingCount, " Total Count : ");
    } catch (error) {
      console.error("Error:", error.response?.data || error.message);
    }
  }

  return (
    <JobContext.Provider value={{ fetchJobs, jobs }}>
      {children}
    </JobContext.Provider>
  );
}

function useJobs() {
  const context = useContext(JobContext);
  if (context === undefined) {
    throw new Error("useJobs must be used within an JobProvider");
  }
  return context;
}

export { JobProvider, useJobs };
