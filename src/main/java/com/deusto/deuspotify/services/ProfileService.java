package com.deusto.deuspotify.services;

import com.deusto.deuspotify.model.Profile;
import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.repositories.ProfileRepository;
import com.deusto.deuspotify.repositories.SongRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Lazy;
import com.deusto.deuspotify.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

/**
 * @class ProfileService
 * @brief Service for managing user profiles, authentication and user details for Spring Security.
 *
 * This service provides methods for registering, retrieving, updating, and deleting user profiles.
 * It also supports authentication via Spring Security and token-based user identification using JWT.
 */

@Service
public class ProfileService implements UserDetailsService {

    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Constructor for ProfileService.
     * 
     * @param profileRepository Repository to manage Profile entities.
     * @param passwordEncoder Password encoder for secure password storage.
     */
    @Autowired
    private SongRepository songRepository;

    public ProfileService(ProfileRepository profileRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @brief Load a user by their username for Spring Security.
     * @param username Username of the user.
     * @return UserDetails object containing user information.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Profile profile = profileRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(profile.getUsername())
                .password(profile.getPassword())
                .roles(profile.isAdmin() ? "ADMIN" : "USER")
                .build();
    }

    /**
     * @brief Retrieve all user profiles.
     * @return List of all Profile entities.
     */
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    /**
     * @brief Retrieve a profile by its ID.
     * @param id ID of the profile.
     * @return Optional containing the Profile if found.
     */
    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    /**
     * @brief Register a new user with encoded password.
     * @param profile Profile object containing registration data.
     * @return The saved Profile object.
     * @throws RuntimeException if the username already exists.
     */
    @Transactional
    public Profile registerUser(Profile profile) {
        if (profileRepository.findByUsername(profile.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists.");
        }
        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        return profileRepository.save(profile);
    }

    /**
     * @brief Update an existing user profile.
     * @param id             ID of the profile to update.
     * @param updatedProfile New profile data.
     * @return Optional containing the updated Profile.
     */
    @Transactional
    public Optional<Profile> updateProfile(Long id, Profile updatedProfile) {
        return profileRepository.findById(id).map(profile -> {
            profile.setUsername(updatedProfile.getUsername());
            if (!updatedProfile.getPassword().isEmpty()) {
                profile.setPassword(passwordEncoder.encode(updatedProfile.getPassword()));
            }
            profile.setEmail(updatedProfile.getEmail());
            profile.setFriendsList(updatedProfile.getFriendsList());
            profile.setFavouriteSongs(updatedProfile.getFavouriteSongs());
            profile.setPlaylists(updatedProfile.getPlaylists());
            profile.setAdmin(updatedProfile.isAdmin());
            return profileRepository.save(profile);
        });
    }

    /**
     * @brief Delete a user profile.
     * @param id ID of the profile to delete.
     * @return true if the profile was deleted, false otherwise.
     */
    @Transactional
    public boolean deleteProfile(Long id) {
        if (profileRepository.existsById(id)) {
            profileRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * @brief Validate user login by comparing raw and encoded passwords.
     * @param username The username.
     * @param password The raw password.
     * @return Optional containing the Profile if login is successful.
     */
    public Optional<Profile> login(String username, String password) {
        return profileRepository.findByUsername(username)
                .filter(profile -> passwordEncoder.matches(password, profile.getPassword()));
    }

    /**
     * @brief Extract and return the currently authenticated user based on the JWT token.
     * @param request The HTTP request containing the Authorization header.
     * @return The authenticated Profile.
     * @throws RuntimeException if no token is found or the user does not exist.
     */
    public Profile getAuthenticatedUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("No JWT token found");
        }
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        return profileRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * @brief Retrieve a profile by its username.
     * @param username The username to search for.
     * @return Optional containing the Profile if found.
     */
    public Optional<Profile> getProfileByUsername(String username) {
        return profileRepository.findByUsername(username);
    }

    /**
     * @brief Add the specified song to the user's favourites.
     * @param username The username of the user.
     * @param songId   The ID of the song to add.
     */
    @Transactional
    public void addFavouriteSong(String username, Long songId) {
        Profile profile = profileRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        Song song = songRepository.findById(songId)
            .orElseThrow(() -> new RuntimeException("Song not found: " + songId));
        profile.getFavouriteSongs().add(song);
        profileRepository.save(profile);
    }

    /**
     * @brief Remove the specified song from the user's favourites.
     * @param username The username of the user.
     * @param songId   The ID of the song to remove.
     */
    @Transactional
    public void removeFavouriteSong(String username, Long songId) {
        Profile profile = profileRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        Song song = songRepository.findById(songId)
            .orElseThrow(() -> new RuntimeException("Song not found: " + songId));
        profile.getFavouriteSongs().remove(song);
        profileRepository.save(profile);
    }

}
