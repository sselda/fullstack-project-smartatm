
import './App.css';
import React from 'react';
import Dashboard from './pages/Dashboard';
import DepositPage from './pages/DepositPage';
import WithdrawPage from './pages/WithdrawPage';
import TransactionPage from './pages/TransactionsPage';
import { Link, Route, Routes, useNavigate } from 'react-router-dom';
import ProtectedRoute from './components/ProtectedRoute';
import RegisterPage from './pages/RegisterPage';
import LoginPage from './pages/LoginPage';
import { useEffect, useState } from "react";
import TransferPage from './pages/TransferPage';

function App() {
  const [token, setToken ] = useState(localStorage.getItem("token"));
  const navigate = useNavigate();

  useEffect(() => {
    const handleStorage = () => {
      setToken(localStorage.getItem("token"));
    };

    window.addEventListener("storage", handleStorage);
    return () => window.removeEventListener("storage", handleStorage);

  }, []);


  return (
  
      <div style={{padding: '2rem' }}>
        <nav>
          {token ? (
            <>
          <Link to="/">Dashboard</Link> |{" "}
          <Link to="/deposit">Deposit</Link> |{" "}
          <Link to="/withdraw">Withdraw</Link> |{" "}
          <Link to="/transaction">Transaction</Link> |{" "}
          <Link to="/transfer">Transfer</Link> |{" "}
          <button onClick={() => {
            localStorage.removeItem("token");
            setToken(null);
            navigate("/login");
          }}>Logout</button>
          </>
          ) : (
            <>
            <Link to="/login">Login</Link> |{" "}
            <Link to="/register">Register</Link> |{" "}
            </>
          )}
        </nav>
        <hr />
       
       <Routes>
        <Route path="/" element={<ProtectedRoute><Dashboard/></ProtectedRoute>} />
        <Route path="/deposit" element={<ProtectedRoute><DepositPage/></ProtectedRoute>} />
        <Route path="/withdraw" element={<ProtectedRoute><WithdrawPage/></ProtectedRoute>} />
        <Route path="/transaction" element={<ProtectedRoute><TransactionPage/></ProtectedRoute>} />
        <Route path="/transfer" element={<ProtectedRoute><TransferPage/></ProtectedRoute>} />
        <Route path="/login" element={<LoginPage setToken={setToken}/>} />
        <Route path="register" element={<RegisterPage/>} />
       </Routes>

      </div>
  )
}

export default App;
