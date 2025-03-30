package com.deusto.deuspotify.services;
import java.util.List;
import java.util.Optional;

import com.deusto.deuspotify.model.Song;

public interface DeuspotifyService {
    List<Song> retrieveAllSongs();
    Optional<Song> findSong(Long id);
    Song addSong(Song song);
}
