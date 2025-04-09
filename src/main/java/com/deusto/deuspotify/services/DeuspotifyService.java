package com.deusto.deuspotify.services;

import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.model.Playlist;

import java.util.List;
import java.util.Optional;

public interface DeuspotifyService {
    // Songs
    List<Song> retrieveAllSongs();
    Optional<Song> findSong(Long id);
    Song addSong(Song song);
    Song updateSong(Long id, Song song);
    void deleteSong(Long id);

    // Playlists
    List<Playlist> retrieveAllPlaylists();
    Optional<Playlist> findPlaylist(Long id);
    Playlist addPlaylist(Playlist playlist);
    Playlist updatePlaylist(Long id, Playlist playlist);
    void deletePlaylist(Long id);
}
