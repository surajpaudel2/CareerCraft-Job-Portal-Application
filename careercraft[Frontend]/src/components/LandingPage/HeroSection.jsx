import Input from "../Forms/Input";

export default function HeroSection() {
  return (
    <section className="relative bg-teal-600 text-white text-center py-20 px-10">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-5xl font-bold mb-6">Welcome to CareerCraft</h1>
        <p className="text-lg mb-8">
          Your ultimate job portal to find your dream job or the perfect
          candidate for your company.
        </p>
        <Input
          type="text"
          name="jobTitle"
          id="jobTitle"
          placeholder="Job Title"
        />
      </div>
    </section>
  );
}
