/**
 * @file AuthController.java
 * @brief Controller that handles authentication endpoints like login, register, and logout.
 */

package com.deusto.deuspotify.controllers;

import com.deusto.deuspotify.model.Profile;
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
 * @brief REST controller for managing authentication (login, registration, logout).
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final ProfileService profileService;

    @Autowired
    private final JwtUtil jwtUtil;

    /**
     * @brief Constructor for AuthController.
     * @param authenticationManager The Spring Security authentication manager.
     * @param profileService Service to manage user profiles.
     * @param jwtUtil Utility to handle JWT token generation.
     */
    public AuthController(AuthenticationManager authenticationManager, ProfileService profileService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.profileService = profileService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * @brief Authenticates a user and returns a JWT token.
     * @param request A map containing "username" and "password".
     * @return A response containing the JWT token.
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
     * @brief Registers a new user profile.
     * @param profile The profile object to register.
     * @return The registered profile.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Profile profile) {
        return ResponseEntity.ok(profileService.registerUser(profile));
    }

    /**
     * @brief Simulates logout operation.
     * @return A response confirming logout.
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Token invalidation (e.g. blacklist) can be implemented here
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}
