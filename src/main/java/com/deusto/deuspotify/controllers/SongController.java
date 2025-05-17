/**
 * @file SongController.java
 * @brief REST controller for managing songs in the Deuspotify application.
 */

package com.deusto.deuspotify.controllers;

import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.services.DeuspotifyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.time.*;
import org.springframework.http.ResponseEntity;

/**
 * @class SongController
 * @brief Controller that provides endpoints to manage songs including CRUD operations and file upload.
 */
@RestController
@RequestMapping("/api/songs")
@CrossOrigin
public class SongController {

    @Autowired
    private DeuspotifyServiceImpl deuspotifyServiceImpl;

    /**
     * @brief Retrieves all songs.
     * @return List of all songs in the system.
     */
    @GetMapping
    public List<Song> getAllSongs() {
        return deuspotifyServiceImpl.retrieveAllSongs();
    }

    /**
     * @brief Retrieves a song by its ID.
     * @param id ID of the song.
     * @return Optional containing the song if found.
     */
    @GetMapping("/{id:\\d+}")
    public Optional<Song> getSongById(@PathVariable Long id) {
        return deuspotifyServiceImpl.findSong(id);
    }

    /**
     * @brief Adds a new song without a file upload.
     * @param song The song object to be added.
     * @return The saved song.
     */
    @PostMapping
    public Song addSong(@RequestBody Song song) {
        return deuspotifyServiceImpl.addSong(song);
    }

    /**
     * @brief Adds a new song with an audio file upload.
     * @param name Name of the song.
     * @param album Name of the album.
     * @param artists Comma-separated list of artists.
     * @param genres Comma-separated list of genres.
     * @param duration Duration in seconds.
     * @param dateOfRelease Release date.
     * @param file Multipart file upload of the song.
     * @return The saved song.
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
     * @brief Creates a shared song using metadata and file path.
     * @param name Name of the song.
     * @param album Album name.
     * @param artists Artists involved.
     * @param genres Music genres.
     * @param duration Duration in seconds.
     * @param dateOfRelease Date string in yyyy-MM-dd format.
     * @param filePath Local path to the song file.
     * @return HTML response that redirects to the songs page.
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
     * @brief Updates the release date of a song.
     * @param id ID of the song.
     * @param dateOfReleaseMillis New release date in milliseconds.
     * @return ResponseEntity with status OK or NotFound.
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
     * @brief Deletes a song by ID.
     * @param id ID of the song.
     */
    @DeleteMapping("/{id}")
    public void deleteSong(@PathVariable Long id) {
        deuspotifyServiceImpl.deleteSong(id);
    }
}
