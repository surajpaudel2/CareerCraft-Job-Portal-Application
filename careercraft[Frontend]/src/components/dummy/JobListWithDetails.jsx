import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import apiBaseUrl from "../../api/apiBaseUrl";

export default function JobListWithDetails() {
  const location = useLocation();
  const filters = location.state;
  const [availableJobs, setAvailableJobs] = useState([]);

  useEffect(() => {
    async function fetchJobs() {
      try {
        const response = await apiBaseUrl.post("/job/search", filters);
        const { content: jobs } = response.data;
        setAvailableJobs(jobs);
        console.log(jobs); // Log the actual data fetched
      } catch (error) {
        console.error(error);
      }
    }

    // Ensure filters is not empty or undefined before making the call
    if (Object.keys(filters).length > 0) {
      fetchJobs();
    }
  }, [filters]);

  const jobs = [
    {
      id: 1,
      title: "Chief Software Engineer",
      company: "Commonwealth Bank - Retail Banking Services",
      location: "Eveleigh, Sydney NSW",
      industry:
        "Engineering - Software (Information & Communication Technology)",
      description:
        "As a Chief Engineer, you'll play a key leadership role in guiding technical strategy and driving solutions for the program's most complex challenges.",
      jobType: "Full-time",
      postedAt: "1h ago",
      salary: "Add expected salary to your profile for insights",
      featured: true,
      logo: "https://via.placeholder.com/50", // Placeholder for logo
    },
    {
      id: 2,
      title: "Senior Software Engineer",
      company: "Co Talent IT Recruitment Pty Ltd",
      location: "Sydney NSW",
      industry:
        "Engineering - Software (Information & Communication Technology)",
      description:
        "We are looking for engineers who want to work with AI and build cutting-edge software products.",
      jobType: "Full-time",
      postedAt: "3h ago",
      salary: "Up to $185k + Super",
      featured: false,
      logo: "https://via.placeholder.com/50", // Placeholder for logo
    },
  ];

  const selectedJob = jobs[0]; // Example: Pre-selecting the first job for details

  return (
    <div className="flex flex-col md:flex-row gap-6 bg-gray-50 min-h-screen p-6">
      {/* Job List */}
      <div className="w-full md:w-1/3">
        <h1 className="text-2xl font-bold text-teal-700 mb-4">
          {jobs.length} jobs
        </h1>
        <div className="flex flex-col gap-4">
          {jobs.map((job) => (
            <div
              key={job.id}
              className={`bg-white rounded-lg shadow-md p-4 border ${
                job.featured ? "border-blue-500" : ""
              } hover:shadow-lg transition duration-300`}
            >
              <div className="flex items-center gap-4 mb-4">
                {/* Company Logo */}
                <img
                  src={job.logo}
                  alt={`${job.company} logo`}
                  className="w-12 h-12"
                />
                <div>
                  {job.featured && (
                    <span className="bg-purple-100 text-purple-700 text-xs font-semibold px-2 py-1 rounded-full">
                      Featured
                    </span>
                  )}
                </div>
              </div>

              {/* Job Title */}
              <h3 className="text-lg font-bold text-teal-700 mb-1">
                {job.title}
              </h3>

              {/* Company Name */}
              <p className="text-sm text-gray-700 font-medium">{job.company}</p>

              {/* Location */}
              <p className="text-sm text-gray-500">{job.location}</p>

              {/* Industry */}
              <p className="text-sm text-gray-500">{job.industry}</p>

              {/* Apply Button */}
              <div className="mt-4">
                <button className="bg-pink-600 text-white px-4 py-2 rounded-md font-semibold hover:bg-pink-500 transition">
                  Apply
                </button>
                <button className="ml-4 bg-gray-200 px-4 py-2 rounded-md font-semibold hover:bg-gray-300 transition">
                  Save
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Job Details */}
      <div className="w-full md:w-2/3 bg-white rounded-lg shadow-md p-6">
        <h2 className="text-xl font-bold text-teal-700 mb-2">
          {selectedJob.title}
        </h2>
        <p className="text-gray-700 font-medium">{selectedJob.company}</p>
        <p className="text-sm text-gray-500 mb-4">{selectedJob.location}</p>

        {/* Job Information */}
        <div className="flex flex-col gap-2 text-sm text-gray-500 mb-4">
          <div className="flex items-center">
            <span className="mr-2">🏢</span>
            {selectedJob.industry}
          </div>
          <div className="flex items-center">
            <span className="mr-2">⏰</span>
            {selectedJob.jobType}
          </div>
          <div className="flex items-center">
            <span className="mr-2">💰</span>
            {selectedJob.salary}
          </div>
          <div className="flex items-center">
            <span className="mr-2">📅</span>
            {selectedJob.postedAt}
          </div>
        </div>

        {/* Job Description */}
        <h3 className="text-lg font-bold text-gray-800 mb-2">About the Role</h3>
        <p className="text-gray-600">{selectedJob.description}</p>

        {/* Additional Features */}
        <div className="mt-6">
          <h4 className="text-md font-bold text-gray-800">Key Benefits</h4>
          <ul className="list-disc list-inside text-gray-600 mt-2">
            <li>Flexible working hours</li>
            <li>Remote-friendly policies</li>
            <li>Growth opportunities</li>
          </ul>
        </div>
      </div>
    </div>
  );
}
