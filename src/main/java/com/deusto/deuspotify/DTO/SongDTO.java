package com.deusto.deuspotify.DTO;

import java.util.Date;
import java.util.List;

/**
 * @class SongDTO
 * @brief Data Transfer Object for the Song entity.
 *
 * Represents the information of a song to transfer between backend layers.
 */
public class SongDTO {

    /** Unique identifier of the song. */
    private Long id;

    /** Name of the song. */
    private String name;

    /** List of artists who performed the song. */
    private List<String> artists;

    /** Duration of the song in seconds. */
    private double duration;

    /** List of genres associated with the song. */
    private List<String> genres;

    /** Release date of the song. */
    private Date dateOfRelease;

    /** Album the song belongs to. */
    private String album;

    /** Path to the song file (if available). */
    private String filePath;

    /**
     * Default constructor.
     */
    public SongDTO() {}

    /**
     * Constructor without file path.
     *
     * @param id Unique identifier.
     * @param name Name of the song.
     * @param artists List of artists.
     * @param duration Duration in seconds.
     * @param genres List of genres.
     * @param dateOfRelease Date of release.
     * @param album Album name.
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
     * Full constructor including file path.
     *
     * @param id Unique identifier.
     * @param name Name of the song.
     * @param artists List of artists.
     * @param duration Duration in seconds.
     * @param genres List of genres.
     * @param dateOfRelease Date of release.
     * @param album Album name.
     * @param filePath File path to the song.
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

    /// @name Getters and Setters
    /// @{

    /** @return Song ID. */
    public Long getId() { return id; }

    /** @param id New song ID. */
    public void setId(Long id) { this.id = id; }

    /** @return Song name. */
    public String getName() { return name; }

    /** @param name New song name. */
    public void setName(String name) { this.name = name; }

    /** @return List of artists. */
    public List<String> getArtists() { return artists; }

    /** @param artists New list of artists. */
    public void setArtists(List<String> artists) { this.artists = artists; }

    /** @return Song duration. */
    public double getDuration() { return duration; }

    /** @param duration New duration in seconds. */
    public void setDuration(double duration) { this.duration = duration; }

    /** @return List of genres. */
    public List<String> getGenres() { return genres; }

    /** @param genres New list of genres. */
    public void setGenres(List<String> genres) { this.genres = genres; }

    /** @return Release date. */
    public Date getDateOfRelease() { return dateOfRelease; }

    /** @param dateOfRelease New release date. */
    public void setDateOfRelease(Date dateOfRelease) { this.dateOfRelease = dateOfRelease; }

    /** @return Album name. */
    public String getAlbum() { return album; }

    /** @param album New album name. */
    public void setAlbum(String album) { this.album = album; }

    /** @return File path. */
    public String getFilePath() { return filePath; }

    /** @param filePath Path to the song file. */
    public void setFilePath(String filePath) { this.filePath = filePath; }

    /// @}
}
