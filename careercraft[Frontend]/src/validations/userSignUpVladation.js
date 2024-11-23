/**
 * Validates a single form field based on its name, value, and the current state of the form.
 *
 * @param {string} name - The name of the form field (e.g., "username", "email", "password").
 * @param {string} value - The current value of the field being validated.
 * @param {object} state - The current state of the form, used for cross-field validations (e.g., comparing passwords).
 * @returns {string} - Returns an error message if the field is invalid; otherwise, returns an empty string.
 */

// Helper function to check if a value is provided
const isRequired = (value, fieldName) =>
  !value ? `${fieldName} is required` : "";

// Helper function to validate length
const isValidLength = (value, min, max, fieldName) =>
  value.length < min || value.length > max
    ? `${fieldName} must be between ${min} and ${max} characters`
    : "";

// Helper function to validate email format
const isValidEmail = (value) =>
  /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value) ? "" : "Email should be valid";

// Helper function to validate password complexity
const isPasswordComplex = (value) =>
  // eslint-disable-next-line no-useless-escape
  /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%\^&\*])(?=\S+$).{8,}$/.test(
    value
  )
    ? ""
    : "Password must contain at least 1 digit, 1 lowercase, 1 uppercase, 1 special character, and no whitespace";

export default function userSignUpValidation(name, value, state) {
  switch (name) {
    case "username":
      return (
        isRequired(value, "Username") || isValidLength(value, 3, 50, "Username")
      );

    case "email":
      return isRequired(value, "Email") || isValidEmail(value);

    case "password":
      return (
        isRequired(value, "Password") ||
        (value.length < 8 && "Password must be at least 8 characters") ||
        isPasswordComplex(value)
      );

    case "fullName":
      return (
        isRequired(value, "Full name") ||
        isValidLength(value, 2, 100, "Full name")
      );

    case "confirmPassword":
      return value !== state.password.value ? "Passwords do not match" : "";

    default:
      return "";
  }
}
