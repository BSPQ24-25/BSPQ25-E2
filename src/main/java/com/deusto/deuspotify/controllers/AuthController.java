/**
 * @file AuthController.java
 * @brief Handles authentication-related operations such as login, registration, and logout.
 */

package com.deusto.deuspotify.controllers;

import com.deusto.deuspotify.model.Profile;
import com.deusto.deuspotify.repositories.ProfileRepository;
import com.deusto.deuspotify.security.JwtUtil;
import com.deusto.deuspotify.services.ProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @class AuthController
 * @brief REST controller responsible for user authentication.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final ProfileService profileService;

    @Autowired
    private final JwtUtil jwtUtil;

    /**
     * Constructor for AuthController.
     * @param authenticationManager Handles authentication logic.
     * @param profileService Provides profile-related services.
     * @param jwtUtil Utility to generate JWT tokens.
     */
    public AuthController(AuthenticationManager authenticationManager, ProfileService profileService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.profileService = profileService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Authenticates the user and generates a JWT token.
     * @param request A map containing "username" and "password".
     * @return A JWT token if authentication is successful.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        String token = jwtUtil.generateToken(username);
        return ResponseEntity.ok(Map.of("token", token));
    }

    /**
     * Registers a new user.
     * @param profile The profile data to register.
     * @return The registered profile or a message.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Profile profile) {
        return ResponseEntity.ok(profileService.registerUser(profile));
    }

    /**
     * Logs the user out. (Token invalidation can be implemented here.)
     * @return A confirmation message.
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}
