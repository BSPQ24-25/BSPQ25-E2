package com.deusto.deuspotify.Controllers;

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

    @GetMapping("/{id:\\d+}")
    public Optional<Song> getSongById(@PathVariable Long id) {
        return deuspotifyServiceImpl.findSong(id);
    }

    @PostMapping
    public Song addSong(@RequestBody Song song) {
        return deuspotifyServiceImpl.addSong(song);
    }
    
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public Song addSongWithFile(
            @RequestParam("name") String name,
            @RequestParam("album") String album,
            @RequestParam("artists") String artists, // comma separated
            @RequestParam("genres") String genres,   // comma separated
            @RequestParam("duration") double duration,
            @RequestParam("date_of_release") @DateTimeFormat(pattern="yyyy-MM-dd") Date dateOfRelease,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        return deuspotifyServiceImpl.addSongWithFile(name, album, artists, genres, duration, dateOfRelease, file);
    }

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

    @PutMapping("/{id}")
    public Song updateSong(@PathVariable Long id, @RequestBody Song song) {
        Optional<Song> existingSongOpt = deuspotifyServiceImpl.findSong(id);
        if (existingSongOpt.isPresent()) {
            Song existingSong = existingSongOpt.get();
            
            // Actualizar los demás campos
            existingSong.setName(song.getName());
            existingSong.setAlbum(song.getAlbum());
            existingSong.setArtists(song.getArtists());
            existingSong.setGenres(song.getGenres());
            existingSong.setDuration(song.getDuration());
            
            // Asegurarte de actualizar la fecha correctamente
            if (song.getDateOfRelease() != null) {
                existingSong.setDateOfRelease(song.getDateOfRelease());
            }

            // Guardar la canción actualizada
            return deuspotifyServiceImpl.updateSong(id, existingSong);
        } else {
            throw new RuntimeException("Canción no encontrada");
        }
    }


    @DeleteMapping("/{id}")
    public void deleteSong(@PathVariable Long id) {
        deuspotifyServiceImpl.deleteSong(id);
    }
}
