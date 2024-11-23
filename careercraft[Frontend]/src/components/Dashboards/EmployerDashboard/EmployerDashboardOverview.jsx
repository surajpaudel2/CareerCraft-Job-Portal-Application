import { HiBriefcase, HiUserGroup, HiChatAlt } from "react-icons/hi";
import MetricCard from "../MetricCard";
import ActivityItem from "../ActivityItem";

export default function EmployerDashboardOverview() {
  return (
    <div className="p-6 bg-white shadow-lg rounded-lg">
      <h2 className="text-3xl font-semibold text-teal-600 mb-6">
        Dashboard Overview
      </h2>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <MetricCard icon={HiBriefcase} title="Active Job Postings" value="5" />
        <MetricCard
          icon={HiUserGroup}
          title="Applications Received"
          value="120"
        />
        <MetricCard icon={HiChatAlt} title="Messages" value="8" />
      </div>

      {/* Recent Activity */}
      <div className="mt-8">
        <h3 className="text-xl font-medium text-teal-700 mb-4">
          Recent Activity
        </h3>
        <ul className="space-y-3">
          <ActivityItem text="New application for Software Engineer" />
          <ActivityItem text="Message from John Doe" />
          <ActivityItem text="Job posting for Project Manager closed" />
        </ul>
      </div>
    </div>
  );
}
