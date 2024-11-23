// MessagesSection.js
import { useState } from "react";
import ConversationList from "./ConversationList";
import ChatWindow from "./ChatWindow";

const dummyConversations = [
  {
    id: 1,
    name: "Henry Dhole",
    profileImage: "https://via.placeholder.com/40",
    lastMessage: "I came across your profile and...",
    isOnline: true,
    messages: [
      {
        sender: "Henry Dhole",
        content: "I want to make an appointment tomorrow from 2:00 to 5:00pm?",
        timestamp: "1:55pm",
        isSender: false,
      },
      {
        sender: "You",
        content: "Hello, Thomas! I will check the schedule and inform you.",
        timestamp: "1:56pm",
        isSender: true,
      },
      {
        sender: "Henry Dhole",
        content: "Ok, Thanks for your reply.",
        timestamp: "1:57pm",
        isSender: false,
      },
      {
        sender: "You",
        content: "You are welcome!",
        timestamp: "1:58pm",
        isSender: true,
      },
      {
        sender: "You",
        content: "You are welcome!",
        timestamp: "1:58pm",
        isSender: true,
      },
      {
        sender: "You",
        content: "You are welcome!",
        timestamp: "1:58pm",
        isSender: true,
      },
      {
        sender: "You",
        content: "You are welcome!",
        timestamp: "1:58pm",
        isSender: true,
      },
      {
        sender: "You",
        content: "You are welcome!",
        timestamp: "1:58pm",
        isSender: true,
      },
    ],
  },
  {
    id: 2,
    name: "Mariya Desoja",
    profileImage: "https://via.placeholder.com/40",
    lastMessage: "I like your confidence 👍",
    isOnline: true,
    messages: [
      {
        sender: "Mariya Desoja",
        content: "Hello! I just wanted to say I like your profile.",
        timestamp: "2:05pm",
        isSender: false,
      },
      {
        sender: "You",
        content: "Thank you so much, Mariya! I appreciate it.",
        timestamp: "2:10pm",
        isSender: true,
      },
      {
        sender: "Mariya Desoja",
        content: "Would love to collaborate sometime!",
        timestamp: "2:12pm",
        isSender: false,
      },
      {
        sender: "You",
        content: "That would be awesome. Let’s set up a time.",
        timestamp: "2:15pm",
        isSender: true,
      },
    ],
  },
  {
    id: 3,
    name: "Robert Jhon",
    profileImage: "https://via.placeholder.com/40",
    lastMessage: "Can you share your offer?",
    isOnline: false,
    messages: [
      {
        sender: "Robert Jhon",
        content: "Hey, can you send over your latest offers?",
        timestamp: "3:00pm",
        isSender: false,
      },
      {
        sender: "You",
        content: "Sure, Robert. I’ll send it shortly.",
        timestamp: "3:02pm",
        isSender: true,
      },
      {
        sender: "Robert Jhon",
        content: "Thanks, looking forward to it.",
        timestamp: "3:05pm",
        isSender: false,
      },
    ],
  },
  {
    id: 4,
    name: "Cody Fisher",
    profileImage: "https://via.placeholder.com/40",
    lastMessage: "I’m waiting for your response!",
    isOnline: true,
    messages: [
      {
        sender: "Cody Fisher",
        content: "Hey, did you get my last message?",
        timestamp: "4:15pm",
        isSender: false,
      },
      {
        sender: "You",
        content: "Apologies, Cody! I was just about to respond.",
        timestamp: "4:17pm",
        isSender: true,
      },
      {
        sender: "Cody Fisher",
        content: "No worries. Just wanted to follow up.",
        timestamp: "4:20pm",
        isSender: false,
      },
    ],
  },
  {
    id: 5,
    name: "Jenny Wilson",
    profileImage: "https://via.placeholder.com/40",
    lastMessage: "I’m waiting for you response!",
    isOnline: false,
    messages: [
      {
        sender: "Jenny Wilson",
        content: "Hey! Can we set up a time to discuss the project?",
        timestamp: "10:00am",
        isSender: false,
      },
      {
        sender: "You",
        content: "Of course! How does tomorrow afternoon sound?",
        timestamp: "10:05am",
        isSender: true,
      },
      {
        sender: "Jenny Wilson",
        content: "Perfect. I'll send you an invite.",
        timestamp: "10:10am",
        isSender: false,
      },
    ],
  },
  {
    id: 6,
    name: "Marcus Siphon",
    profileImage: "https://via.placeholder.com/40",
    lastMessage: "Just wanted to follow up.",
    isOnline: false,
    messages: [
      {
        sender: "Marcus Siphon",
        content: "Hey, just following up on our last conversation.",
        timestamp: "11:30am",
        isSender: false,
      },
      {
        sender: "You",
        content: "Thanks for checking in, Marcus. I'll update you soon.",
        timestamp: "11:35am",
        isSender: true,
      },
      {
        sender: "Marcus Siphon",
        content: "No problem, appreciate it.",
        timestamp: "11:37am",
        isSender: false,
      },
    ],
  },
];

export default function Messages() {
  const [conversations] = useState(dummyConversations);
  const [selectedConversation, setSelectedConversation] = useState(null);

  return (
    <div className="flex h-full space-x-4">
      {/* Sidebar with conversations */}
      <ConversationList
        conversations={conversations}
        onSelectConversation={setSelectedConversation}
      />

      {/* Chat window for the selected conversation */}
      {selectedConversation ? (
        <ChatWindow conversation={selectedConversation} />
      ) : (
        <div className="flex-1 bg-white dark:bg-gray-800 rounded-lg shadow-md flex items-center justify-center">
          <p className="text-gray-500">
            Select a conversation to start chatting
          </p>
        </div>
      )}
    </div>
  );
}
