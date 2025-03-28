package com.deusto.deuspotify;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.deusto.deuspotify.repositories.SongRepository;
import com.deusto.deuspotify.repositories.ProfileRepository;
import com.deusto.deuspotify.repositories.PlaylistRepository;
import com.deusto.deuspotify.model.Playlist;
import com.deusto.deuspotify.model.Profile;
import com.deusto.deuspotify.model.Song;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SongRepository songRepository;
    private final ProfileRepository profileRepository;
    private final PlaylistRepository playlistRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(SongRepository songRepository, ProfileRepository profileRepository,
                           PlaylistRepository playlistRepository, PasswordEncoder passwordEncoder) {
        this.songRepository = songRepository;
        this.profileRepository = profileRepository;
        this.playlistRepository = playlistRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create songs only if none exist
        if (songRepository.count() == 0) {
            Song song1 = new Song();
            Song song2 = new Song(
                "Bohemian Rhapsody",
                Arrays.asList("Queen"),
                5.55,
                Arrays.asList("Rock", "Classic Rock"),
                new Date(),
                "A Night at the Opera"
            );
            songRepository.saveAll(Arrays.asList(song1, song2));
            System.out.println("Songs inserted.");
        } else {
            System.out.println("Songs already exist.");
        }

        // Create profile 'asier' only if it does not exist
        List<Profile> asierProfiles = profileRepository.findAll()
                .stream()
                .filter(profile -> "asier".equals(profile.getUsername()))
                .collect(Collectors.toList());
        if (asierProfiles.isEmpty()) {
            Profile profile1 = new Profile("asier",
                    passwordEncoder.encode("conhash"),
                    "asier@example.com",
                    Arrays.asList("amigo1", "amigo2"),
                    null, null, false);
            profileRepository.save(profile1);
            System.out.println("Profile 'asier' inserted.");
        } else {
            System.out.println("Profile 'asier' already exists.");
        }

        // Create playlists only if none exist
        if (playlistRepository.count() == 0) {
            Optional<Song> song1 = songRepository.findById(1L);
            Optional<Song> song2 = songRepository.findById(2L);
            if (song1.isPresent() && song2.isPresent()) {
                Playlist playlist1 = new Playlist();
                Playlist playlist2 = new Playlist(
                    Arrays.asList("juanito99"),
                    true,
                    Arrays.asList(song1.get(), song2.get()),
                    Arrays.asList(1, 2)
                );
                playlistRepository.saveAll(Arrays.asList(playlist1, playlist2));
                System.out.println("Playlists inserted.");
            } else {
                System.out.println("No songs available for playlists.");
            }
        } else {
            System.out.println("Playlists already exist.");
        }
    }
}
