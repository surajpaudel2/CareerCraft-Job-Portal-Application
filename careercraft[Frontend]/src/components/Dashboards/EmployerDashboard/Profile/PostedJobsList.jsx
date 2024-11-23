import { useEmployer } from "../../../../contexts/EmployerContext";

/*
  Component that is responsible show the list of jobs that are posted in the compoany profile.
*/
export default function PostedJobsList() {
  const { companyDetails } = useEmployer();
  const { postedJobs } = companyDetails;

  return (
    <div className="mt-8">
      <h3 className="text-xl font-semibold text-teal-600 text-center">
        Posted Jobs
      </h3>
      <ul className="mt-4 space-y-2">
        {postedJobs && postedJobs.length > 0 ? (
          postedJobs.map((job) => (
            <li
              key={job.id}
              className="p-4 bg-gray-100 rounded-lg shadow-sm hover:bg-teal-50 transition duration-200"
            >
              <p className="font-medium text-gray-800">{job.title}</p>
              <p className="text-sm text-gray-600">Location: {job.location}</p>
              <p className="text-sm text-gray-600">Status: {job.status}</p>
            </li>
          ))
        ) : (
          <p className="text-gray-600 text-center">No jobs posted yet.</p>
        )}
      </ul>
    </div>
  );
}
