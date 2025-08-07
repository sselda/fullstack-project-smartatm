package com.smartatm.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.spec.SecretKeySpec;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final String SECRET = "my-super-secret-key-for-jwt-which-is-at-least-256-bits";
    private final long EXPRATION_TIME = 1000 * 60 * 60 * 24;

    public Key getSigningKey() {
        return new SecretKeySpec(SECRET.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(String email, List<String> roles) {
        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }

public String extractUsername (String token) {
    return Jwts.parser()
            .setSigningKey(getSigningKey())
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
}

public boolean isTokenValid(String token) {
    try {
        Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token);
        return true;
    } catch (JwtException e) {
        return false;
    }
}
}