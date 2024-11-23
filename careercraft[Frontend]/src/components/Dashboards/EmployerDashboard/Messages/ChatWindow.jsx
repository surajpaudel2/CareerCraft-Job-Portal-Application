import { useState } from "react";

function ChatWindow({ conversation }) {
  const [newMessage, setNewMessage] = useState("");

  const handleSendMessage = () => {
    if (newMessage.trim()) {
      // Logic to send a message (e.g., add to messages array)
      setNewMessage(""); // Clear input after sending
    }
  };

  return (
    <div className="w-2/3 bg-white dark:bg-gray-800 rounded-lg shadow-md flex flex-col p-4">
      {/* Header */}
      <div className="flex items-center mb-4">
        <img
          src={conversation.profileImage}
          alt={conversation.name}
          className="w-10 h-10 rounded-full mr-3"
        />
        <div>
          <h2 className="text-xl font-semibold text-gray-800">
            {conversation.name}
          </h2>
          <p className="text-sm text-gray-500">Reply to message</p>
        </div>
      </div>

      {/* Messages */}
      <div className="flex-1 overflow-y-auto mb-4 space-y-4">
        {conversation.messages.map((msg, index) => (
          <div
            key={index}
            className={`flex ${msg.isSender ? "justify-end" : "justify-start"}`}
          >
            <div
              className={`max-w-xs p-3 rounded-lg text-white ${
                msg.isSender ? "bg-teal-500" : "bg-gray-400 text-gray-800"
              }`}
            >
              <p>{msg.content}</p>
              <span className="text-xs mt-1 block text-right text-gray-300">
                {msg.timestamp}
              </span>
            </div>
          </div>
        ))}
      </div>

      {/* Message Input */}
      <div className="flex items-center border-t p-2">
        <input
          type="text"
          placeholder="Type something here"
          value={newMessage}
          onChange={(e) => setNewMessage(e.target.value)}
          className="flex-1 border-none bg-gray-100 rounded-full px-4 py-2 text-gray-800 focus:outline-none focus:ring focus:ring-teal-500"
        />
        <button
          onClick={handleSendMessage}
          className="ml-2 p-2 rounded-full bg-teal-500 text-white hover:bg-teal-600"
        >
          <span className="material-icons">send</span>
        </button>
      </div>
    </div>
  );
}

export default ChatWindow;
