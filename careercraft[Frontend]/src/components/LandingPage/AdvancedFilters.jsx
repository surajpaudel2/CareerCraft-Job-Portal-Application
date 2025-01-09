const AdvancedFilters = ({ formData, setFormData }) => {
  const handleCheckboxChange = (e) => {
    const { value, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      workType: checked
        ? [...prev.workType, value]
        : prev.workType.filter((t) => t !== value),
    }));
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

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

  return (
    <div className="mt-6 bg-white p-4 rounded-lg shadow">
      <div className="grid grid-cols-1 lg:grid-cols-6 gap-6">
        {/* Job Type */}
        <div className="lg:col-span-6">
          <label className="block text-sm font-bold text-gray-700 mb-2">
            Job Type
          </label>
          <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-4">
            {jobTypes.map((type) => (
              <label key={type} className="flex items-center space-x-2 text-sm">
                <input
                  type="checkbox"
                  value={type}
                  checked={formData.workType.includes(type)}
                  onChange={handleCheckboxChange}
                  className="h-4 w-4 text-teal-600 border-gray-300 rounded focus:ring-teal-500"
                />
                <span>{type}</span>
              </label>
            ))}
          </div>
        </div>

        {/* Posted After */}
        <div className="lg:col-span-2">
          <label className="block text-sm font-bold text-gray-700 mb-2">
            Posted After
          </label>
          <input
            type="date"
            name="postedAfter"
            value={formData.postedAfter || ""}
            onChange={handleInputChange}
            className="w-full p-2 border border-gray-300 rounded-md text-sm focus:ring-teal-500 focus:border-teal-500"
          />
        </div>

        {/* Min Salary */}
        <div className="lg:col-span-2">
          <label className="block text-sm font-bold text-gray-700 mb-2">
            Min Salary
          </label>
          <input
            type="number"
            name="minSalary"
            value={formData.minSalary || ""}
            onChange={handleInputChange}
            placeholder="e.g., 20000"
            className="w-full p-2 border border-gray-300 rounded-md text-sm focus:ring-teal-500 focus:border-teal-500"
          />
        </div>

        {/* Max Salary */}
        <div className="lg:col-span-2">
          <label className="block text-sm font-bold text-gray-700 mb-2">
            Max Salary
          </label>
          <input
            type="number"
            name="maxSalary"
            value={formData.maxSalary || ""}
            onChange={handleInputChange}
            placeholder="e.g., 100000"
            className="w-full p-2 border border-gray-300 rounded-md text-sm focus:ring-teal-500 focus:border-teal-500"
          />
        </div>
      </div>
    </div>
  );
};

export default AdvancedFilters;
