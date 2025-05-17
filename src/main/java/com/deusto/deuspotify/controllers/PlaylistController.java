package com.deusto.deuspotify.controllers;

import com.deusto.deuspotify.model.Playlist;
import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.services.DeuspotifyService;
import com.deusto.deuspotify.services.ProfileService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
/**
 * REST controller for managing playlists.
 * Provides endpoints to create, read, update, and delete playlists,
 * as well as to update their associated songs.
 */
@RestController
@RequestMapping("/api/playlists")
@CrossOrigin
public class PlaylistController {

    private final DeuspotifyService deuspotifyService;
    private final ProfileService profileService;

    /**
     * Constructs a new PlaylistController with the required services.
     *
     * @param deuspotifyService Service handling playlist operations
     * @param profileService Service handling profile operations (not used in this controller)
     */
    public PlaylistController(DeuspotifyService deuspotifyService, ProfileService profileService) {
        this.deuspotifyService = deuspotifyService;
        this.profileService = profileService;
    }

    /**
     * Retrieves all playlists.
     *
     * @return a list of all playlists
     */
    @GetMapping
    public List<Playlist> getAllPlaylists() {
        return deuspotifyService.retrieveAllPlaylists();
    }

    /**
     * Retrieves a playlist by its ID.
     *
     * @param id the ID of the playlist
     * @return the playlist if found, otherwise an empty Optional
     */
    @GetMapping("/{id}")
    public Optional<Playlist> getPlaylistById(@PathVariable Long id) {
        return deuspotifyService.findPlaylist(id);
    }

    /**
     * Creates a new playlist.
     *
     * @param playlist the playlist to create
     * @return the created playlist
     */
    @PostMapping
    public Playlist createPlaylist(@RequestBody Playlist playlist) {
        return deuspotifyService.addPlaylist(playlist);
    }

    /**
     * Updates an existing playlist.
     *
     * @param id the ID of the playlist to update
     * @param playlist the updated playlist data
     * @return the updated playlist
     */
    @PutMapping("/{id}")
    public Playlist updatePlaylist(@PathVariable Long id, @RequestBody Playlist playlist) {
        return deuspotifyService.updatePlaylist(id, playlist);
    }

    /**
     * Deletes a playlist by its ID.
     *
     * @param id the ID of the playlist to delete
     */
    @DeleteMapping("/{id}")
    public void deletePlaylist(@PathVariable Long id) {
        deuspotifyService.deletePlaylist(id);
    }

    /**
     * Updates the list of songs in a given playlist.
     *
     * @param id the ID of the playlist to update
     * @param songIds list of song IDs to set in the playlist
     * @return the updated playlist
     * @throws RuntimeException if the playlist is not found
     */
    @PutMapping("/{id}/songs")
    public Playlist updatePlaylistSongs(@PathVariable Long id, @RequestBody List<Long> songIds) {
        Playlist playlist = deuspotifyService.findPlaylist(id)
            .orElseThrow(() -> new RuntimeException("Playlist not found"));

        List<Song> songs = deuspotifyService.findSongsByIds(songIds);
        playlist.setSongs(songs);

        return deuspotifyService.updatePlaylist(id, playlist);
    }
}
