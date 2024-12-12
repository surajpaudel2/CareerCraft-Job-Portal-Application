export default function Input({
  label = "",
  type = "text",
  name,
  id,
  placeholder,
  value = "",
  handleOnChange,
  handleOnBlur,
  options = [],
}) {
  // Helper function to check if a value is included in the array, ignoring case
  const isChecked = (optionValue) => {
    if (Array.isArray(value)) {
      return value.some(
        (val) => val.toLowerCase() === optionValue.toLowerCase()
      );
    }
    return false;
  };

  return (
    <div className="mb-4">
      {label && (
        <label
          htmlFor={id}
          className="block text-sm font-medium text-gray-700 dark:text-white mb-1"
        >
          {label}
        </label>
      )}

      {type === "select" ? (
        <select
          name={name}
          id={id}
          className="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-teal-600 focus:border-teal-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
          value={
            options.find(
              (option) => option.toLowerCase() === value.toLowerCase()
            ) || ""
          } // Case-insensitive match for select field
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
      ) : type === "textarea" ? (
        <textarea
          name={name}
          id={id}
          placeholder={placeholder}
          value={value}
          onChange={(event) => handleOnChange(event)}
          onBlur={(event) => handleOnBlur(event)}
          rows="5" // Default number of rows for a larger textarea
          className="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-teal-600 focus:border-teal-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
        />
      ) : type === "checkbox" ? (
        <div className="flex flex-wrap gap-2">
          {options.map((option) => (
            <label
              key={option}
              className="flex items-center space-x-2 text-gray-900 dark:text-white"
            >
              <input
                type="checkbox"
                name={name}
                value={option}
                checked={isChecked(option)} // Case-insensitive match for checkboxes
                onChange={(event) => {
                  const selectedValue = event.target.value;
                  if (event.target.checked) {
                    handleOnChange({
                      target: {
                        name,
                        value: [...(value || []), selectedValue],
                      },
                    });
                  } else {
                    handleOnChange({
                      target: {
                        name,
                        value: value.filter(
                          (v) => v.toLowerCase() !== selectedValue.toLowerCase()
                        ), // Remove value ignoring case
                      },
                    });
                  }
                }}
                className="form-checkbox text-teal-600 focus:ring-teal-500"
              />
              <span>{option}</span>
            </label>
          ))}
        </div>
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
    </div>
  );
}
