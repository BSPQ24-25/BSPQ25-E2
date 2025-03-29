package com.deusto.deuspotify.Controllers;

import com.deusto.deuspotify.model.Profile;
import com.deusto.deuspotify.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin
public class ProfileController {

    @Autowired
    private ProfileRepository profileRepository;

    @GetMapping
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    @GetMapping("/{id}")
    public Profile getProfileById(@PathVariable Long id) {
        return profileRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Profile createProfile(@RequestBody Profile profile) {
        return profileRepository.save(profile);
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
