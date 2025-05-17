package com.deusto.deuspotify.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Utility class for handling JSON Web Tokens (JWT).
 * <p>
 * Provides methods for generating, parsing, and validating JWT tokens using
 * a secure secret key and the HS256 signing algorithm.
 * </p>
 */
@Component
public class JwtUtil {

    /** Secret key used for signing JWT tokens. */
    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Secure key

    /**
     * Generates a JWT token for a given username.
     *
     * @param username the username to include in the token
     * @return a signed JWT token string
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * Extracts the username (subject) from a JWT token.
     *
     * @param token the JWT token
     * @return the username extracted from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from a JWT token using a resolver function.
     *
     * @param token the JWT token
     * @param claimsResolver function to extract the claim
     * @param <T> the type of the claim
     * @return the extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from a JWT token.
     *
     * @param token the JWT token
     * @return the full claims object
     */
    private Claims extractAllClaims(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // Use parserBuilder()
                .build()                   // Build before parsing
                .parseClaimsJws(token);
        return claimsJws.getBody();
    }

    /**
     * Validates a JWT token by comparing the extracted username with the provided one.
     *
     * @param token the JWT token
     * @param username the expected username
     * @return true if the token is valid and matches the username; false otherwise
     */
    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username);
    }
}
