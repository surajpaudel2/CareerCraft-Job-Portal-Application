export default function ActivityItem({ text }) {
  return (
    <li className="bg-gray-50 p-4 rounded-lg shadow-sm flex items-center transform transition duration-300 hover:bg-teal-50">
      <span className="w-2 h-2 bg-teal-500 rounded-full mr-3"></span>
      {text}
    </li>
  );
}
