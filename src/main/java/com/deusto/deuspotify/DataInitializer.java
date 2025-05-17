/**
 * @file DataInitializer.java
 * @brief Class that initializes test data in the database when the application starts.
 */

package com.deusto.deuspotify;

import java.util.Arrays;
import java.util.Date;
import java.util.Collections;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.deusto.deuspotify.repositories.SongRepository;
import com.deusto.deuspotify.repositories.ProfileRepository;
import com.deusto.deuspotify.repositories.PlaylistRepository;
import com.deusto.deuspotify.model.Playlist;
import com.deusto.deuspotify.model.Profile;
import com.deusto.deuspotify.model.Song;

/**
 * @class DataInitializer
 * @brief Component that runs at application startup and inserts initial data into the database.
 *
 * This component inserts one song, one user profile, and one playlist if they do not already exist.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final SongRepository songRepository;
    private final ProfileRepository profileRepository;
    private final PlaylistRepository playlistRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for dependency injection.
     *
     * @param songRepository Song repository.
     * @param profileRepository Profile repository.
     * @param playlistRepository Playlist repository.
     * @param passwordEncoder Password encoder for encrypting user passwords.
     */
    public DataInitializer(SongRepository songRepository,
                           ProfileRepository profileRepository,
                           PlaylistRepository playlistRepository,
                           PasswordEncoder passwordEncoder) {
        this.songRepository = songRepository;
        this.profileRepository = profileRepository;
        this.playlistRepository = playlistRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Executes at application startup.
     *
     * @param args Command-line arguments.
     * @throws Exception In case of any error during initialization.
     */
    @Override
    public void run(String... args) throws Exception {
        try {
            insertSongs();
            insertProfiles();
            insertPlaylists();
        } catch (Exception e) {
            System.err.println("Error while initializing data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Inserts a demo song into the database if no songs exist.
     */
    private void insertSongs() {
        if (songRepository.count() == 0) {
            Song bohemian = new Song(
                "Bohemian Rhapsody",
                Arrays.asList("Queen"),
                5.55,
                Arrays.asList("Rock", "Classic Rock"),
                new Date(),
                "A Night at the Opera"
            );

            songRepository.save(bohemian);
            System.out.println("Songs inserted into the database.");
        } else {
            System.out.println("Songs already exist in the database.");
        }
    }

    /**
     * Inserts a demo user profile if it does not already exist.
     */
    private void insertProfiles() {
        if (!profileRepository.findByUsername("user").isPresent()) {
            Profile profile = new Profile(
                "user",
                passwordEncoder.encode("pass"),
                "user@example.com",
                Arrays.asList("friend1", "friend2"),
                Collections.emptyList(),
                Collections.emptyList(),
                false
            );

            profileRepository.save(profile);
            System.out.println("User profile 'user' inserted into the database.");
        } else {
            System.out.println("User profile 'user' already exists in the database.");
        }
    }

    /**
     * Inserts a demo playlist if none exist, using songs already saved in the database.
     */
    private void insertPlaylists() {
        if (playlistRepository.count() == 0) {
            Optional<Song> song1 = songRepository.findById(1L);
            Optional<Song> song2 = songRepository.findById(2L);

            if (song1.isPresent() && song2.isPresent()) {
                Playlist playlist = new Playlist(
                    "MyTestPlaylist",
                    Arrays.asList("juanito99"),
                    true,
                    Arrays.asList(song1.get(), song2.get()),
                    Arrays.asList(song1.get().getId().intValue(), song2.get().getId().intValue())
                );

                playlistRepository.save(playlist);
                System.out.println("Playlist inserted into the database.");
            } else {
                System.out.println("No songs found to associate with the playlist.");
            }
        } else {
            System.out.println("Playlists already exist in the database.");
        }
    }
}
