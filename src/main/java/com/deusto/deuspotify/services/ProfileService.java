package com.deusto.deuspotify.services;

import com.deusto.deuspotify.model.Profile;
import com.deusto.deuspotify.repositories.ProfileRepository;
import com.deusto.deuspotify.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing user profiles, authentication, and user-related logic.
 */
@Service
public class ProfileService implements UserDetailsService {

    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    /**
     * Constructor for ProfileService.
     *
     * @param profileRepository the profile repository
     * @param passwordEncoder   the password encoder
     */
    public ProfileService(ProfileRepository profileRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Setter for injecting JwtUtil bean.
     *
     * @param jwtUtil the JWT utility component
     */
    @Autowired
    public void setJwtUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Loads user details by username for Spring Security authentication.
     *
     * @param username the username
     * @return UserDetails object
     * @throws UsernameNotFoundException if the user does not exist
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
     * Retrieves all profiles in the system.
     *
     * @return list of all profiles
     */
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    /**
     * Finds a profile by ID.
     *
     * @param id the profile ID
     * @return optional containing the profile if found
     */
    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    /**
     * Registers a new user with encoded password.
     *
     * @param profile the profile to register
     * @return the saved profile
     */
    @Transactional
    public Profile registerUser(Profile profile) {
        if (profileRepository.findByUsername(profile.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists.");
        }
        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        return profileRepository.save(profile);
    }

    /**
     * Updates a profile by its ID.
     *
     * @param id              the profile ID
     * @param updatedProfile  the updated profile data
     * @return optional containing the updated profile
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
     * Deletes a profile by its ID.
     *
     * @param id the profile ID
     * @return true if deleted, false if not found
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
     * Attempts login by validating username and password.
     *
     * @param username the username
     * @param password the raw password
     * @return optional profile if credentials are valid
     */
    public Optional<Profile> login(String username, String password) {
        return profileRepository.findByUsername(username)
                .filter(profile -> passwordEncoder.matches(password, profile.getPassword()));
    }

    /**
     * Retrieves the authenticated profile from the JWT in the request.
     *
     * @param request the HTTP request
     * @return the authenticated profile
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
}
