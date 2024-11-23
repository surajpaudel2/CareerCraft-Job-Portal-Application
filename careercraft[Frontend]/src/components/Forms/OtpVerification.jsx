import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { Alert } from "flowbite-react";
import apiBaseUrl from "../../api/apiBaseUrl";

export default function OTPVerification() {
  const location = useLocation();
  const email = location.state?.email; // Retrieve the email passed from Signup

  const [otp, setOtp] = useState(Array(6).fill("")); // 6-digit OTP

  const [errorMessage, setErrorMessage] = useState(""); // State for setting the error message if the user enters invalid OTP.

  const navigate = useNavigate();

  useEffect(() => {
    if (!email) {
      navigate("/"); // Redirect to signup page if email is missing
    }
  }, [email, navigate]);

  const handleChange = (event, index) => {
    const value = event.target.value;

    // Handle backspace for the last input
    if (event.key === "Backspace") {
      if (otp[index] === "") {
        if (index > 0) {
          const newOtp = [...otp];
          newOtp[index - 1] = ""; // Clear the previous input
          setOtp(newOtp);
          event.target.previousSibling?.focus();
        }
      } else {
        const newOtp = [...otp];
        newOtp[index] = ""; // Clear the current input
        setOtp(newOtp);
      }
      return;
    }

    // Handle normal input for digits
    if (/^\d$/.test(value)) {
      // Only allow a single digit
      const newOtp = [...otp];
      newOtp[index] = value;
      setOtp(newOtp);

      // Automatically move to the next input
      if (index < otp.length - 1) {
        event.target.nextSibling?.focus();
      }
    }
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    const otpValue = otp.join(""); // Combine array to get full OTP string

    if (otpValue.length < 6) {
      setErrorMessage("Please enter a 6-digit OTP.");
      return;
    }

    setErrorMessage(""); // Clear any previous error message

    // Submit OTP and email for verification
    apiBaseUrl
      .post("/auth/verify/reg/otp", { email, otp: otpValue })
      .then((response) => {
        const { success, message } = response.data;
        if (success) {
          navigate("/", { state: { isNewlyVerified: true }, replace: true });
        } else {
          setErrorMessage(message);
        }
      })
      .catch((error) => {
        const { message } = error.response.data;
        setErrorMessage(message);
      });
  };

  return (
    <section className="py-4 md:py-8 dark:bg-gray-800">
      <div className="flex flex-col items-center justify-center px-6 py-8 mx-auto md:h-screen lg:py-0">
        <div className="w-full bg-white rounded-lg shadow dark:border sm:max-w-md xl:p-0 dark:bg-gray-800 dark:border-gray-700">
          <div className="p-6 space-y-4 md:space-y-6 sm:p-8">
            {errorMessage && <Alert color="failure">{errorMessage}</Alert>}

            <h1 className="text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl dark:text-white">
              Enter the OTP sent to {email}
            </h1>

            <form className="space-y-4 md:space-y-6" onSubmit={handleSubmit}>
              <div className="flex justify-center space-x-2">
                {/* Creating 6 input filed for OTP.  */}
                {otp.map((digit, index) => (
                  <input
                    key={index}
                    type="text"
                    maxLength="1"
                    value={digit}
                    onChange={(event) => handleChange(event, index)}
                    onKeyDown={(event) => handleChange(event, index)}
                    className="w-12 h-12 text-center border border-gray-300 rounded-md focus:ring-teal-600 focus:border-teal-600 dark:bg-gray-700 dark:border-gray-600 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                  />
                ))}
              </div>

              <button
                type="submit"
                className="w-full bg-teal-600 text-white font-bold py-2 px-4 rounded"
              >
                Verify OTP
              </button>
            </form>
          </div>
        </div>
      </div>
    </section>
  );
}
