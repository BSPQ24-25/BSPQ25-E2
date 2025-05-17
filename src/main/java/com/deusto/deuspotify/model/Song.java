package com.deusto.deuspotify.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @class Song
 * @brief Represents a musical song in the Deuspotify application.
 *
 * Contains metadata such as name, artists, duration, genres, release date, album, and file path.
 */
@Entity
@Table(name = "songs")
public class Song {

    /** Unique identifier of the song. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name of the song. */
    private String name;

    /** List of artists who performed the song. */
    @ElementCollection
    @CollectionTable(name = "song_artists", joinColumns = @JoinColumn(name = "song_id"))
    @Column(name = "artist")
    private List<String> artists;

    /** Duration of the song in seconds. */
    private double duration;

    /** List of genres associated with the song. */
    @ElementCollection
    @CollectionTable(name = "song_genres", joinColumns = @JoinColumn(name = "song_id"))
    @Column(name = "genre")
    private List<String> genres;

    /** Date the song was released. */
    @Temporal(TemporalType.DATE)
    private Date dateOfRelease;

    /** Album to which the song belongs. */
    private String album;

    /** File path of the song's audio file. */
    private String filePath;

    /**
     * Default constructor.
     */
    public Song() {}

    /**
     * Constructor with ID.
     * @param id Song ID.
     */
    public Song(Long id) {
        this.id = id;
    }

    /**
     * Constructor without file path.
     * @param name Name of the song.
     * @param artists List of artists.
     * @param duration Duration in seconds.
     * @param genres List of genres.
     * @param dateOfRelease Date of release.
     * @param album Album name.
     */
    public Song(String name, List<String> artists, double duration, List<String> genres, Date dateOfRelease, String album) {
        this.name = name;
        this.artists = artists;
        this.duration = duration;
        this.genres = genres;
        this.dateOfRelease = dateOfRelease;
        this.album = album;
    }

    /**
     * Constructor with file path.
     * @param name Name of the song.
     * @param artists List of artists.
     * @param duration Duration in seconds.
     * @param genres List of genres.
     * @param dateOfRelease Date of release.
     * @param album Album name.
     * @param filePath Path to the audio file.
     */
    public Song(String name, List<String> artists, double duration, List<String> genres, Date dateOfRelease, String album, String filePath) {
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

    /** @param id Song ID. */
    public void setId(Long id) { this.id = id; }

    /** @return Song name. */
    public String getName() { return name; }

    /** @param name Song name. */
    public void setName(String name) { this.name = name; }

    /** @return List of artists. */
    public List<String> getArtists() { return artists; }

    /** @param artists List of artists. */
    public void setArtists(List<String> artists) { this.artists = artists; }

    /** @return Duration in seconds. */
    public double getDuration() { return duration; }

    /** @param duration Duration in seconds. */
    public void setDuration(double duration) { this.duration = duration; }

    /** @return List of genres. */
    public List<String> getGenres() { return genres; }

    /** @param genres List of genres. */
    public void setGenres(List<String> genres) { this.genres = genres; }

    /** @return Date of release. */
    public Date getDateOfRelease() { return dateOfRelease; }

    /** @param dateOfRelease Release date. */
    public void setDateOfRelease(Date dateOfRelease) { this.dateOfRelease = dateOfRelease; }

    /** @return Album name. */
    public String getAlbum() { return album; }

    /** @param album Album name. */
    public void setAlbum(String album) { this.album = album; }

    /** @return File path to the audio file. */
    public String getFilePath() { return filePath; }

    /** @param filePath File path to the audio file. */
    public void setFilePath(String filePath) { this.filePath = filePath; }

    /// @}

    /**
     * @brief Returns a string representation of the song.
     * @return A string containing song details.
     */
    @Override
    public String toString() {
        return "Song{" +
                "name='" + name + '\'' +
                ", artists=" + artists +
                ", duration=" + duration +
                ", genres=" + genres +
                ", dateOfRelease=" + dateOfRelease +
                ", album='" + album + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
