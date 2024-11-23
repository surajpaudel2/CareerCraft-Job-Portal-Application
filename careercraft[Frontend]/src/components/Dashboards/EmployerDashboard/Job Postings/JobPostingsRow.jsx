import { useState } from "react";
import { HiPencilAlt, HiTrash } from "react-icons/hi";
import { DeletePopUpModal } from "../../../utilities/DeletePopUpModal";

/*
  TODO : Since the UI is not updated after the deletion of the job, need to fix this. It works if and only if we have 1 job in the object, but not for the multiple jobs.
*/

export default function JobPostingRow({ job, onDelete, onEdit }) {
  const [showDeletePopUpModal, setShowDeletePopUpModal] = useState(false); // State that manage to decide whether to show the pop up while deleting the job.
  const [jobIdToDelete, setJobIdToDelete] = useState(null);

  const messageForDelete = "Are you sure you want to delete this Job?";

  return (
    <>
      <tr className="border-b border-gray-200 hover:bg-gray-50 transition duration-200">
        <td className="p-4">{job.title}</td>
        <td className="p-4">{job.location}</td>
        <td className="p-4">
          <span
            className={`px-2 py-1 rounded-full text-xs ${
              job.status === "ACTIVE"
                ? "bg-green-100 text-green-700"
                : "bg-gray-200 text-gray-700"
            }`}
          >
            {job.status}
          </span>
        </td>
        <td className="p-4 text-center">
          <button
            onClick={() => onEdit(job.id)}
            className="text-blue-500 hover:text-blue-700 mr-3"
          >
            <HiPencilAlt className="inline-block" />
          </button>
          <button
            onClick={() => {
              setJobIdToDelete(job.id);
              setShowDeletePopUpModal(true);
            }}
            className="text-red-500 hover:text-red-700"
          >
            <HiTrash className="inline-block" />
          </button>
        </td>
      </tr>

      <DeletePopUpModal
        openModal={showDeletePopUpModal}
        setOpenModal={setShowDeletePopUpModal}
        message={messageForDelete}
        onConfirm={() => onDelete(jobIdToDelete)}
      />
    </>
  );
}
