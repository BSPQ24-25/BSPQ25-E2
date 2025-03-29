package com.deusto.deuspotify.Controllers;

import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.repositories.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/songs")
@CrossOrigin
public class SongController {
    
    @Autowired
    private SongRepository songRepository;

    @GetMapping
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Song> getSongById(@PathVariable Long id) {
        return songRepository.findById(id);
    }

    @PostMapping
    public Song addSong(@RequestBody Song song) {
        return songRepository.save(song);
    }
}
