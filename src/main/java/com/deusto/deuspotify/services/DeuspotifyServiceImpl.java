package com.deusto.deuspotify.services;

import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.model.Playlist;
import com.deusto.deuspotify.repositories.SongRepository;
import com.deusto.deuspotify.repositories.PlaylistRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
/**
 * @class DeuspotifyServiceImpl
 * @brief Service implementation for managing songs and playlists in the Deuspotify application.
 *
 * This service provides functionality to retrieve, create, update, and delete songs and playlists,
 * and handles operations related to file uploads and custom ordering of playlist contents.
 */
@Service
public class DeuspotifyServiceImpl implements DeuspotifyService {

    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;

    private final String uploadDir = "uploads/songs";

    /**
     * Constructor for dependency injection.
     * 
     * @param songRepository Repository for managing Song entities.
     * @param playlistRepository Repository for managing Playlist entities.
     */
    public DeuspotifyServiceImpl(SongRepository songRepository, PlaylistRepository playlistRepository) {
        this.songRepository = songRepository;
        this.playlistRepository = playlistRepository;
    }

    /// @name Song Operations
    /// @{

    /** {@inheritDoc} */
    public List<Song> retrieveAllSongs() {
        return songRepository.findAll();
    }

    /** {@inheritDoc} */
    public Optional<Song> findSong(Long id) {
        return songRepository.findById(id);
    }

    /** {@inheritDoc} */
    public Song addSong(Song song) {
        return songRepository.save(song);
    }

    /** {@inheritDoc} */
    public Song updateSong(Long id, Song song) {
        song.setId(id);
        return songRepository.save(song);
    }

    /** {@inheritDoc} */
    public void deleteSong(Long id) {
        List<Playlist> playlists = playlistRepository.findAll();
        Optional<Song> songOpt = songRepository.findById(id);
        
        if (songOpt.isPresent()) {
            Song song = songOpt.get();
            for (Playlist playlist : playlists) {
                if (playlist.getSongs().contains(song)) {
                    playlist.getSongs().remove(song);
                    playlistRepository.save(playlist);
                }
            }
            songRepository.delete(song);
        } else {
            throw new RuntimeException("Canción no encontrada");
        }
    }

    /**
     * Adds a song with an associated audio file.
     *
     * @param name Song name.
     * @param album Album name.
     * @param artists Comma-separated artist names.
     * @param genres Comma-separated genre names.
     * @param duration Duration of the song in seconds.
     * @param dateOfRelease Release date.
     * @param file Audio file to upload.
     * @return The created Song.
     */
    public Song addSongWithFile(String name, String album, String artists, String genres,
                                double duration, Date dateOfRelease, MultipartFile file) {
        if (file.getContentType() == null || !file.getContentType().startsWith("audio/")) {
            throw new IllegalArgumentException("Only audio files are allowed");
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().matches(".*\\.(mp3|wav|ogg|flac)$")) {
            throw new IllegalArgumentException("Invalid audio file extension");
        }

        try {
            Files.createDirectories(Paths.get(uploadDir));
            String cleanedFilename = System.currentTimeMillis() + "_" + originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
            Path filePath = Paths.get(uploadDir, cleanedFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Song song = new Song();
            song.setName(name);
            song.setAlbum(album);
            song.setArtists(Arrays.asList(artists.split("\\s*,\\s*")));
            song.setGenres(Arrays.asList(genres.split("\\s*,\\s*")));
            song.setDuration(duration);
            song.setDateOfRelease(dateOfRelease);
            song.setFilePath(cleanedFilename);

            return songRepository.save(song);
        } catch (IOException e) {
            throw new RuntimeException("Error saving file", e);
        }
    }

    /**
     * Creates a shared song using a provided file path (without uploading).
     *
     * @param name Song name.
     * @param album Album name.
     * @param artists Comma-separated artist names.
     * @param genres Comma-separated genre names.
     * @param duration Song duration in seconds.
     * @param dateOfRelease Release date.
     * @param filePath Path to the existing audio file.
     * @return The created Song.
     */
    public Song createSharedSong(String name, String album, String artists, String genres, double duration, Date dateOfRelease, String filePath) {
        Song sharedSong = new Song();
        sharedSong.setName(name + " Shared");
        sharedSong.setAlbum(album);
        sharedSong.setArtists(new ArrayList<>(Arrays.asList(artists.split("\\s*,\\s*"))));
        sharedSong.setGenres(new ArrayList<>(Arrays.asList(genres.split("\\s*,\\s*"))));
        sharedSong.setDuration(duration);
        sharedSong.setDateOfRelease(dateOfRelease);
        sharedSong.setFilePath(filePath);
        return songRepository.save(sharedSong);
    }

    /// @}
    /// @name Playlist Operations
    /// @{

    /** {@inheritDoc} */
    public List<Playlist> retrieveAllPlaylists() {
        return playlistRepository.findAll();
    }

    /** {@inheritDoc} */
    public Optional<Playlist> findPlaylist(Long id) {
        return playlistRepository.findById(id);
    }

    /** {@inheritDoc} */
    public Playlist addPlaylist(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    /**
     * Updates a playlist and optionally reorders its songs.
     *
     * @param id Playlist ID.
     * @param playlist Playlist data including optional reordering.
     * @return The updated playlist.
     */
    public Playlist updatePlaylist(Long id, Playlist playlist) {
        playlist.setId(id);

        if (playlist.getSongs() != null && playlist.getOrder() != null) {
            String orderType = playlist.getOrderType();

            if ("duration".equalsIgnoreCase(orderType)) {
                playlist.getSongs().sort((s1, s2) -> Double.compare(s2.getDuration(), s1.getDuration()));
            } else if ("release_date".equalsIgnoreCase(orderType)) {
                playlist.getSongs().sort((s1, s2) -> {
                    if (s1.getDateOfRelease() == null || s2.getDateOfRelease() == null) {
                        return 0;
                    }
                    return s2.getDateOfRelease().compareTo(s1.getDateOfRelease());
                });
            }
        }

        return playlistRepository.save(playlist);
    }

    /** {@inheritDoc} */
    public void deletePlaylist(Long id) {
        playlistRepository.deleteById(id);
    }

    /** {@inheritDoc} */
    public List<Song> findSongsByIds(List<Long> songIds) {
        return songRepository.findAllById(songIds);
    }

    /** {@inheritDoc} */
    public Playlist updatePlaylistSongs(Long playlistId, List<Long> songIds) {
        Optional<Playlist> playlistOptional = playlistRepository.findById(playlistId);
        if (!playlistOptional.isPresent()) {
            throw new RuntimeException("Playlist no encontrada");
        }

        Playlist playlist = playlistOptional.get();
        List<Song> songs = findSongsByIds(songIds);
        playlist.setSongs(songs);

        return playlistRepository.save(playlist);
    }

    /// @}
}
