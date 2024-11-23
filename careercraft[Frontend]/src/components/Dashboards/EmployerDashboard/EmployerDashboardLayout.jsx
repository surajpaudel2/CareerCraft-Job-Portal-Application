import { Outlet } from "react-router-dom";
import EmployerDashboardSidebar from "./EmployerDashboardSidebar";

/*
  Layout of the employer dashboard, how dashboard should be represeneted.
*/
export default function EmployerDashboardLayout() {
  return (
    <div className="min-h-screen flex bg-gray-100">
      <div className="w-64 bg-gray-800 text-white h-screen overflow-hidden">
        <EmployerDashboardSidebar />
      </div>

      <div className="flex-1 p-4 overflow-y-auto">
        <Outlet />
      </div>
    </div>
  );
}
