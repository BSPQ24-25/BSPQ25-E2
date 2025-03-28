package com.deusto.deuspotify.services;

import com.deusto.deuspotify.model.Profile;
import com.deusto.deuspotify.repositories.ProfileRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;

@Service
public class ProfileService implements UserDetailsService {
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    // Usamos @Lazy para evitar que la inyección de ProfileService ocurra antes de tiempo
    public ProfileService(ProfileRepository profileRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

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

    public Profile registerUser(Profile profile) {
        profile.setPassword(passwordEncoder.encode(profile.getPassword())); // Hash password
        return profileRepository.save(profile);
    }
}