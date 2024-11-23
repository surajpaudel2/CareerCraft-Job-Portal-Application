import { useEffect, useState } from "react";
import CongratulationsMessage from "../utilities/CongratulationsMessage";
import { useLocation } from "react-router-dom";
import Input from "../Forms/Input";
import apiBaseUrl from "../../api/apiBaseUrl";

/*
  TODO : Copy the entire code below to show the registration[Congratulations] message when user is registered.
    const location = useLocation();
  const [showCongratulations, setShowCongratulations] = useState(false);

  useEffect(() => {
    if (location.state?.isNewlyVerified) {
      setShowCongratulations(true);
      const timer = setTimeout(() => {
        setShowCongratulations(false);
      }, 5000);

      return () => clearTimeout(timer);
    }
  }, [location.state]);

  return (
    <>
      {showCongratulations && <CongratulationsMessage />}
      <h1>This is Hero Section</h1>;
    </>
  );
*/

export default function LandingPage() {
  const searchJobs = async () => {
    // Define the search request DTO as an object
    const searchRequestDto = {
      title: "Java", // Title filter
      location: null, // Location filter
      minSalary: null, // Minimum salary
      maxSalary: null, // Maximum salary
      postedAfter: null, // Posted after date
      page: 0, // Page number for pagination
      size: 10, // Page size for pagination
    };

    try {
      // Send request body in a POST request
      const response = await apiBaseUrl.post("/auth/search", searchRequestDto);

      // Log the response to verify the data
      console.log("Job Search Response:", response.data);

      // Use the response data as needed
      return response.data;
    } catch (error) {
      console.error("Error fetching job search results:", error);
      throw error;
    }
  };

  searchJobs();

  searchJobs();

  function handleOnSearch() {}

  return (
    <div className="bg-teal-50 min-h-screen">
      {/* Hero Section with Search */}
      <section className="relative bg-teal-600 text-white text-center py-20 px-10">
        <div className="max-w-4xl mx-auto">
          <h1 className="text-5xl font-bold mb-6">
            Find Your Dream Job on CareerCraft
          </h1>
          <p className="text-lg mb-8">
            Search through thousands of job postings and find your next
            opportunity today!
          </p>

          {/* Job Search Form */}
          <div className="bg-slate-100  rounded-lg shadow-md p-6 flex flex-col md:flex-row gap-4 justify-between items-center max-w-3xl mx-auto">
            {/* <input
              type="text"
              placeholder="Job title, skills, or company"
              className="flex-grow p-3 border rounded-lg text-gray-700 focus:outline-teal-500"
            /> */}
            <Input
              type="text"
              name="job"
              id="job"
              value=""
              placeholder="Job title, skills, or company"
            />
            <Input
              type="text"
              name="location"
              id="location"
              value=""
              placeholder="City, state, or remote"
            />
            <button className="bg-teal-600 hover:bg-teal-700 text-white py-3 px-6 rounded-lg font-semibold transition">
              Search
            </button>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-20 px-10">
        <div className="max-w-6xl mx-auto text-center">
          <h2 className="text-3xl font-bold mb-10">Why Choose CareerCraft?</h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div className="bg-white shadow-lg rounded-lg p-6">
              <h3 className="text-xl font-semibold mb-4">For Job Seekers</h3>
              <p className="text-gray-600">
                Discover the latest job openings, save jobs, and apply
                effortlessly to kickstart your career.
              </p>
            </div>
            <div className="bg-white shadow-lg rounded-lg p-6">
              <h3 className="text-xl font-semibold mb-4">For Employers</h3>
              <p className="text-gray-600">
                Post jobs, manage applicants, and find the best talent to grow
                your business.
              </p>
            </div>
            <div className="bg-white shadow-lg rounded-lg p-6">
              <h3 className="text-xl font-semibold mb-4">Easy to Use</h3>
              <p className="text-gray-600">
                Enjoy a seamless user experience with intuitive navigation and
                robust features.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Call-to-Action Section */}
      <section className="bg-teal-100 py-20 px-10">
        <div className="max-w-4xl mx-auto text-center">
          <h2 className="text-3xl font-bold mb-6">
            Ready to Take the Next Step?
          </h2>
          <p className="text-lg mb-8">
            Join thousands of users who have found success with CareerCraft.
          </p>
          <button className="bg-teal-600 hover:bg-teal-500 text-white py-3 px-6 rounded-lg font-semibold shadow-md transition">
            Join Now
          </button>
        </div>
      </section>
    </div>
  );
}
