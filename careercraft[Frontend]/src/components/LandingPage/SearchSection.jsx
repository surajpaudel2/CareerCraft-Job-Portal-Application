import Input from "../Forms/Input";

export default function SearchSection({
  filters,
  handleOnChange,
  handleSearch,
}) {
  return (
    <section className="bg-teal-600 text-white py-20 px-10">
      <div className="max-w-5xl mx-auto">
        <h1 className="text-4xl font-bold text-center mb-10">
          Find Your Dream Job
        </h1>
        <div className="bg-white rounded-lg shadow-lg p-6 flex flex-wrap items-center gap-4">
          {/* What Input */}
          <div className="flex flex-col flex-grow">
            <label
              htmlFor="title"
              className="text-sm text-teal-700 font-semibold"
            >
              What
            </label>
            <Input
              type="text"
              id="title"
              name="title"
              placeholder="Job title, skills, or company"
              value={filters.title}
              handleOnChange={handleOnChange}
              className="p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-teal-500"
            />
          </div>

          {/* Where Input */}
          <div className="flex flex-col flex-grow">
            <label
              htmlFor="location"
              className="text-sm text-teal-700 font-semibold"
            >
              Where
            </label>
            <Input
              type="text"
              id="location"
              name="location"
              placeholder="Enter suburb, city, or region"
              value={filters.location}
              handleOnChange={handleOnChange}
              className="p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-teal-500"
            />
          </div>

          {/* Find jobs button */}
          <button
            className="bg-teal-700 text-white px-8 py-3 rounded-md font-semibold hover:bg-teal-800 transition mt-5"
            onClick={handleSearch}
          >
            Find Jobs
          </button>
        </div>

        {/* Filters */}
        <div className="mt-6 flex flex-wrap gap-4 justify-center text-white">
          <select
            name="workType"
            value={filters.workType}
            onChange={handleOnChange}
            className="bg-teal-700 text-white py-2 px-4 rounded-md focus:outline-none focus:ring-2 focus:ring-teal-500"
          >
            <option>All work types</option>
            <option>Full-time</option>
            <option>Part-time</option>
            <option>Contract</option>
          </select>
          <select
            name="minSalary"
            value={filters.minSalary}
            onChange={handleOnChange}
            className="bg-teal-700 text-white py-2 px-4 rounded-md focus:outline-none focus:ring-2 focus:ring-teal-500"
          >
            <option value={0}>Paying $0</option>
            <option value={50000}>$50,000+</option>
            <option value={100000}>$100,000+</option>
          </select>
          <select
            name="maxSalary"
            value={filters.maxSalary}
            onChange={handleOnChange}
            className="bg-teal-700 text-white py-2 px-4 rounded-md focus:outline-none focus:ring-2 focus:ring-teal-500"
          >
            <option value={350000}>to $350K+</option>
            <option value={250000}>to $250K+</option>
            <option value={150000}>to $150K+</option>
          </select>
          <select
            name="listedAfter"
            value={filters.listedAfter}
            onChange={handleOnChange}
            className="bg-teal-700 text-white py-2 px-4 rounded-md focus:outline-none focus:ring-2 focus:ring-teal-500"
          >
            <option>listed any time</option>
            <option value="1">1 Day Ago</option>
            <option value="7">7 Days Ago</option>
            <option value="30">30 Days Ago</option>
          </select>
        </div>
      </div>
    </section>
  );
}
