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
 * @class JwtUtil
 * @brief Utility class for generating and validating JSON Web Tokens (JWT).
 *
 * This class handles the creation of JWT tokens and provides methods for extracting claims
 * and validating tokens.
 */
@Component
public class JwtUtil {

    /**
     * Secret key used to sign JWT tokens using HS256 algorithm.
     */
    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Generates a JWT token for the given username.
     * 
     * @param username The username for which the token is generated.
     * @return A signed JWT token string.
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
     * Extracts the username from the given token.
     * 
     * @param token The JWT token.
     * @return The username (subject) embedded in the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the token using a claim resolver function.
     * 
     * @param token The JWT token.
     * @param claimsResolver A function to extract a specific claim.
     * @param <T> The type of the claim.
     * @return The extracted claim value.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the token.
     * 
     * @param token The JWT token.
     * @return All claims contained in the token.
     */
    private Claims extractAllClaims(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
        return claimsJws.getBody();
    }

    /**
     * Validates the token by checking if it belongs to the provided username.
     * 
     * @param token The JWT token.
     * @param username The username to compare with the token's subject.
     * @return True if the token is valid and belongs to the username; otherwise, false.
     */
    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username);
    }
}
