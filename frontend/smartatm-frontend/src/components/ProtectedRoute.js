import React from "react";
import { Navigate, useLocation } from "react-router-dom";

function ProtectedRoute({ children }) {
    const token = localStorage.getItem("token");
    const location = useLocation();

    if(!token) {
        console.log("No user login, redirecting to the login page:", location.pathname);
        return <Navigate to="/login" state={{ from: location}} replace />
    }
    return children;
}

export default ProtectedRoute;
