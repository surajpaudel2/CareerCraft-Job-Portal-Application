import Label from "./Label";
import Input from "./Input";

export default function FormField({
  label,
  type,
  name,
  id,
  placeholder,
  handleOnChange,
  handleOnBlur,
  error,
  value,
  options = [],
}) {
  return (
    <div>
      <Label htmlFor={id} title={label} />
      {type === "textarea" ? (
        <textarea
          name={name}
          id={id}
          value={value}
          placeholder={placeholder}
          onChange={handleOnChange}
          onBlur={handleOnBlur}
          className="w-full p-2 mt-1 border rounded-md focus:outline-none focus:ring-2 focus:ring-teal-500"
          rows="8"
        />
      ) : type === "select" ? (
        <select
          name={name}
          id={id}
          value={
            options.includes(value) // Check for an exact match
              ? value
              : options.find(
                  (option) => option.toLowerCase() === value?.toLowerCase()
                ) || options[0] // Match case-insensitively
          }
          onChange={handleOnChange}
          onBlur={handleOnBlur}
          className="w-full p-2 mt-1 border rounded-md focus:outline-none focus:ring-2 focus:ring-teal-500"
        >
          {options.map((option) => (
            <option key={option} value={option}>
              {option}
            </option>
          ))}
        </select>
      ) : (
        <Input
          type={type}
          name={name}
          id={id}
          value={value}
          options={options}
          placeholder={placeholder}
          handleOnChange={handleOnChange}
          handleOnBlur={handleOnBlur}
        />
      )}
      {error && <p className="text-red-500 text-sm">{error}</p>}
    </div>
  );
}
