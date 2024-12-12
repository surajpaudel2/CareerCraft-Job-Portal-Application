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
                checked={Array.isArray(value) && value.includes(option)}
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
                        value: value.filter((v) => v !== selectedValue),
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
    </>
  );
}
