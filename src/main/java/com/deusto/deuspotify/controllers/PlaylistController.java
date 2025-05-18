/**
 * @file PlaylistController.java
 * @brief REST controller for managing playlists.
 */

package com.deusto.deuspotify.controllers;

import com.deusto.deuspotify.model.Playlist;
import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.services.DeuspotifyService;
import com.deusto.deuspotify.services.ProfileService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @class PlaylistController
 * @brief Controller that handles HTTP requests related to playlists.
 */
@RestController
@RequestMapping("/api/playlists")
@CrossOrigin
public class PlaylistController {

    private final DeuspotifyService deuspotifyService;
    private final ProfileService profileService;

    /**
     * @brief Constructor for PlaylistController.
     * @param deuspotifyService Service for managing playlists and songs.
     * @param profileService Service for managing user profiles.
     */
    public PlaylistController(DeuspotifyService deuspotifyService, ProfileService profileService) {
        this.deuspotifyService = deuspotifyService;
        this.profileService = profileService;
    }

    /**
     * @brief Retrieves all playlists.
     * @return List of all playlists.
     */
    @GetMapping
    public List<Playlist> getAllPlaylists() {
        return deuspotifyService.retrieveAllPlaylists();
    }

    /**
     * @brief Retrieves playlists that belong to a given username.
     * @param username The username of the owner.
     * @return List of playlists owned by the user.
     */
    @GetMapping("/user/{username}")
    public List<Playlist> getPlaylistsByUsername(@PathVariable String username) {
        return deuspotifyService.retrieveAllPlaylists().stream()
                .filter(p -> p.getOwners().contains(username))
                .toList();
    }

    /**
     * @brief Retrieves a playlist by its ID.
     * @param id The ID of the playlist.
     * @return The playlist, if found.
     */
    @GetMapping("/{id}")
    public Optional<Playlist> getPlaylistById(@PathVariable Long id) {
        return deuspotifyService.findPlaylist(id);
    }

    /**
     * @brief Creates a new playlist.
     * @param playlist Playlist object to create.
     * @return The saved playlist.
     */
    @PostMapping
    public Playlist createPlaylist(@RequestBody Playlist playlist) {
        return deuspotifyService.addPlaylist(playlist);
    }

    /**
     * @brief Updates an existing playlist.
     * @param id ID of the playlist to update.
     * @param playlist The playlist data to update with.
     * @return The updated playlist.
     */
    @PutMapping("/{id}")
    public Playlist updatePlaylist(@PathVariable Long id, @RequestBody Playlist playlist) {
        return deuspotifyService.updatePlaylist(id, playlist);
    }

    /**
     * @brief Deletes a playlist by ID.
     * @param id ID of the playlist to delete.
     */
    @DeleteMapping("/{id}")
    public void deletePlaylist(@PathVariable Long id) {
        deuspotifyService.deletePlaylist(id);
    }

    /**
     * @brief Updates the list of songs in a playlist.
     * @param id ID of the playlist.
     * @param songIds List of song IDs to assign to the playlist.
     * @return Updated playlist with new song list.
     */
    @PutMapping("/{id}/songs")
    public Playlist updatePlaylistSongs(@PathVariable Long id, @RequestBody List<Long> songIds) {
        Playlist playlist = deuspotifyService.findPlaylist(id)
            .orElseThrow(() -> new RuntimeException("Playlist no encontrada"));

        List<Song> songs = deuspotifyService.findSongsByIds(songIds);
        playlist.setSongs(songs);

        return deuspotifyService.updatePlaylist(id, playlist);
    }
}
