/**
 * Validates a job field based on its name and value.
 *
 * @param {string} name - The name of the field to validate (e.g., "jobTitle", "description").
 * @param {string | number} value - The value of the field being validated.
 * @returns {string} - An error message if the field is invalid; otherwise, an empty string.
 */

// Helper function to check if a value is provided
const isRequired = (value, fieldName) =>
  !value ? `${fieldName} is required` : "";

// Helper function to validate string length
const isValidLength = (value, min, max, fieldName) =>
  value.length < min || value.length > max
    ? `${fieldName} must be between ${min} and ${max} characters`
    : "";

// Helper function to validate numeric fields
const isPositiveNumber = (value, fieldName) =>
  isNaN(value) || Number(value) <= 0
    ? `${fieldName} must be a positive number`
    : "";

// Helper function to validate job status
const isValidStatus = (value, validStatuses) =>
  validStatuses.some((status) => status.toLowerCase() === value.toLowerCase())
    ? ""
    : "Invalid status selected";

export default function jobFormVAlidation(name, value) {
  switch (name) {
    case "jobTitle":
      return (
        isRequired(value, "Job title") ||
        isValidLength(value, 3, 100, "Job title")
      );

    case "description":
      return (
        isRequired(value, "Description") ||
        (value.length < 10 && "Description must be at least 10 characters")
      );

    case "salary":
      return isRequired(value, "Salary") || isPositiveNumber(value, "Salary");

    case "location":
      return isRequired(value, "Location");

    case "status":
      return isValidStatus(value, ["Active", "Pending", "Closed"]);

    default:
      return "";
  }
}
