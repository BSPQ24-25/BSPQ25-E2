package com.deusto.deuspotify.Controllers;

import com.deusto.deuspotify.model.Playlist;
import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.services.DeuspotifyService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/playlists")
@CrossOrigin
public class PlaylistController {

    private final DeuspotifyService deuspotifyService;

    public PlaylistController(DeuspotifyService deuspotifyService) {
        this.deuspotifyService = deuspotifyService;
    }

    @GetMapping
    public List<Playlist> getAllPlaylists() {
        return deuspotifyService.retrieveAllPlaylists();
    }

    @GetMapping("/{id}")
    public Optional<Playlist> getPlaylistById(@PathVariable Long id) {
        return deuspotifyService.findPlaylist(id);
    }

    @PostMapping
    public Playlist createPlaylist(@RequestBody Playlist playlist) {
        return deuspotifyService.addPlaylist(playlist);
    }

    @PutMapping("/{id}")
    public Playlist updatePlaylist(@PathVariable Long id, @RequestBody Playlist playlist) {
        return deuspotifyService.updatePlaylist(id, playlist);
    }

    @DeleteMapping("/{id}")
    public void deletePlaylist(@PathVariable Long id) {
        deuspotifyService.deletePlaylist(id);
    }

    @PutMapping("/{id}/songs")
    
    
    public Playlist updatePlaylistSongs(@PathVariable Long id, @RequestBody List<Long> songIds) {

        Playlist playlist = deuspotifyService.findPlaylist(id)
            .orElseThrow(() -> new RuntimeException("Playlist no encontrada"));

        List<Song> songs = deuspotifyService.findSongsByIds(songIds);
        playlist.setSongs(songs);

        return deuspotifyService.updatePlaylist(id, playlist);
    }
}
