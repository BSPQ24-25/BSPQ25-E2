package com.deusto.deuspotify;
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
        Song song2 = new Song();
        Song song3 = new Song();
        
        Profile profile1 = new Profile();
        Profile profile2 = new Profile();
        Profile profile3 = new Profile();

        Playlist playlist1 = new Playlist();
        Playlist playlist2 = new Playlist();
        Playlist playlist3 = new Playlist();
    }
}
