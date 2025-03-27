package com.deusto.deuspotify;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
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

    public DataInitializer(SongRepository songRepository, ProfileRepository profileRepository, PlaylistRepository playlistRepository) {
        this.songRepository = songRepository;
        this.profileRepository = profileRepository;
        this.playlistRepository = playlistRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Crear y guardar categorías
        Song song1 = new Song();
        Song song2 = new Song(
            "Bohemian Rhapsody",
            Arrays.asList("Queen"),
            5.55,
            Arrays.asList("Rock", "Classic Rock"),
            new Date(), // fecha actual
            "A Night at the Opera"
        );

        songRepository.save(song1);
        songRepository.save(song2);
        System.out.println("Canciones en BBDD: " + songRepository.count());

        
        Profile profile1 = new Profile();
        Profile profile2 = new Profile(
            "juanito99",
            "superSegura123",
            "juanito@example.com",
            Arrays.asList("amigo1", "amigo2"),
            null,
            null,
            false
        );

        profileRepository.save(profile1);
        profileRepository.save(profile2);
        System.out.println("Perfiles en BBDD: " + profileRepository.count());


        Playlist playlist1 = new Playlist();
        Playlist playlist2 = new Playlist(
            Arrays.asList("juanito99"),
            true,                        
            Arrays.asList(song1, song2),  
            Arrays.asList(1, 2)  
        );

        playlistRepository.save(playlist1);
        playlistRepository.save(playlist2);
        System.out.println("Playlists en BBDD: " + playlistRepository.count());
    }
}
