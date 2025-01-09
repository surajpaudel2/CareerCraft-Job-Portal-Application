import {
  Navigate,
  Route,
  BrowserRouter as Router,
  Routes,
  useLocation,
} from "react-router-dom";
import TopNav from "./components/Navbar/TopNav";
import Login from "./components/Forms/Login/Login";
import { GoogleOAuthProvider } from "@react-oauth/google";
import OtpVerification from "./components/Forms/OtpVerification";
import Signup from "./components/Forms/Signup/Signup";
import EmployerDashboardLayout from "./components/Dashboards/EmployerDashboard/EmployerDashboardLayout";
import EmployerDashboardOverview from "./components/Dashboards/EmployerDashboard/EmployerDashboardOverview";
import JobPostings from "./components/Dashboards/EmployerDashboard/Job Postings/JobPostings";
import CompanyProfile from "./components/Dashboards/EmployerDashboard/Profile/CompanyProfile";
import Messages from "./components/Dashboards/EmployerDashboard/Messages/Messages";
import EmployerProfileForm from "./components/Forms/Employer Sign Up Form/EmployerProfileForm";
import PrivateRoute from "./components/Private Route/PrivateRoute";
import AuthProvider from "./contexts/AuthContext";
import { EmployerProvider } from "./contexts/EmployerContext";
import LandingPage from "./components/LandingPage/LandingPage";
import WhyUsSection from "./components/LandingPage/WhyUsSection";
import Jobs from "./components/Jobs/Jobs";
import { JobProvider } from "./contexts/JobContext";

const clientId =
  "516077764705-ueuep86aim4a2f20595f1p1jqc9pt689.apps.googleusercontent.com"; // Client Id for the google login.

function App() {
  return (
    <AuthProvider>
      <GoogleOAuthProvider clientId={clientId}>
        <Router>
          <MainContent />
        </Router>
      </GoogleOAuthProvider>
    </AuthProvider>
  );
}

/*
  This component is responsible for managing the routing within the application.
  
  Key Features:
  - Avoids unnecessary re-rendering of the TopNav component for certain routes.
  - Implements conditional rendering of the TopNav based on the current URL.
    For example:
    - TopNav is displayed for most routes.
    - TopNav is hidden for routes under "/employer", as the dashboard for users, 
      employers, and admins does not require it.

  The conditional rendering logic is based on `location.pathname`.
*/
function MainContent() {
  const location = useLocation();

  // Determine whether to render the TopNav based on the current route.
  const shouldRenderTopNav = !location.pathname.startsWith("/employer");

  return (
    <>
      {shouldRenderTopNav && <TopNav />}
      <Routes>
        {/* Public routes */}
        <Route
          path="/"
          element={
            <JobProvider>
              <LandingPage />
            </JobProvider>
          }
        >
          <Route index element={<WhyUsSection />} />
        </Route>
        <Route
          path="/jobs"
          element={
            <JobProvider>
              <Jobs />
            </JobProvider>
          }
        />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/otp-verification" element={<OtpVerification />} />
        <Route path="/register-employer" element={<EmployerProfileForm />} />

        {/* Employer dashboard (private routes) */}
        <Route
          path="/employer"
          element={
            <PrivateRoute>
              <EmployerProvider>
                <EmployerDashboardLayout />
              </EmployerProvider>
            </PrivateRoute>
          }
        >
          <Route index element={<Navigate to="dashboard" />} />
          <Route path="dashboard" element={<EmployerDashboardOverview />} />
          <Route path="job-postings" element={<JobPostings />} />
          <Route path="profile" element={<CompanyProfile />} />
          <Route path="messages" element={<Messages />} />
        </Route>
      </Routes>
    </>
  );
}

export default App;
