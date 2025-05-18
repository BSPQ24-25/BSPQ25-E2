/**
 * @file ProfileController.java
 * @brief REST controller for managing user profiles.
 */

package com.deusto.deuspotify.controllers;

import com.deusto.deuspotify.model.Profile;
import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.services.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
     * @brief Retrieve all user profiles.
     * @return ResponseEntity containing the list of profiles.
     */
    @GetMapping
    public ResponseEntity<List<Profile>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    /**
     * @brief Retrieve a user profile by its ID.
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
     * @brief Create a new user profile.
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
     * @brief Update an existing user profile.
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
     * @brief Delete a user profile by its ID.
     * @param id ID of the profile to delete.
     * @return ResponseEntity indicating success or 404 if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id) {
        if (profileService.deleteProfile(id)) {
            return ResponseEntity.ok("Profile deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * @brief Authenticate a user by username and password.
     * @param loginRequest The profile object containing login credentials.
     * @return ResponseEntity with the profile if login is successful, or 401 error.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Profile loginRequest) {
        Optional<Profile> profile = profileService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return profile.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.status(401).body(null));
    }

    /**
     * @brief Retrieve the list of favourite songs for the given user.
     * @param username The username whose favourite songs are to be fetched.
     * @return List of Song entities marked as favourites by the user.
     * @throws UsernameNotFoundException if no profile with the given username exists.
     */
    @GetMapping("/{username}/favourite-songs")
    public List<Song> getFavouriteSongs(@PathVariable String username) {
        Profile profile = profileService.getProfileByUsername(username)
                                        .orElseThrow(() ->
                                            new UsernameNotFoundException(
                                                "No profile found for username: " + username));
        return profile.getFavouriteSongs();
    }

    /**
     * @brief Mark a song as favourite for the user.
     * @param username The username who favourites the song.
     * @param songId   The ID of the song to mark as favourite.
     * @return ResponseEntity indicating success or error.
     */
    @PostMapping("/{username}/favourite-songs/{songId}")
    public ResponseEntity<?> addFavouriteSong(
            @PathVariable String username,
            @PathVariable Long songId) {
        try {
            profileService.addFavouriteSong(username, songId);
            return ResponseEntity.ok().build();
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * @brief Remove a song from the user's favourites.
     * @param username The username who unfavourites the song.
     * @param songId   The ID of the song to remove from favourites.
     * @return ResponseEntity indicating success or error.
     */
    @DeleteMapping("/{username}/favourite-songs/{songId}")
    public ResponseEntity<?> removeFavouriteSong(
            @PathVariable String username,
            @PathVariable Long songId) {
        try {
            profileService.removeFavouriteSong(username, songId);
            return ResponseEntity.ok().build();
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
