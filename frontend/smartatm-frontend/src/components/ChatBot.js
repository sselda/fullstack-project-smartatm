import React, { useState } from 'react';
import axios from 'axios';

function ChatBot() {
  const [input, setInput] = useState('');
  const [messages, setMessages] = useState([]);

  const sendMessage = async () => {
  if (!input.trim()) return;

  const newMessages = [...messages, { sender: 'user', text: input }];
  setMessages(newMessages);
  setInput('');

  const token = localStorage.getItem('token');

  try {
    const response = await axios.post(
      'http://localhost:8080/mock/chat',
      {
        model: 'gpt-3.5-turbo',
        messages: [
          { role: 'system', content: 'You are a bank assistant. Help the user with ATM transactions.' },
          ...newMessages.map((msg) => ({
            role: msg.sender === 'user' ? 'user' : 'assistant',
            content: msg.text,
          }))
        ]
      },
      {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}` // ✅ Header’a token’ı ekle
        },
      }
    );

    const reply = response.data.choices[0].message.content;
    setMessages([...newMessages, { sender: 'assistant', text: reply }]);
  } catch (error) {
    setMessages([...newMessages, { sender: 'assistant', text: 'An error has occurred. Please try again.' }]);
  }
};


  return (
    <div style={{ border: '1px solid #ccc', padding: '1rem', borderRadius: '8px', maxWidth: '500px', margin: '2rem auto' }}>
      <h3>💬 SmartATM Asistan</h3>
      <div style={{ height: '200px', overflowY: 'auto', marginBottom: '1rem', background: '#f9f9f9', padding: '1rem' }}>
        {messages.map((msg, index) => (
          <div key={index} style={{ textAlign: msg.sender === 'user' ? 'right' : 'left' }}>
            <strong>{msg.sender === 'user' ? 'Sen' : 'Bot'}:</strong> {msg.text}
          </div>
        ))}
      </div>
      <input
        type="text"
        placeholder="Write something..."
        value={input}
        onChange={(e) => setInput(e.target.value)}
        style={{ width: '80%', padding: '0.5rem' }}
      />
      <button onClick={sendMessage} style={{ padding: '0.5rem' }}>Send</button>
    </div>
  );
}

export default ChatBot;
