package com.deusto.deuspotify.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "playlists")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> owners;

    private boolean isPublic;

    @ManyToMany
    @JoinTable(
        name = "playlist_song",
        joinColumns = @JoinColumn(name = "playlist_id"),
        inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Song> songs;

    private int numberOfSongs;

    @ElementCollection
    @Column(name = "song_order")
    private List<Integer> order;

    public Playlist() {}

    public Playlist(List<String> owners, boolean isPublic, List<Song> songs, List<Integer> order) {
        this.owners = owners;
        this.isPublic = isPublic;
        this.songs = songs;
        this.numberOfSongs = songs != null ? songs.size() : 0;
        this.order = order;
    }

    // Getters and setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public List<String> getOwners() { return owners; }
    public void setOwners(List<String> owners) { this.owners = owners; }

    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }

    public List<Song> getSongs() { return songs; }
    public void setSongs(List<Song> songs) { 
        this.songs = songs; 
        this.numberOfSongs = songs != null ? songs.size() : 0;
    }

    public int getNumberOfSongs() { return numberOfSongs; }

    public List<Integer> getOrder() { return order; }
    public void setOrder(List<Integer> order) { this.order = order; }

    @Override
    public String toString() {
        return "Playlist{" +
                "owners=" + owners +
                ", isPublic=" + isPublic +
                ", numberOfSongs=" + numberOfSongs +
                ", order=" + order +
                '}';
    }
}
