package com.deusto.deuspotify.Controllers;

import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.services.DeuspotifyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;
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
    
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Song addSongWithFile(
            @RequestParam("name") String name,
            @RequestParam("album") String album,
            @RequestParam("artists") String artists, // comma separated
            @RequestParam("genres") String genres,   // comma separated
            @RequestParam("duration") double duration,
            @RequestParam("date_of_release") @DateTimeFormat(pattern="yyyy-MM-dd") Date dateOfRelease,
            @RequestParam("file") MultipartFile file) {
        return deuspotifyServiceImpl.addSongWithFile(name, album, artists, genres, duration, dateOfRelease, file);
    }

    @PutMapping("/{id}")
    public Song updateSong(@PathVariable Long id, @RequestBody Song song) {
        return deuspotifyServiceImpl.updateSong(id, song);
    }

    @DeleteMapping("/{id}")
    public void deleteSong(@PathVariable Long id) {
        deuspotifyServiceImpl.deleteSong(id);
    }
}
