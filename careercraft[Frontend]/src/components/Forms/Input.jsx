export default function Input({
  type = "text",
  name,
  id,
  placeholder,
  value = "",
  handleOnChange,
  handleOnBlur,
  options = [],
}) {
  return (
    <>
      {type === "select" ? (
        <select
          name={name}
          id={id}
          className="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-teal-600 focus:border-teal-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
          value={value}
          onChange={(event) => handleOnChange(event)}
          onBlur={(event) => handleOnBlur(event)}
          required
        >
          <option value="" disabled hidden>
            {placeholder || "Select an option"}
          </option>
          {options.map((option) => (
            <option key={option} value={option}>
              {option}
            </option>
          ))}
        </select>
      ) : (
        <input
          type={type}
          name={name}
          id={id}
          className="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-teal-600 focus:border-teal-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
          placeholder={placeholder}
          value={value}
          onChange={(event) => handleOnChange(event)}
          onBlur={(event) => handleOnBlur(event)}
          required
        />
      )}
    </>
  );
}
