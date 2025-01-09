export default function JobCard({
  logoUrl,
  companyName,
  jobTitle,
  description,
  location,
  salary,
  jobTypes,
  requirements,
}) {
  return (
    <div className="max-w-md p-6 bg-white border border-gray-200 rounded-lg shadow-md hover:shadow-lg dark:bg-gray-800 dark:border-gray-700 transition-shadow">
      <JobHeader
        logoUrl={logoUrl}
        companyName={companyName}
        jobTitle={jobTitle}
      />
      <JobDetails location={location} salary={salary} />
      <JobDescription description={description} />
      {jobTypes && jobTypes.length > 0 && <JobTypes jobTypes={jobTypes} />}
      {requirements && requirements.length > 0 && (
        <JobRequirements requirements={requirements} />
      )}
    </div>
  );
}

function JobHeader({ logoUrl, companyName, jobTitle }) {
  return (
    <div className="flex items-start gap-4 mb-4">
      {logoUrl && (
        <div className="h-16 w-16 flex-shrink-0">
          <img
            src={logoUrl}
            alt={`${companyName} Logo`}
            className="rounded-full h-full w-full object-cover"
          />
        </div>
      )}
      <div>
        <h5 className="text-xl font-semibold tracking-tight text-gray-900 dark:text-white">
          {jobTitle}
        </h5>
        <span className="text-sm text-gray-600 dark:text-gray-400">
          {companyName}
        </span>
      </div>
    </div>
  );
}

function JobDetails({ location, salary }) {
  return (
    <div className="flex justify-between items-center text-gray-700 dark:text-gray-400 mb-3">
      <span className="flex items-center gap-2">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          strokeWidth={1.5}
          stroke="currentColor"
          className="w-5 h-5"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            d="M12 2.25c3.728 0 6.75 2.947 6.75 6.582 0 4.876-6.75 12.918-6.75 12.918s-6.75-8.042-6.75-12.918C5.25 5.197 8.272 2.25 12 2.25z"
          />
        </svg>
        {location}
      </span>
      <span className="font-medium text-green-500 dark:text-green-400">
        {salary ? `$${salary}` : "Not specified"}
      </span>
    </div>
  );
}

function JobDescription({ description }) {
  return (
    <p className="mb-4 text-sm text-gray-500 dark:text-gray-400">
      {description.length > 100
        ? description.slice(0, 100) + "..."
        : description}
    </p>
  );
}

function JobTypes({ jobTypes }) {
  return (
    <div className="mb-4">
      <h6 className="text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
        Job Types:
      </h6>
      <div className="flex flex-wrap gap-2">
        {jobTypes.map((type, index) => (
          <span
            key={index}
            className="inline-block bg-blue-100 text-blue-800 text-xs font-medium mr-2 px-3 py-1 rounded-full dark:bg-blue-900 dark:text-blue-300"
          >
            {type}
          </span>
        ))}
      </div>
    </div>
  );
}

function JobRequirements({ requirements }) {
  return (
    <div className="mb-4">
      <h6 className="text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
        Requirements:
      </h6>
      <ul className="list-disc list-inside text-sm text-gray-500 dark:text-gray-400">
        {requirements.slice(0, 3).map((req, index) => (
          <li key={index}>{req}</li>
        ))}
        {requirements.length > 3 && <li>And more...</li>}
      </ul>
    </div>
  );
}
