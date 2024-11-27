export default function WhyUsSection() {
  return (
    <section className="py-20 px-10 bg-white">
      <div className="max-w-6xl mx-auto text-center">
        <h2 className="text-3xl font-bold text-teal-700 mb-10">
          Why Choose CareerCraft?
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div className="bg-teal-50 shadow-lg rounded-lg p-6">
            <h3 className="text-xl font-semibold text-teal-800 mb-4">
              For Job Seekers
            </h3>
            <p className="text-gray-600">
              Discover the latest job openings, save jobs, and apply
              effortlessly to kickstart your career.
            </p>
          </div>
          <div className="bg-teal-50 shadow-lg rounded-lg p-6">
            <h3 className="text-xl font-semibold text-teal-800 mb-4">
              For Employers
            </h3>
            <p className="text-gray-600">
              Post jobs, manage applicants, and find the best talent to grow
              your business.
            </p>
          </div>
          <div className="bg-teal-50 shadow-lg rounded-lg p-6">
            <h3 className="text-xl font-semibold text-teal-800 mb-4">
              Easy to Use
            </h3>
            <p className="text-gray-600">
              Enjoy a seamless user experience with intuitive navigation and
              robust features.
            </p>
          </div>
        </div>
      </div>
    </section>
  );
}
