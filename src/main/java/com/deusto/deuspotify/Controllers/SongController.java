package com.deusto.deuspotify.Controllers;

import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.services.DeuspotifyServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/songs")
@CrossOrigin
public class SongController {
    
    @Autowired
    private DeuspotifyServiceImpl deuspotifyServiceImpl;
    
    @GetMapping
    public List<Song> getAllSongs() {
        return deuspotifyServiceImpl.retrieveAllSongs();
    }

    @GetMapping("/{id}")
    public Optional<Song> getSongById(@PathVariable Long id) {
        return deuspotifyServiceImpl.findSong(id);
    }

    @PostMapping
    public Song addSong(@RequestBody Song song) {
        return deuspotifyServiceImpl.addSong(song);
    }
}
