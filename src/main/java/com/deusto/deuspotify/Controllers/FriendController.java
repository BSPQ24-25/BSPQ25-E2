package com.deusto.deuspotify.controllers;

import com.deusto.deuspotify.model.Profile;
import com.deusto.deuspotify.repositories.ProfileRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@CrossOrigin("*")
public class FriendController {

    private final ProfileRepository profileRepository;

    public FriendController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @GetMapping
    public List<Profile> getAllFriends() {
        return profileRepository.findAll();
    }

    @PostMapping
    public Profile addFriend(@RequestBody Profile profile) {
        return profileRepository.save(profile);
    }
}
