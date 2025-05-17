package com.deusto.deuspotify.services;

import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.model.Playlist;
import com.deusto.deuspotify.repositories.SongRepository;
import com.deusto.deuspotify.repositories.PlaylistRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the DeuspotifyService interface providing the core business logic
 * for managing songs and playlists in the application.
 */
@Service
public class DeuspotifyServiceImpl implements DeuspotifyService {

    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;

    private final String uploadDir = "uploads/songs";

    /**
     * Constructor injecting required repositories.
     *
     * @param songRepository the song repository
     * @param playlistRepository the playlist repository
     */
    public DeuspotifyServiceImpl(SongRepository songRepository, PlaylistRepository playlistRepository) {
        this.songRepository = songRepository;
        this.playlistRepository = playlistRepository;
    }

    // ===================== SONGS =====================

    /**
     * Retrieves all songs stored in the database.
     *
     * @return list of all songs
     */
    @Override
    public List<Song> retrieveAllSongs() {
        return songRepository.findAll();
    }

    /**
     * Finds a specific song by ID.
     *
     * @param id the song ID
     * @return optional of song if found
     */
    @Override
    public Optional<Song> findSong(Long id) {
        return songRepository.findById(id);
    }

    /**
     * Adds a new song to the database.
     *
     * @param song the song to add
     * @return the saved song
     */
    @Override
    public Song addSong(Song song) {
        return songRepository.save(song);
    }

    /**
     * Updates an existing song by ID.
     *
     * @param id the ID of the song to update
     * @param song the song with updated fields
     * @return the updated song
     */
    @Override
    public Song updateSong(Long id, Song song) {
        song.setId(id);
        return songRepository.save(song);
    }

    /**
     * Deletes a song by its ID and removes it from all playlists that contain it.
     *
     * @param id the ID of the song to delete
     */
    @Override
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
            throw new RuntimeException("Song not found");
        }
    }

    /**
     * Adds a new song and saves an audio file to the server.
     *
     * @param name the song name
     * @param album the album name
     * @param artists comma-separated list of artists
     * @param genres comma-separated list of genres
     * @param duration the song duration in seconds
     * @param dateOfRelease the release date of the song
     * @param file the audio file
     * @return the saved song
     */
    @Override
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
            String cleanedFilename = System.currentTimeMillis() + "_" + originalFilename.replaceAll("[^a-zA-Z0-9.\\-]", "_");
            Path filePath = Paths.get(uploadDir, cleanedFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Song song = new Song(name, Arrays.asList(artists.split("\\s*,\\s*")), duration,
                    Arrays.asList(genres.split("\\s*,\\s*")), dateOfRelease, album, cleanedFilename);

            return songRepository.save(song);
        } catch (IOException e) {
            throw new RuntimeException("Error saving file", e);
        }
    }

    /**
     * Creates a copy of an existing song intended for sharing.
     *
     * @param name the song name
     * @param album the album
     * @param artists artists list
     * @param genres genres list
     * @param duration song duration
     * @param dateOfRelease release date
     * @param filePath path to the shared file
     * @return the new shared song
     */
    @Override
    public Song createSharedSong(String name, String album, String artists, String genres,
                                 double duration, Date dateOfRelease, String filePath) {
        Song sharedSong = new Song(name + " Shared", Arrays.asList(artists.split("\\s*,\\s*")),
                duration, Arrays.asList(genres.split("\\s*,\\s*")), dateOfRelease, album, filePath);
        return songRepository.save(sharedSong);
    }

    // ===================== PLAYLISTS =====================

    /**
     * Retrieves all playlists in the system.
     *
     * @return list of all playlists
     */
    @Override
    public List<Playlist> retrieveAllPlaylists() {
        return playlistRepository.findAll();
    }

    /**
     * Finds a playlist by its ID.
     *
     * @param id the playlist ID
     * @return optional playlist if found
     */
    @Override
    public Optional<Playlist> findPlaylist(Long id) {
        return playlistRepository.findById(id);
    }

    /**
     * Adds a new playlist to the system.
     *
     * @param playlist the playlist to add
     * @return the saved playlist
     */
    @Override
    public Playlist addPlaylist(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    /**
     * Updates an existing playlist, applying order rules if specified.
     *
     * @param id the ID of the playlist to update
     * @param playlist the updated playlist object
     * @return the updated playlist
     */
    @Override
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

    /**
     * Deletes a playlist by ID.
     *
     * @param id the playlist ID
     */
    @Override
    public void deletePlaylist(Long id) {
        playlistRepository.deleteById(id);
    }

    /**
     * Finds a list of songs by their IDs.
     *
     * @param songIds list of song IDs
     * @return list of found songs
     */
    @Override
    public List<Song> findSongsByIds(List<Long> songIds) {
        return songRepository.findAllById(songIds);
    }

    /**
     * Updates the list of songs in a playlist.
     *
     * @param playlistId the ID of the playlist
     * @param songIds the new list of song IDs
     * @return the updated playlist
     */
    @Override
    public Playlist updatePlaylistSongs(Long playlistId, List<Long> songIds) {
        Optional<Playlist> playlistOptional = playlistRepository.findById(playlistId);
        if (!playlistOptional.isPresent()) {
            throw new RuntimeException("Playlist not found");
        }

        Playlist playlist = playlistOptional.get();
        List<Song> songs = findSongsByIds(songIds);
        playlist.setSongs(songs);

        return playlistRepository.save(playlist);
    }
}
