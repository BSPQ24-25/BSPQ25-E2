package com.deusto.deuspotify.controllers;

import com.deusto.deuspotify.model.Profile;
import com.deusto.deuspotify.services.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing user profiles.
 * Handles CRUD operations and login for Profile entities.
 */
@RestController
@RequestMapping("/api/profiles")
@CrossOrigin
public class ProfileController {

    private final ProfileService profileService;

    /**
     * Constructs the ProfileController with a ProfileService.
     * @param profileService the service managing profile logic
     */
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * Retrieves a list of all user profiles.
     * @return ResponseEntity containing the list of profiles
     */
    @GetMapping
    public ResponseEntity<List<Profile>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    /**
     * Retrieves a profile by its ID.
     * @param id the profile's ID
     * @return ResponseEntity with the profile or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Profile> getProfileById(@PathVariable Long id) {
        Optional<Profile> profile = profileService.getProfileById(id);
        return profile.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new user profile.
     * @param profile the profile data to create
     * @return ResponseEntity with created profile or error message
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
     * Updates an existing profile by ID.
     * @param id the profile ID
     * @param updatedProfile the updated profile data
     * @return ResponseEntity with the updated profile or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody Profile updatedProfile) {
        return profileService.updateProfile(id, updatedProfile)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes a profile by ID.
     * @param id the profile ID
     * @return ResponseEntity indicating the result of the deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id) {
        if (profileService.deleteProfile(id)) {
            return ResponseEntity.ok("Profile successfully deleted.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Attempts to log in a user with given credentials.
     * @param loginRequest the profile object containing username and password
     * @return ResponseEntity with the logged-in profile or 401 if unauthorized
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Profile loginRequest) {
        Optional<Profile> profile = profileService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return profile.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(401).body(null));
    }
}
