package com.deusto.deuspotify.Controllers;

import com.deusto.deuspotify.model.Profile;
import com.deusto.deuspotify.repositories.ProfileRepository;
import com.deusto.deuspotify.security.JwtUtil;
import com.deusto.deuspotify.services.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final ProfileService profileService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, ProfileService profileService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.profileService = profileService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        String token = jwtUtil.generateToken(username);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Profile profile) {
        return ResponseEntity.ok(profileService.registerUser(profile));
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Aquí podrías implementar invalidación del token si usas un blacklist
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}