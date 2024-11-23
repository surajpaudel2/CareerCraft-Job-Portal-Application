import { useNavigate } from "react-router-dom";
import { CheckCircleIcon } from "@heroicons/react/24/solid"; // Heroicons or any other icon library
import { Button } from "flowbite-react"; // Flowbite's button component for a modern look

export default function CongratulationsMessage() {
  const navigate = useNavigate();

  const handleExplore = () => {
    navigate("/dashboard"); // Redirect to dashboard or main page
  };

  return (
    <section className="flex flex-col items-center justify-center min-h-screen bg-gradient-to-r from-green-400 to-blue-500 text-white">
      <div className="bg-white rounded-lg shadow-lg p-8 md:p-12 text-center max-w-md mx-auto">
        <CheckCircleIcon className="w-16 h-16 text-green-500 mx-auto mb-4" />
        <h1 className="text-2xl font-bold text-gray-800 mb-4">
          Congratulations!
        </h1>
        <p className="text-gray-600 mb-6">
          Your account is now active. You’re all set to explore and enjoy all
          the features we have to offer!
        </p>
        <p className="text-gray-600 mb-6">
          Feel free to browse, connect, and make the most of your experience
          with us. We’re thrilled to have you onboard!
        </p>
        <Button color="success" onClick={handleExplore} className="w-full">
          Start Exploring
        </Button>
      </div>
    </section>
  );
}
