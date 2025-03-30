package com.deusto.deuspotify.services;

import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.model.Playlist;
import com.deusto.deuspotify.repositories.SongRepository;
import com.deusto.deuspotify.repositories.PlaylistRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeuspotifyServiceImpl implements DeuspotifyService {

    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;

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
}
