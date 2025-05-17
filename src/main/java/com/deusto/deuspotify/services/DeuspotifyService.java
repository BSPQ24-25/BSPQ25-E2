package com.deusto.deuspotify.services;

import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.model.Playlist;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service interface defining operations for managing songs and playlists
 * in the Deuspotify application.
 */
public interface DeuspotifyService {

    // ======================
    //        SONGS
    // ======================

    /**
     * Retrieves a list of all songs in the system.
     *
     * @return a list of {@link Song} objects
     */
    List<Song> retrieveAllSongs();

    /**
     * Finds a song by its unique ID.
     *
     * @param id the ID of the song
     * @return an {@link Optional} containing the song if found, or empty if not found
     */
    Optional<Song> findSong(Long id);

    /**
     * Adds a new song to the system.
     *
     * @param song the {@link Song} object to add
     * @return the added song
     */
    Song addSong(Song song);

    /**
     * Updates an existing song by ID.
     *
     * @param id the ID of the song to update
     * @param song the updated song data
     * @return the updated {@link Song} object
     */
    Song updateSong(Long id, Song song);

    /**
     * Deletes a song by its ID.
     *
     * @param id the ID of the song to delete
     */
    void deleteSong(Long id);

    /**
     * Adds a new song with an uploaded audio file.
     *
     * @param name the name of the song
     * @param album the album name
     * @param artists comma-separated list of artists
     * @param genres comma-separated list of genres
     * @param duration the duration of the song in seconds
     * @param dateOfRelease the release date of the song
     * @param file the audio file to upload
     * @return the created {@link Song}
     */
    Song addSongWithFile(String name, String album, String artists, String genres, double duration, Date dateOfRelease, MultipartFile file);

    /**
     * Creates a new song using an existing file path.
     *
     * @param name the name of the song
     * @param album the album name
     * @param artists comma-separated list of artists
     * @param genres comma-separated list of genres
     * @param duration the duration of the song in seconds
     * @param dateOfRelease the release date of the song
     * @param filePath the path to the audio file
     * @return the created {@link Song}
     */
    Song createSharedSong(String name, String album, String artists, String genres, double duration, Date dateOfRelease, String filePath);

    // ======================
    //      PLAYLISTS
    // ======================

    /**
     * Retrieves all playlists available in the system.
     *
     * @return a list of {@link Playlist} objects
     */
    List<Playlist> retrieveAllPlaylists();

    /**
     * Finds a playlist by its ID.
     *
     * @param id the ID of the playlist
     * @return an {@link Optional} containing the playlist if found, or empty if not
     */
    Optional<Playlist> findPlaylist(Long id);

    /**
     * Adds a new playlist to the system.
     *
     * @param playlist the {@link Playlist} object to add
     * @return the added playlist
     */
    Playlist addPlaylist(Playlist playlist);

    /**
     * Updates a playlist by its ID.
     *
     * @param id the ID of the playlist to update
     * @param playlist the updated playlist data
     * @return the updated {@link Playlist}
     */
    Playlist updatePlaylist(Long id, Playlist playlist);

    /**
     * Deletes a playlist by its ID.
     *
     * @param id the ID of the playlist to delete
     */
    void deletePlaylist(Long id);

    /**
     * Finds multiple songs by their IDs.
     *
     * @param songIds a list of song IDs
     * @return a list of {@link Song} objects
     */
    List<Song> findSongsByIds(List<Long> songIds);

    /**
     * Updates the list of songs in a playlist by replacing them with songs identified by their IDs.
     *
     * @param playlistId the ID of the playlist to update
     * @param songIds the list of song IDs to set in the playlist
     * @return the updated {@link Playlist}
     */
    Playlist updatePlaylistSongs(Long playlistId, List<Long> songIds);
}
