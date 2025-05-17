package com.deusto.deuspotify.controllers;

import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.services.DeuspotifyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * REST controller for managing songs.
 * Provides endpoints to create, retrieve, update, and delete songs.
 */
@RestController
@RequestMapping("/api/songs")
@CrossOrigin
public class SongController {

    @Autowired
    private DeuspotifyServiceImpl deuspotifyServiceImpl;

    /**
     * Retrieves all songs from the database.
     * @return list of songs
     */
    @GetMapping
    public List<Song> getAllSongs() {
        return deuspotifyServiceImpl.retrieveAllSongs();
    }

    /**
     * Retrieves a song by its ID.
     * @param id the ID of the song
     * @return Optional containing the song, if found
     */
    @GetMapping("/{id:\\d+}")
    public Optional<Song> getSongById(@PathVariable Long id) {
        return deuspotifyServiceImpl.findSong(id);
    }

    /**
     * Adds a new song.
     * @param song the song to add
     * @return the added song
     */
    @PostMapping
    public Song addSong(@RequestBody Song song) {
        return deuspotifyServiceImpl.addSong(song);
    }

    /**
     * Uploads a song along with its audio file.
     * @param name name of the song
     * @param album album name
     * @param artists comma-separated artist names
     * @param genres comma-separated genres
     * @param duration duration in seconds
     * @param dateOfRelease release date
     * @param file audio file
     * @return the added song
     */
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public Song addSongWithFile(
            @RequestParam("name") String name,
            @RequestParam("album") String album,
            @RequestParam("artists") String artists,
            @RequestParam("genres") String genres,
            @RequestParam("duration") double duration,
            @RequestParam("date_of_release") @DateTimeFormat(pattern="yyyy-MM-dd") Date dateOfRelease,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        return deuspotifyServiceImpl.addSongWithFile(name, album, artists, genres, duration, dateOfRelease, file);
    }

    /**
     * Creates a shared song from an existing file path.
     * @param name name of the song
     * @param album album name
     * @param artists artists list
     * @param genres genres list
     * @param duration duration in seconds
     * @param dateOfRelease release date in yyyy-MM-dd
     * @param filePath path to the file on disk
     * @return HTML response notifying the user of success
     */
    @GetMapping("/createFromFilePath")
    public ResponseEntity<String> createSharedSong(
            @RequestParam("name") String name,
            @RequestParam("album") String album,
            @RequestParam("artists") String artists,
            @RequestParam("genres") String genres,
            @RequestParam("duration") double duration,
            @RequestParam("dateOfRelease") String dateOfRelease,
            @RequestParam("filePath") String filePath) {

        Date date = Date.from(LocalDate.parse(dateOfRelease).atStartOfDay(ZoneId.systemDefault()).toInstant());

        deuspotifyServiceImpl.createSharedSong(name, album, artists, genres, duration, date, filePath);

        String htmlResponse = """
            <html>
                <head>
                    <script type="text/javascript">
                        alert('Song correctly imported');
                        window.location.href = '/songs.html';
                    </script>
                </head>
                <body></body>
            </html>
            """;

        return ResponseEntity.ok()
                .header("Content-Type", "text/html")
                .body(htmlResponse);
    }

    /**
     * Updates a song's release date.
     * @param id song ID
     * @param dateOfReleaseMillis release date in milliseconds
     * @return ResponseEntity indicating success or failure
     */
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateSongDate(
            @PathVariable Long id,
            @RequestParam("date_of_release") long dateOfReleaseMillis) {
    
        Date releaseDate = new Date(dateOfReleaseMillis);
    
        Optional<Song> existingSongOpt = deuspotifyServiceImpl.findSong(id);
        if (existingSongOpt.isPresent()) {
            Song existingSong = existingSongOpt.get();
            existingSong.setDateOfRelease(releaseDate);
            deuspotifyServiceImpl.updateSong(id, existingSong);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a song by ID.
     * @param id the song's ID
     */
    @DeleteMapping("/{id}")
    public void deleteSong(@PathVariable Long id) {
        deuspotifyServiceImpl.deleteSong(id);
    }
}
