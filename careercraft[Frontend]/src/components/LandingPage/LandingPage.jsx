import SearchSection from "./SearchSection.jsx";
import FooterSection from "./FooterSection";
import { useState } from "react";
import { Outlet, useNavigate } from "react-router-dom";

export default function LandingPage() {
  const [filters, setFilters] = useState({
    title: null,
    location: null,
    workType: null,
    minSalary: null,
    maxSalary: null,
    postedAfter: null,
  });

  const navigate = useNavigate();

  function handleOnChange(e) {
    const { name, value } = e.target;
    setFilters({ ...filters, [name]: value });
  }

  async function handleSearch() {
    try {
      navigate(`/jobs`, { state: filters });
    } catch (error) {
      console.error("Error during job search:", error);
    }
  }
  return (
    <div className="bg-teal-50 min-h-screen flex flex-col justify-between">
      {/* Search Section */}
      <SearchSection
        filters={filters}
        handleOnChange={handleOnChange}
        handleSearch={handleSearch}
      />

      <Outlet />

      {/* Footer Section */}
      <FooterSection />
    </div>
  );
}
