package com.deusto.deuspotify.services;

import com.deusto.deuspotify.model.Profile;
import com.deusto.deuspotify.repositories.ProfileRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService implements UserDetailsService {
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

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

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    @Transactional
    public Profile registerUser(Profile profile) {
        // Verificar si el usuario ya existe
        if (profileRepository.findByUsername(profile.getUsername()).isPresent()) {
            throw new RuntimeException("El usuario ya existe.");
        }
        
        // Hashear la contraseña antes de guardar
        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        
        return profileRepository.save(profile);
    }
}
