import { useState } from 'react';
import axios from 'axios'
import { useLocation, useNavigate } from 'react-router-dom';
import { QRCodeSVG } from "qrcode.react";

function WithdrawPage() {
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const initialAmount = queryParams.get("amount") || "";
    const [amount, setAmount] = useState(initialAmount);
    const [qrId, setQrId] = useState("");
    const navigate = useNavigate();
    const [confirmed, setConfirmed] = useState(false);

    const handleWithdrawQR = async () => {
        try {
            const token = localStorage.getItem("token");
            console.log("token being sent:", token);

            if(!token) {
                alert("No token found, user not logged in.");
                navigate("/login");
                return;
            }

            const response = await axios.post('http://localhost:8080/api/withdraw/create-qr', 
                null,
                {
                    params: { amount },
                    responseType: 'text',
                    headers: {Authorization: `Bearer ${token}`}
            });

            console.log("QR response blob:", response.data);
            const qrId = response.data;
            console.log("QR ID:", qrId);
            setQrId(qrId);
        } catch (error) {
            console.error('QR code not be generated:', error)
        }
    };

    const handleWithdrawConfirm = async () => {
        try {
            const token = localStorage.getItem("token");

            const response = await axios.post('http://localhost:8080/api/withdraw/confirm',
            { token: qrId},
            {
                headers: {Authorization: `Bearer ${token}`
            }
        });
        console.log("Withdraw was successful.", response.data);
        setConfirmed(true);
    } catch (error) {
        console.error('Confirmation failed!', error);
        alert("Confirmation failed!")
    }
    }

    return (
        <div>
            <h2>Withdraw money with QR</h2>
            <input
                type="number"
                placeholder="Enter amount"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
            />
            <button onClick={handleWithdrawQR}>Generate qr</button>

            { qrId && (
                <div style={{marginTop: '1rem'}}>
                    <p>QR ID: {qrId} </p>
                    <QRCodeSVG value={qrId} size={128}/>
                    </div>
            )}

            {qrId && !confirmed && (
                <button onClick= {handleWithdrawConfirm}>Confirm Withdraw</button>
            )}

            {confirmed && <p style={{color: "green"}}>Deposit Confirm</p>}
            
        </div>
    );

}

export default WithdrawPage;