package com.deusto.deuspotify.DTO;

import java.util.Date;
import java.util.List;

/**
 * Data Transfer Object representing a song.
 * Used for transferring song data between layers of the application.
 */
public class SongDTO {

    /** Unique identifier of the song. */
    private Long id;

    /** Title of the song. */
    private String name;

    /** List of artist names who performed the song. */
    private List<String> artists;

    /** Duration of the song in seconds. */
    private double duration;

    /** List of genres associated with the song. */
    private List<String> genres;

    /** Release date of the song. */
    private Date dateOfRelease;

    /** Name of the album the song belongs to. */
    private String album;

    /** Path to the audio file or its storage location. */
    private String filePath;

    /** Default constructor. */
    public SongDTO() {}

    /**
     * Constructs a SongDTO with all attributes except the file path.
     *
     * @param id             the ID of the song
     * @param name           the name of the song
     * @param artists        the list of artists
     * @param duration       the duration of the song in seconds
     * @param genres         the genres of the song
     * @param dateOfRelease  the release date
     * @param album          the album name
     */
    public SongDTO(Long id, String name, List<String> artists, double duration, List<String> genres, Date dateOfRelease, String album) {
        this.id = id;
        this.name = name;
        this.artists = artists;
        this.duration = duration;
        this.genres = genres;
        this.dateOfRelease = dateOfRelease;
        this.album = album;
    }

    /**
     * Constructs a SongDTO with all attributes.
     *
     * @param id             the ID of the song
     * @param name           the name of the song
     * @param artists        the list of artists
     * @param duration       the duration of the song in seconds
     * @param genres         the genres of the song
     * @param dateOfRelease  the release date
     * @param album          the album name
     * @param filePath       the file path to the song
     */
    public SongDTO(Long id, String name, List<String> artists, double duration, List<String> genres, Date dateOfRelease, String album, String filePath) {
        this.id = id;
        this.name = name;
        this.artists = artists;
        this.duration = duration;
        this.genres = genres;
        this.dateOfRelease = dateOfRelease;
        this.album = album;
        this.filePath = filePath;
    }

    /** @return the song ID */
    public Long getId() { return id; }

    /** @param id the song ID */
    public void setId(Long id) { this.id = id; }

    /** @return the name of the song */
    public String getName() { return name; }

    /** @param name the name of the song */
    public void setName(String name) { this.name = name; }

    /** @return the list of artist names */
    public List<String> getArtists() { return artists; }

    /** @param artists the list of artist names */
    public void setArtists(List<String> artists) { this.artists = artists; }

    /** @return the duration in seconds */
    public double getDuration() { return duration; }

    /** @param duration the duration in seconds */
    public void setDuration(double duration) { this.duration = duration; }

    /** @return the list of genres */
    public List<String> getGenres() { return genres; }

    /** @param genres the list of genres */
    public void setGenres(List<String> genres) { this.genres = genres; }

    /** @return the release date */
    public Date getDateOfRelease() { return dateOfRelease; }

    /** @param dateOfRelease the release date */
    public void setDateOfRelease(Date dateOfRelease) { this.dateOfRelease = dateOfRelease; }

    /** @return the album name */
    public String getAlbum() { return album; }

    /** @param album the album name */
    public void setAlbum(String album) { this.album = album; }

    /** @return the file path */
    public String getFilePath() { return filePath; }

    /** @param filePath the file path */
    public void setFilePath(String filePath) { this.filePath = filePath; }
}
