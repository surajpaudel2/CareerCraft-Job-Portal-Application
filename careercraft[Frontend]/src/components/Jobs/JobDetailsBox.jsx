export default function JobDetailsBox({
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
    <div className="max-w-4xl p-6 bg-white border border-teal-500 rounded-lg shadow-md min-h-screen flex-grow-0">
      <div className="flex items-start gap-4 mb-6">
        {logoUrl && (
          <div className="h-16 w-16 flex-shrink-0">
            <img
              src={logoUrl}
              alt={`${companyName} Logo`}
              className="rounded-full h-full w-full object-cover border border-gray-200"
            />
          </div>
        )}
        <div>
          <h1 className="text-2xl font-bold text-teal-600">{jobTitle}</h1>
          <h2 className="text-lg text-gray-700 dark:text-gray-300">
            {companyName}
          </h2>
          <div className="flex items-center text-sm text-gray-500 mt-1">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              strokeWidth={1.5}
              stroke="currentColor"
              className="w-4 h-4 mr-1"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="M12 2.25c3.728 0 6.75 2.947 6.75 6.582 0 4.876-6.75 12.918-6.75 12.918s-6.75-8.042-6.75-12.918C5.25 5.197 8.272 2.25 12 2.25z"
              />
            </svg>
            {location}
          </div>
        </div>
      </div>

      <div className="mb-6">
        <h3 className="text-lg font-semibold text-gray-800 dark:text-white mb-2">
          About the Job
        </h3>
        <p className="text-gray-700 dark:text-gray-400">{description}</p>
      </div>

      {jobTypes && jobTypes.length > 0 && (
        <div className="mb-6">
          <h3 className="text-lg font-semibold text-gray-800 dark:text-white mb-2">
            Job Types
          </h3>
          <div className="flex flex-wrap gap-2">
            {jobTypes.map((type, index) => (
              <span
                key={index}
                className="inline-block bg-teal-100 text-teal-800 text-xs font-medium px-3 py-1 rounded-full"
              >
                {type}
              </span>
            ))}
          </div>
        </div>
      )}

      {requirements && requirements.length > 0 && (
        <div className="mb-6">
          <h3 className="text-lg font-semibold text-gray-800 dark:text-white mb-2">
            Requirements
          </h3>
          <ul className="list-disc list-inside text-gray-700 dark:text-gray-400">
            {requirements.map((req, index) => (
              <li key={index}>{req}</li>
            ))}
          </ul>
        </div>
      )}

      <div className="flex gap-4 mt-6">
        <button className="px-4 py-2 bg-teal-600 text-white font-semibold rounded-lg shadow hover:bg-teal-700">
          View or Apply for Job
        </button>
        <button className="px-4 py-2 border border-teal-600 text-teal-600 font-semibold rounded-lg hover:bg-teal-100">
          Save Job
        </button>
      </div>
    </div>
  );
}
