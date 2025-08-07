import React from 'react';
import LoginPage from './LoginPage';
import RegisterPage from './RegisterPage';
import './AuthPage.css'

function AuthPage() {
    return (
        <div className="auth-wrapper">
            <div className="auth-section">
                <RegisterPage />
            </div>
            <div className="auth-section">
                <LoginPage />
            </div>
        </div>
    );
}

export default AuthPage;