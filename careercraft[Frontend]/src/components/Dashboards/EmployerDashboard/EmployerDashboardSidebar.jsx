"use client";

import { Sidebar } from "flowbite-react";
import {
  HiChartPie,
  HiUser,
  HiBriefcase,
  HiDocumentText,
  HiInbox,
  HiTrendingUp,
  HiCog,
  HiUsers,
  HiCreditCard,
} from "react-icons/hi";
import { BiBuoy } from "react-icons/bi";
import { NavLink } from "react-router-dom";

export default function EmployerDashboardSidebar() {
  return (
    <div>
      <Sidebar
        className="h-screen fixed overflow-hidden"
        aria-label="Employer Dashboard Sidebar"
      >
        <div className="mb-7">
          <h1>CareerCraft</h1>
        </div>
        <Sidebar.Items>
          {/* Main Sections */}
          <Sidebar.ItemGroup>
            <Sidebar.Item
              as={NavLink}
              to={"/employer/dashboard"}
              icon={HiChartPie}
            >
              Dashboard
            </Sidebar.Item>
            <Sidebar.Item
              as={NavLink}
              to="/employer/job-postings"
              icon={HiBriefcase}
            >
              Job Postings
            </Sidebar.Item>
            <Sidebar.Item
              as={NavLink}
              to="/employer/applications"
              icon={HiDocumentText}
            >
              Applications
            </Sidebar.Item>
            <Sidebar.Item
              as={NavLink}
              to="employer/candidate-search"
              icon={HiUsers}
            >
              Candidate Search
            </Sidebar.Item>
            <Sidebar.Item as={NavLink} to="/employer/messages" icon={HiInbox}>
              Messages
            </Sidebar.Item>
            <Sidebar.Item
              href="/employer-dashboard/analytics"
              icon={HiTrendingUp}
            >
              Analytics
            </Sidebar.Item>
          </Sidebar.ItemGroup>

          {/* Profile and Management */}
          <Sidebar.ItemGroup>
            <Sidebar.Item as={NavLink} to="/employer/profile" icon={HiUser}>
              Company Profile
            </Sidebar.Item>
            <Sidebar.Item
              href="/employer-dashboard/team-management"
              icon={HiUsers}
            >
              Team Management
            </Sidebar.Item>
          </Sidebar.ItemGroup>

          {/* Billing and Settings */}
          <Sidebar.ItemGroup>
            <Sidebar.Item
              href="/employer-dashboard/billing"
              icon={HiCreditCard}
            >
              Billing & Subscription
            </Sidebar.Item>
            <Sidebar.Item href="/employer-dashboard/settings" icon={HiCog}>
              Settings
            </Sidebar.Item>
          </Sidebar.ItemGroup>

          {/* Support and Help */}
          <Sidebar.ItemGroup>
            <Sidebar.Item
              href="/employer-dashboard/documentation"
              icon={HiChartPie}
            >
              Documentation
            </Sidebar.Item>
            <Sidebar.Item href="/employer-dashboard/help" icon={BiBuoy}>
              Help & Support
            </Sidebar.Item>
          </Sidebar.ItemGroup>
        </Sidebar.Items>
      </Sidebar>
    </div>
  );
}
