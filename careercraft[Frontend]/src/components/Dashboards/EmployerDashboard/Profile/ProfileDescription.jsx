/*
  Component responsible to show the description of the company
*/
export default function DescriptionSection({ description }) {
  return (
    <div className="space-y-4 w-4/5 m-auto">
      <div>
        <p className="text-gray-800 leading-relaxed">{description}</p>
      </div>
    </div>
  );
}
