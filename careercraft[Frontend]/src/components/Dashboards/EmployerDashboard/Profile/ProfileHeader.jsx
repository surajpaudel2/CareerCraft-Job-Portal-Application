import { useEmployer } from "../../../../contexts/EmployerContext";
import getInitials from "../../../../utils/getInitials";

/*
  Component responsible to show the profile header that includes of the Company Profile Section. 
    -> Image (Or the company name initials depending upon the available logo url)
    -> Name of the Company
    -> Industry
    -> Location
*/
export default function ProfileHeader() {
  const { companyDetails } = useEmployer();
  const { logoUrl, companyName, industry, location } = companyDetails;

  let companyInitials = logoUrl ? "" : getInitials(companyName);

  return (
    <div className="text-center mb-8">
      <div className="flex justify-center mb-4">
        {logoUrl ? (
          <img
            src={logoUrl}
            alt="Company Logo"
            className="w-24 h-24 rounded-full object-cover shadow-md"
          />
        ) : (
          <div className="flex w-24 h-24 rounded-full  items-center justify-center bg-teal-500 text-white text-2xl font-bold shadow-md">
            {companyInitials}
          </div>
        )}
      </div>

      <h2 className="text-3xl font-semibold text-teal-600">{companyName}</h2>
      <p className="text-lg font-medium text-gray-600">{industry}</p>
      <p className="text-gray-800">{location}</p>
    </div>
  );
}
