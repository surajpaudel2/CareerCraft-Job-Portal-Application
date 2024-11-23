export default function MetricCard({ icon: Icon, title, value }) {
  return (
    <div className="p-6 bg-teal-100 rounded-lg flex items-center space-x-4 transform transition duration-300 hover:scale-105 hover:bg-teal-200">
      <Icon className="h-12 w-12 text-teal-600" />
      <div>
        <h3 className="text-lg font-medium text-teal-700">{title}</h3>
        <p className="text-3xl font-bold text-gray-800">{value}</p>
      </div>
    </div>
  );
}
