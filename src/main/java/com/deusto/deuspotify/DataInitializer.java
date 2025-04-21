package com.deusto.deuspotify;

import java.util.Arrays;
import java.util.Date;
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
        try {
            insertSongs();
            insertProfiles();
            insertPlaylists();
        } catch (Exception e) {
            System.err.println("Error al inicializar los datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void insertSongs() {
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
            System.out.println("Canciones insertadas en la BD.");
        } else {
            System.out.println("Las canciones ya existen en la BD.");
        }
    }

    private void insertProfiles() {
        if (!profileRepository.findByUsername("user").isPresent()) {
            Profile profile = new Profile("user",
                    passwordEncoder.encode("pass"),
                    "user@example.com",
                    Arrays.asList("amigo1", "amigo2"),
                    null, null, false);

            profileRepository.save(profile);
            System.out.println("Perfil 'user' insertado en la BD.");
        } else {
            System.out.println("El perfil 'user' ya existe en la BD.");
        }
    }

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
                    Arrays.asList(1, 2)
                );

                playlistRepository.save(playlist);
                System.out.println("Playlist insertada en la BD.");
            } else {
                System.out.println(" No se encontraron canciones para asociar a la playlist.");
            }
        } else {
            System.out.println("La playlist ya existe en la BD.");
        }
    }
}
