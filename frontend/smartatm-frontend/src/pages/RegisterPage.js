import React, { useState } from 'react';
import axios from 'axios';


function RegisterPage() {
    const [username, setUserName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    
    const handleRegister = async () => {
        try{
            await axios.post("http://localhost:8080/api/auth/register", {
                username,
                email,
                password
            });

            alert('Registiration successful! Now you can login.');
            window.location.href='/register';
    
        } catch (error) {
            alert('Registiration failed!');
            console.error(error);
        }
    };

    return (
        <div className="auth-form">
            <h2>Register</h2>
            <input 
                type="text"
                placeholder="Username"
                value={username}
                onChange={(e) => setUserName(e.target.value)}
            /><br/>

            <input
                type="text"
                placeholder="Email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
            /><br/>

            <input
            type="text"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            /><br/>
            <button onClick= {handleRegister}>Register</button>
    
        </div>
    );
}

export default RegisterPage;