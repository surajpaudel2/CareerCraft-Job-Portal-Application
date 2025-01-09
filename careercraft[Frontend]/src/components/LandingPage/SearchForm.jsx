import { FiSearch, FiMapPin } from "react-icons/fi";
import AdvancedFilters from "./AdvancedFilters";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import jobSearchData from "../Jobs/jobSearchDefaultData";

function SearchForm() {
  const [formData, setFormData] = useState(jobSearchData);
  const navigate = useNavigate();

  const [showAdvancedFilters, setShowAdvancedFilters] = useState(false);

  function handleInputChange(e) {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  }

  function handleSubmit(e) {
    e.preventDefault();

    navigate("/jobs", { state: { formData } });
  }

  return (
    <div className="max-w-4xl mx-auto">
      <form
        onSubmit={handleSubmit}
        className="grid grid-cols-1 md:grid-cols-8 gap-4 items-center bg-white p-4 rounded-lg shadow"
      >
        {/* Job Title */}
        <div className="md:col-span-3">
          <label
            htmlFor="title"
            className="block text-sm font-medium text-gray-700 mb-1"
          >
            What
          </label>
          <div className="relative">
            <FiSearch className="absolute left-3 top-3 text-gray-400" />
            <input
              type="text"
              id="title"
              name="title"
              value={formData.title}
              onChange={handleInputChange}
              placeholder="e.g., Software Engineer"
              className="w-full pl-10 py-3 border border-gray-300 rounded-md text-sm focus:ring-teal-500 focus:border-teal-500"
            />
          </div>
        </div>

        {/* Location */}
        <div className="md:col-span-3">
          <label
            htmlFor="location"
            className="block text-sm font-medium text-gray-700 mb-1"
          >
            Where
          </label>
          <div className="relative">
            <FiMapPin className="absolute left-3 top-3 text-gray-400" />
            <input
              type="text"
              id="location"
              name="location"
              value={formData.location}
              onChange={handleInputChange}
              placeholder="e.g., City, State"
              className="w-full pl-10 py-3 border border-gray-300 rounded-md text-sm focus:ring-teal-500 focus:border-teal-500"
            />
          </div>
        </div>

        {/* Search Button */}
        <div className="md:col-span-2 flex flex-col md:flex-row items-center md:space-x-4 space-y-2 md:space-y-0">
          <button
            type="submit"
            className="flex items-center justify-center bg-teal-600 text-white px-4 py-2 rounded-md hover:bg-teal-700 focus:outline-none focus:ring-2 focus:ring-teal-500"
          >
            Search
          </button>
          <button
            type="button"
            onClick={() => setShowAdvancedFilters(!showAdvancedFilters)}
            className="text-teal-600 text-sm underline hover:text-teal-700 focus:outline-none"
          >
            {showAdvancedFilters ? "Hide Filters" : "Advanced Filters"}
          </button>
        </div>
      </form>

      {showAdvancedFilters && (
        <AdvancedFilters formData={formData} setFormData={setFormData} />
      )}
    </div>
  );
}

export default SearchForm;
