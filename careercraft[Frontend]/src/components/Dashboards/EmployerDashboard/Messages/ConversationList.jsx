function ConversationList({ conversations, onSelectConversation }) {
  return (
    <div className="w-1/3 bg-white dark:bg-gray-800 rounded-lg shadow-md p-4">
      <h2 className="text-xl font-semibold text-teal-600 mb-4">
        Active Conversations
      </h2>
      <ul className="space-y-2">
        {conversations.map((conversation) => (
          <li
            key={conversation.id}
            className="flex items-center p-2 rounded-lg cursor-pointer hover:bg-gray-100 transition duration-200"
            onClick={() => onSelectConversation(conversation)}
          >
            <img
              src={conversation.profileImage}
              alt={conversation.name}
              className="w-10 h-10 rounded-full mr-3"
            />
            <div>
              <h3 className="text-gray-800 font-semibold">
                {conversation.name}
              </h3>
              <p className="text-sm text-gray-600">
                {conversation.lastMessage}
              </p>
            </div>
            {conversation.isOnline && (
              <span className="ml-auto h-2 w-2 bg-green-500 rounded-full"></span>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default ConversationList;
