import React, { useState, useEffect } from 'react';
import axios from 'axios';

function TransferPage() {
  const [toIban, setToIban] = useState("");
  const [amount, setAmount] = useState("");
  const [myIban, setMyIban] = useState("");
  const [myBalance, setMyBalance] = useState(0);
  const [transactions, setTransactions] = useState([]);

  const token = localStorage.getItem('token');

  //Get the user`s IBAN and balance
  const fetchMyAccount = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/account/me', {
        headers: { Authorization: `Bearer ${token}`},
      });
      setMyIban(response.data.iban);
      setMyBalance(response.data.balance);
    } catch (error) {
      console.error('Error when logging into your account:', error);
    }
  }

  //Get transaction history by IBAN
  const fetchTransactions = async (iban) => {
    try {
      const response = await axios.get(`http://localhost:8080/api/transactions/${iban}`, {
        headers: { Authorization : `Bearer ${token}`},
  });
    setTransactions(response.data);
  } catch (error) {
    console.error('Transaction data could not be retrieved.', error);
  }
  };

  //Called when the page loads
  useEffect(() => {
    if (!token) return;
    fetchMyAccount();
  });

  //Receive transactions after receiving IBAN
  useEffect(() => {
    if (myIban) {
      fetchTransactions(myIban);
    }
  });


  const handleTransfer = async () => {

    try {
      await axios.post("http://localhost:8080/api/transfer", {
        toIban,
        amount
      }, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      alert("Transfer successful ✅");
      setToIban('');
      setAmount('');
      fetchMyAccount();
      fetchTransactions(myIban);
    } catch (error) {
      console.error("Transfer failed ❌:", error);
      alert("Transfer failed!");
    }
  };

  return (
    <div>
      <h2>Welcome!</h2>
      <p><strong>Your IBAN:</strong> {myIban}</p>
      <p><strong>Your Balance:</strong> {myBalance} €</p>

      <h3>Transfer Money</h3>
      <input
        type="text"
        placeholder="Recipient's IBAN"
        value={toIban}
        onChange={(e) => setToIban(e.target.value)}
      />
      <br />
      <input
        type="number"
        placeholder="Amount"
        value={amount}
        onChange={(e) => setAmount(e.target.value)}
      />
      <br />
      <button onClick={handleTransfer}>Send Money</button>

      <h3 style={{ marginTop: '2rem' }}>Transaction History</h3>
      <ul>
        {transactions.map((tx, idx) => (
          <li key={idx}>
            <strong>{tx.type}</strong> — {tx.amount} € on {tx.timestamp.split('T')[0]}
            {tx.fromIban && <span> | From: {tx.fromIban}</span>}
            {tx.toIban && <span> | To: {tx.toIban}</span>}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default TransferPage;