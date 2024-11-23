import PropTypes from "prop-types";

export default function Button({ name }) {
  return (
    <button
      type="submit"
      className="text-white bg-teal-600 py-1.5 px-4 rounded font-bold w-full"
    >
      {name}
    </button>
  );
}

Button.propTypes = {
  name: PropTypes.string.isRequired, // Define `name` as a required string prop
};
