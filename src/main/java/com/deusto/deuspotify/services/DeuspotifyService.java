package com.deusto.deuspotify.services;

import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.model.Playlist;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;
import java.util.List;
import java.util.Optional;
/**
 * @interface DeuspotifyService
 * @brief Defines the service layer operations for managing songs and playlists.
 *
 * This interface provides abstract methods for CRUD operations on songs and playlists,
 * including operations for file uploads and playlist-song associations.
 */
public interface DeuspotifyService {

    // ===============================
    //            Songs
    // ===============================

    /**
     * Retrieves all songs in the system.
     * 
     * @return A list of all songs.
     */
    List<Song> retrieveAllSongs();

    /**
     * Finds a specific song by its ID.
     * 
     * @param id The ID of the song.
     * @return An Optional containing the song if found, or empty otherwise.
     */
    Optional<Song> findSong(Long id);

    /**
     * Adds a new song.
     * 
     * @param song The song to be added.
     * @return The saved song.
     */
    Song addSong(Song song);

    /**
     * Updates an existing song.
     * 
     * @param id The ID of the song to update.
     * @param song The updated song data.
     * @return The updated song.
     */
    Song updateSong(Long id, Song song);

    /**
     * Deletes a song by its ID.
     * 
     * @param id The ID of the song to delete.
     */
    void deleteSong(Long id);

    /**
     * Adds a new song along with an audio file.
     * 
     * @param name The name of the song.
     * @param album The album name.
     * @param artists Comma-separated list of artists.
     * @param genres Comma-separated list of genres.
     * @param duration Duration of the song in seconds.
     * @param dateOfRelease The release date of the song.
     * @param file The audio file to upload.
     * @return The created song.
     */
    Song addSongWithFile(String name, String album, String artists, String genres, double duration, Date dateOfRelease, MultipartFile file);

    /**
     * Creates a shared song entry from an existing file path.
     * 
     * @param name The name of the song.
     * @param album The album name.
     * @param artists Comma-separated list of artists.
     * @param genres Comma-separated list of genres.
     * @param duration Duration in seconds.
     * @param dateOfRelease Date of release.
     * @param filePath Path to the audio file.
     * @return The created Song entity.
     */
    Song createSharedSong(String name, String album, String artists, String genres, double duration, Date dateOfRelease, String filePath);


    // ===============================
    //          Playlists
    // ===============================

    /**
     * Retrieves all playlists in the system.
     * 
     * @return A list of all playlists.
     */
    List<Playlist> retrieveAllPlaylists();

    /**
     * Finds a specific playlist by its ID.
     * 
     * @param id The ID of the playlist.
     * @return An Optional containing the playlist if found, or empty otherwise.
     */
    Optional<Playlist> findPlaylist(Long id);

    /**
     * Adds a new playlist.
     * 
     * @param playlist The playlist to add.
     * @return The saved playlist.
     */
    Playlist addPlaylist(Playlist playlist);

    /**
     * Updates an existing playlist.
     * 
     * @param id The ID of the playlist to update.
     * @param playlist The updated playlist data.
     * @return The updated playlist.
     */
    Playlist updatePlaylist(Long id, Playlist playlist);

    /**
     * Deletes a playlist by its ID.
     * 
     * @param id The ID of the playlist to delete.
     */
    void deletePlaylist(Long id);

    /**
     * Finds a list of songs by their IDs.
     * 
     * @param songIds A list of song IDs.
     * @return A list of corresponding Song entities.
     */
    List<Song> findSongsByIds(List<Long> songIds);

    /**
     * Updates the list of songs in a given playlist.
     * 
     * @param playlistId The ID of the playlist.
     * @param songIds The list of song IDs to associate.
     * @return The updated playlist.
     */
    Playlist updatePlaylistSongs(Long playlistId, List<Long> songIds);
}
