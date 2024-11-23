/*
  Simple Component to show the timestamps
*/

export default function Timestamps({ createdAt, updatedAt }) {
  return (
    <div className="mt-6 text-center text-gray-500 text-sm">
      <p>Created At: {new Date(createdAt).toLocaleDateString()}</p>
      <p>Updated At: {new Date(updatedAt).toLocaleDateString()}</p>
    </div>
  );
}
