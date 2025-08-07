import React from 'react';
import ChatBot from '../components/ChatBot';

function Dashboard() {
    return (
        <div style={{ padding: '2rem' }}>
            <h1>Welcome to SmartATM</h1>
            <p>Select an action from the menu.</p>
            <h2>Dashboard</h2>
  <ChatBot />
        </div>
    );
}

export default Dashboard;