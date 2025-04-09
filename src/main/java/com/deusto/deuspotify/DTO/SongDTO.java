package com.deusto.deuspotify.DTO;

import java.util.Date;
import java.util.List;

public class SongDTO {

    private Long id;
    private String name;
    private List<String> artists;
    private double duration;
    private List<String> genres;
    private Date dateOfRelease;
    private String album;
    private String filePath;

    public SongDTO() {}

    public SongDTO(Long id, String name, List<String> artists, double duration, List<String> genres, Date dateOfRelease, String album) {
        this.id = id;
        this.name = name;
        this.artists = artists;
        this.duration = duration;
        this.genres = genres;
        this.dateOfRelease = dateOfRelease;
        this.album = album;
    }

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
}
