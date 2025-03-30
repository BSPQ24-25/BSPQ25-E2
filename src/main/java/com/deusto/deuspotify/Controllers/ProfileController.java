package com.deusto.deuspotify.controllers;

import com.deusto.deuspotify.model.Profile;
import com.deusto.deuspotify.services.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public Profile updateProfile(@PathVariable Long id, @RequestBody Profile updatedProfile) {
        return profileRepository.findById(id).map(profile -> {
            profile.setUsername(updatedProfile.getUsername());
            profile.setPassword(updatedProfile.getPassword());
            profile.setEmail(updatedProfile.getEmail());
            profile.setFriendsList(updatedProfile.getFriendsList());
            profile.setFavouriteSongs(updatedProfile.getFavouriteSongs());
            profile.setPlaylists(updatedProfile.getPlaylists());
            profile.setAdmin(updatedProfile.isAdmin());
            return profileRepository.save(profile);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteProfile(@PathVariable Long id) {
        profileRepository.deleteById(id);
    }

    @PostMapping("/login")
    public Profile login(@RequestBody Profile loginRequest) {
        return profileRepository.findAll().stream()
                .filter(profile -> profile.getUsername().equals(loginRequest.getUsername())
                        && profile.getPassword().equals(loginRequest.getPassword()))
                .findFirst()
                .orElse(null);
    }
}
