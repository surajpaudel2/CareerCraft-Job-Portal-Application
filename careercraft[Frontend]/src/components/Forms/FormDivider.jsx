export default function FormDivider() {
  return (
    <div className="flex items-center">
      <div className="w-full h-0.5 bg-gray-200 dark:bg-gray-700"></div>
      <div className="px-5 text-center text-gray-500 dark:text-gray-400">
        or
      </div>
      <div className="w-full h-0.5 bg-gray-200 dark:bg-gray-700"></div>
    </div>
  );
}
