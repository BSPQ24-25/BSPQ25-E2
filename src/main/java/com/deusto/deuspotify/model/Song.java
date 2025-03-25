package com.deusto.deuspotify.model;

import java.util.List;
import java.util.Date;

public class Song {
    private String name;
    private List<String> artists;
    private double duration; // en minutos o segundos, según prefieras
    private List<String> genres;
    private Date dateOfRelease;
    private String album;

    public Song() {}

    public Song(String name, List<String> artists, double duration, List<String> genres, Date dateOfRelease, String album) {
        this.name = name;
        this.artists = artists;
        this.duration = duration;
        this.genres = genres;
        this.dateOfRelease = dateOfRelease;
        this.album = album;
    }

    // Getters y setters
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

    @Override
    public String toString() {
        return "Song{" + "name='" + name + '\'' + ", artists=" + artists + ", duration=" + duration + ", genres=" + genres + ", dateOfRelease=" + dateOfRelease + ", album='" + album + '\'' + '}';
    }
}