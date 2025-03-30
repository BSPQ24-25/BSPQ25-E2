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

    // Playlists
    List<Playlist> retrieveAllPlaylists();
    Optional<Playlist> findPlaylist(Long id);
    Playlist addPlaylist(Playlist playlist);
}
