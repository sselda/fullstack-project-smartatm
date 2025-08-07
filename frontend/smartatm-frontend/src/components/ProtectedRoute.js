import React from "react";
import { Navigate, useLocation } from "react-router-dom";

function ProtectedRoute({ children }) {
    const token = localStorage.getItem("token");
    const location = useLocation();

    if(!token) {
        console.log("Kullanıcı girişi yok, login sayfasına yönlendiriliyor:", location.pathname);
        return <Navigate to="/login" state={{ from: location}} replace />
    }
    return children;
}

export default ProtectedRoute;