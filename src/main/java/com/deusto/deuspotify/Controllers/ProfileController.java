package com.deusto.deuspotify.controllers;

import com.deusto.deuspotify.model.Profile;
import com.deusto.deuspotify.services.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.io.IOException;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<List<Profile>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profile> getProfileById(@PathVariable Long id) {
        Optional<Profile> profile = profileService.getProfileById(id);
        return profile.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Nuevo endpoint para obtener el perfil por nombre de usuario
    @GetMapping("/username/{username}")
    public ResponseEntity<Profile> getProfileByUsername(@PathVariable String username) {
        Optional<Profile> profile = profileService.getProfileByUsername(username);
        return profile.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createProfile(@RequestBody Profile profile) {
        try {
            Profile savedProfile = profileService.registerUser(profile);
            return ResponseEntity.ok(savedProfile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody Profile updatedProfile) {
        return profileService.updateProfile(id, updatedProfile)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id) {
        if (profileService.deleteProfile(id)) {
            return ResponseEntity.ok("Perfil eliminado correctamente.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Profile loginRequest) {
        Optional<Profile> profile = profileService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return profile.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(401).body(null)); // Devolvemos `null` en lugar de un String
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadProfileImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        Optional<Profile> optionalProfile = profileService.getProfileById(id);
        if (!optionalProfile.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Profile profile = optionalProfile.get();

        try {
            String uploadDir = "src/main/resources/static/images/subidas";
            Files.createDirectories(Paths.get(uploadDir));

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
            Path filePath = Paths.get(uploadDir, filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Ruta relativa accesible por el navegador
            String imageUrl = "/images/subidas/" + filename;
            profile.setProfileImageUrl(imageUrl);
            profileService.saveProfile(profile);

            return ResponseEntity.ok(profile);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al guardar la imagen: " + e.getMessage());
        }
    }
}
