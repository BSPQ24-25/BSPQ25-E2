/**
 * @file ProfileController.java
 * @brief REST controller for managing user profiles.
 */

package com.deusto.deuspotify.controllers;

import com.deusto.deuspotify.model.Profile;
import com.deusto.deuspotify.services.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @class ProfileController
 * @brief Controller that handles HTTP requests related to user profiles.
 */
@RestController
@RequestMapping("/api/profiles")
@CrossOrigin
public class ProfileController {

    private final ProfileService profileService;

    /**
     * @brief Constructor for ProfileController.
     * @param profileService Service that handles profile logic.
     */
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * @brief Retrieves all user profiles.
     * @return ResponseEntity containing the list of profiles.
     */
    @GetMapping
    public ResponseEntity<List<Profile>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    /**
     * @brief Retrieves a user profile by ID.
     * @param id The ID of the profile.
     * @return ResponseEntity containing the profile or 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Profile> getProfileById(@PathVariable Long id) {
        Optional<Profile> profile = profileService.getProfileById(id);
        return profile.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * @brief Creates a new profile.
     * @param profile The profile to create.
     * @return ResponseEntity with the created profile or error message.
     */
    @PostMapping
    public ResponseEntity<?> createProfile(@RequestBody Profile profile) {
        try {
            Profile savedProfile = profileService.registerUser(profile);
            return ResponseEntity.ok(savedProfile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * @brief Updates an existing profile.
     * @param id ID of the profile to update.
     * @param updatedProfile New data for the profile.
     * @return ResponseEntity with the updated profile or 404 if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody Profile updatedProfile) {
        return profileService.updateProfile(id, updatedProfile)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * @brief Deletes a profile by ID.
     * @param id ID of the profile to delete.
     * @return ResponseEntity indicating success or failure.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id) {
        if (profileService.deleteProfile(id)) {
            return ResponseEntity.ok("Perfil eliminado correctamente.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * @brief Authenticates a user by username and password.
     * @param loginRequest The profile object containing login credentials.
     * @return ResponseEntity with the profile if login is successful, or 401 error.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Profile loginRequest) {
        Optional<Profile> profile = profileService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return profile.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(401).body(null));
    }

}
