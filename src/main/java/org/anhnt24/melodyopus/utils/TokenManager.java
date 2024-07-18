package org.anhnt24.melodyopus.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.io.Decoders;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

// this class handles the generation and validation of JWT tokens.
// It uses a secret key for signing and verifying the tokens.
@Component
public class TokenManager implements Serializable {

    private static final long serialVersionUID = 7008375124389347049L;
    public static final long TOKEN_VALIDITY = 10000 * 60 * 60; // about 400 days
    @Value("${secret}")
    private String jwtSecret;

    // method to get the signing key from the secret
    private Key getSigningKey() {
        String encodedKey = Base64.getEncoder().encodeToString(this.jwtSecret.getBytes(StandardCharsets.UTF_8));
        byte[] keyBytes = Decoders.BASE64.decode(encodedKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // method to generate a JWT token for a user
    public String generateJwtToken(UserDetails userDetails) {
        String token = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(getSigningKey()).compact();
        return token;
    }


    public Boolean validateJwtToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        Claims claims = Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token).getBody();
        Boolean isTokenExpired = claims.getExpiration().before(new Date());
        return (username.equals(userDetails.getUsername()) && !isTokenExpired);
    }

    public String getUsernameFromToken(String token) {
        final Claims claims = Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}