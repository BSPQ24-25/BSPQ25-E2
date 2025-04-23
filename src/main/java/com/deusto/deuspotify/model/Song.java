package com.deusto.deuspotify.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "songs")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "song_artists", joinColumns = @JoinColumn(name = "song_id"))
    @Column(name = "artist")
    private List<String> artists;

    private double duration;

    @ElementCollection
    @CollectionTable(name = "song_genres", joinColumns = @JoinColumn(name = "song_id"))
    @Column(name = "genre")
    private List<String> genres;

    @Temporal(TemporalType.DATE)
    private Date dateOfRelease;

    private String album;
    
    private String filePath;

    public Song() {}

        public Song(Long id) {
        this.id = id;
    }

    public Song(String name, List<String> artists, double duration, List<String> genres, Date dateOfRelease, String album) {
        this.name = name;
        this.artists = artists;
        this.duration = duration;
        this.genres = genres;
        this.dateOfRelease = dateOfRelease;
        this.album = album;
    }

    public Song(String name, List<String> artists, double duration, List<String> genres, Date dateOfRelease, String album, String filePath) {
        this.name = name;
        this.artists = artists;
        this.duration = duration;
        this.genres = genres;
        this.dateOfRelease = dateOfRelease;
        this.album = album;
        this.filePath = filePath;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<String> getArtists() { return artists; }
    public void setArtists(List<String> artists) { this.artists = artists; }

    public double getDuration() { return duration; }
    public void setDuration(double duration) { this.duration = duration; }

    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }

    public Date getDateOfRelease() { return dateOfRelease; }
    public void setDateOfRelease(Date dateOfRelease) { this.dateOfRelease = dateOfRelease; }

    public String getAlbum() { return album; }
    public void setAlbum(String album) { this.album = album; }

    public String getFilePath() { return filePath; }
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
