package com.deusto.deuspotify.services;

import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.model.Playlist;
import com.deusto.deuspotify.repositories.SongRepository;
import com.deusto.deuspotify.repositories.PlaylistRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DeuspotifyServiceImpl implements DeuspotifyService {

    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;
    
    private final String uploadDir = "uploads/songs";

    public DeuspotifyServiceImpl(SongRepository songRepository, PlaylistRepository playlistRepository) {
        this.songRepository = songRepository;
        this.playlistRepository = playlistRepository;
    }

    // Songs
    public List<Song> retrieveAllSongs() {
        return songRepository.findAll();
    }

    public Optional<Song> findSong(Long id) {
        return songRepository.findById(id);
    }

    public Song addSong(Song song) {
        return songRepository.save(song);
    }

    public Song updateSong(Long id, Song song) {
        song.setId(id);
        return songRepository.save(song);
    }

    public void deleteSong(Long id) {
        songRepository.deleteById(id);
    }

    public Song addSongWithFile(String name, String album, String artists, String genres,
                                double duration, Date dateOfRelease, MultipartFile file) {
        if (file.getContentType() == null || !file.getContentType().startsWith("audio/")) {
            throw new IllegalArgumentException("Only audio files are allowed");
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().matches(".*\\.(mp3|wav|ogg|flac)$")) {
            throw new IllegalArgumentException("Invalid audio file extension");
        }
        
        try {
            Files.createDirectories(Paths.get(uploadDir));
            String cleanedFilename = System.currentTimeMillis() + "_" + originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
            Path filePath = Paths.get(uploadDir, cleanedFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            Song song = new Song();
            song.setName(name);
            song.setAlbum(album);
            song.setArtists(Arrays.asList(artists.split("\\s*,\\s*")));
            song.setGenres(Arrays.asList(genres.split("\\s*,\\s*")));
            song.setDuration(duration);
            song.setDateOfRelease(dateOfRelease);
            song.setFilePath(cleanedFilename);
            
            return songRepository.save(song);
        } catch (IOException e) {
            throw new RuntimeException("Error saving file", e);
        }
    }

    @Override
    public Song createSharedSong(String name, String album, String artists, String genres, double duration, Date dateOfRelease, String filePath) {
        Song sharedSong = new Song();
        sharedSong.setName(name + " Shared");
        sharedSong.setAlbum(album);
        sharedSong.setArtists(new ArrayList<>(Arrays.asList(artists.split("\\s*,\\s*"))));
        sharedSong.setGenres(new ArrayList<>(Arrays.asList(genres.split("\\s*,\\s*"))));
        sharedSong.setDuration(duration);
        sharedSong.setDateOfRelease(dateOfRelease);
        sharedSong.setFilePath(filePath);
        return songRepository.save(sharedSong);
    }

    // Playlists
    public List<Playlist> retrieveAllPlaylists() {
        return playlistRepository.findAll();
    }

    public Optional<Playlist> findPlaylist(Long id) {
        return playlistRepository.findById(id);
    }

    public Playlist addPlaylist(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    public Playlist updatePlaylist(Long id, Playlist playlist) {
        playlist.setId(id);
        return playlistRepository.save(playlist);
    }

    public void deletePlaylist(Long id) {
        playlistRepository.deleteById(id);
    }
}
