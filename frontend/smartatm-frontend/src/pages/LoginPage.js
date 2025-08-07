import React, {useState} from "react";
import axios from "axios";

function LoginPage() {
    
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = async () => {
        try {

            const response = await axios.post("http://localhost:8080/api/auth/login", {
                email,
                password
            });

            console.log("Login response:", response.data);
            const token = response.data;
            localStorage.setItem("token", token);
          
        } catch (error) {
            alert('Login failed!');
        }
    };

    return (
        <div className="auth-form">
            <h2>Login</h2>
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
            <button onClick={handleLogin}>Login</button>
        </div>
    );
}

export default LoginPage;