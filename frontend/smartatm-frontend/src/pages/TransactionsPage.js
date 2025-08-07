import { useState, useEffect } from 'react';
import axios from 'axios'

function TransactionPage() {
    const [iban, setIban] = useState("");
    const [transactions, setTransactions] = useState([]);

    useEffect(() => {
        const token = localStorage.getItem("token");
        const fetchIbanAndTransactions = async () => {
            try {
                const ibanResponse = await axios.get("http://localhost:8080/api/account/me", {
                    headers: { Authorization: `Bearer ${token}`},
                });

                const userIban = ibanResponse.data.iban;
                setIban(userIban);

                const transactionsResponse = await axios.get(`http://localhost:8080/api/transactions/${userIban}`, {
                    headers: { Authorization: `Bearer ${token}`},
                });

                setTransactions(transactionsResponse.data);
                console.log("Transaction List:", transactionsResponse.data);
            }  catch (error) {
                console.error("Transaction data could not be retrieved.", error);
            }
        };
        fetchIbanAndTransactions();
    }, []);

    return (
        <div>
            <h2>Transaction Page</h2>
            <p>IBAN: {iban}</p>

            <h3>Transactions:</h3>
            <ul>
                {transactions.map((transactions, index) => (
                <li key={index}>    
                    <strong>{transactions.type}</strong> - {transactions.amount} € on {transactions.timestamp.split("T")[1].slice(0,8)}
                </li>
                ))}
            </ul>
        </div>
    )
}

export default TransactionPage;