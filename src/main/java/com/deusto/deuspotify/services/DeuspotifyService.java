package com.deusto.deuspotify.services;

import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.model.Playlist;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DeuspotifyService {
    // Songs
    List<Song> retrieveAllSongs();
    Optional<Song> findSong(Long id);
    Song addSong(Song song);
    Song updateSong(Long id, Song song);
    void deleteSong(Long id);
    Song addSongWithFile(String name, String album, String artists, String genres, double duration, Date dateOfRelease, MultipartFile file);
    Song createSharedSong(String name, String album, String artists, String genres, double duration, Date dateOfRelease, String filePath);

    // Playlists
    List<Playlist> retrieveAllPlaylists();
    Optional<Playlist> findPlaylist(Long id);
    Playlist addPlaylist(Playlist playlist);
    Playlist updatePlaylist(Long id, Playlist playlist);
    void deletePlaylist(Long id);
    List<Song> findSongsByIds(List<Long> songIds);
    Playlist updatePlaylistSongs(Long playlistId, List<Long> songIds);
    String saveUploadedFile(MultipartFile file);
}
