import ProfileHeader from "./ProfileHeader";
import DescriptionSection from "./ProfileDescription";
import Timestamps from "./TimeStamps";
import PostedJobsList from "./PostedJobsList";
import { AiOutlineEdit } from "react-icons/ai";
import { useEmployer } from "../../../../contexts/EmployerContext";

export default function CompanyProfile() {
  const { companyDetails } = useEmployer();

  const { createdAt, updatedAt } = companyDetails;

  return (
    <div className="relative w-full h-full p-8 bg-white dark:bg-gray-800 rounded-lg shadow-lg">
      <div className="absolute top-4 right-4">
        <button className="flex items-center px-4 py-2 bg-teal-600 text-white rounded-md hover:bg-teal-700 transition duration-300">
          <AiOutlineEdit className="mr-2" />
          Edit Profile
        </button>
      </div>

      <ProfileHeader />

      <DescriptionSection description={companyDetails.description} />

      <Timestamps createdAt={createdAt} updatedAt={updatedAt} />

      <PostedJobsList />
    </div>
  );
}
