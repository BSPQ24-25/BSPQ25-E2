package com.deusto.deuspotify.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Entity representing a song in the Deuspotify application.
 * Includes metadata such as name, artists, duration, genres, release date, album, and file path.
 */
@Entity
@Table(name = "songs")
public class Song {

    /** Unique identifier of the song. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name or title of the song. */
    private String name;

    /** List of artist names who performed the song. */
    @ElementCollection
    @CollectionTable(name = "song_artists", joinColumns = @JoinColumn(name = "song_id"))
    @Column(name = "artist")
    private List<String> artists;

    /** Duration of the song in seconds. */
    private double duration;

    /** List of genres the song belongs to. */
    @ElementCollection
    @CollectionTable(name = "song_genres", joinColumns = @JoinColumn(name = "song_id"))
    @Column(name = "genre")
    private List<String> genres;

    /** Date when the song was released. */
    @Temporal(TemporalType.DATE)
    private Date dateOfRelease;

    /** Name of the album the song belongs to. */
    private String album;

    /** Path to the audio file stored on the server. */
    private String filePath;

    /** Default constructor. */
    public Song() {}

    /**
     * Constructor with ID.
     * 
     * @param id The unique identifier of the song.
     */
    public Song(Long id) {
        this.id = id;
    }

    /**
     * Constructs a Song without file path.
     *
     * @param name           the song name
     * @param artists        the list of artists
     * @param duration       the song duration in seconds
     * @param genres         the list of genres
     * @param dateOfRelease  the date of release
     * @param album          the album name
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
     * Constructs a Song with all fields.
     *
     * @param name           the song name
     * @param artists        the list of artists
     * @param duration       the song duration in seconds
     * @param genres         the list of genres
     * @param dateOfRelease  the date of release
     * @param album          the album name
     * @param filePath       the file path to the audio file
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

    /** @return the unique ID of the song */
    public Long getId() { return id; }

    /** @param id the unique ID to set */
    public void setId(Long id) { this.id = id; }

    /** @return the name of the song */
    public String getName() { return name; }

    /** @param name the name of the song to set */
    public void setName(String name) { this.name = name; }

    /** @return the list of artists */
    public List<String> getArtists() { return artists; }

    /** @param artists the list of artists to set */
    public void setArtists(List<String> artists) { this.artists = artists; }

    /** @return the duration of the song in seconds */
    public double getDuration() { return duration; }

    /** @param duration the song duration in seconds */
    public void setDuration(double duration) { this.duration = duration; }

    /** @return the list of genres */
    public List<String> getGenres() { return genres; }

    /** @param genres the list of genres to set */
    public void setGenres(List<String> genres) { this.genres = genres; }

    /** @return the release date of the song */
    public Date getDateOfRelease() { return dateOfRelease; }

    /** @param dateOfRelease the release date to set */
    public void setDateOfRelease(Date dateOfRelease) { this.dateOfRelease = dateOfRelease; }

    /** @return the album the song belongs to */
    public String getAlbum() { return album; }

    /** @param album the album name to set */
    public void setAlbum(String album) { this.album = album; }

    /** @return the file path to the audio file */
    public String getFilePath() { return filePath; }

    /** @param filePath the file path to set */
    public void setFilePath(String filePath) { this.filePath = filePath; }

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
