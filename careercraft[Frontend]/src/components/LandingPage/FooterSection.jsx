export default function FooterSection() {
  return (
    <footer className="bg-teal-600 text-white py-10">
      <div className="max-w-6xl mx-auto text-center">
        <p className="text-lg font-semibold">CareerCraft © 2025</p>
        <div className="flex justify-center gap-4 mt-4">
          <a href="#" className="hover:text-teal-200">
            Privacy Policy
          </a>
          <a href="#" className="hover:text-teal-200">
            Terms of Service
          </a>
          <a href="#" className="hover:text-teal-200">
            Contact Us
          </a>
        </div>
      </div>
    </footer>
  );
}
