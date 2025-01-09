import SearchForm from "./SearchForm";
import FooterSection from "./FooterSection";
import { Outlet } from "react-router-dom";

function LandingPage() {
  return (
    <div className="min-h-screen bg-white">
      <main
        className="w-full px-4 sm:px-8 py-6 shadow-md"
        style={{ backgroundColor: "#f0fdfa" }}
      >
        <div className="max-w-4xl mx-auto">
          <SearchForm />
        </div>
      </main>
      <Outlet />
      <FooterSection />
    </div>
  );
}

export default LandingPage;
