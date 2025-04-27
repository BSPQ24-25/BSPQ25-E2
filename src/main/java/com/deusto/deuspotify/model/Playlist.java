package com.deusto.deuspotify.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "playlists")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

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

    private String orderType = "none"; // 🔥 Nuevo campo: "none", "duration", "creation"

    public Playlist() {}

    public Playlist(String name, List<String> owners, boolean isPublic, List<Song> songs, List<Integer> order) {
        this.name = name;
        this.owners = owners;
        this.isPublic = isPublic;
        this.songs = songs;
        this.numberOfSongs = songs != null ? songs.size() : 0;
        this.order = order;
    }

    // Getters y setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

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

    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }

    @Override
    public String toString() {
        return "Playlist{" +
                "name='" + name + '\'' +
                ", owners=" + owners +
                ", isPublic=" + isPublic +
                ", numberOfSongs=" + numberOfSongs +
                ", order=" + order +
                ", orderType=" + orderType +
                '}';
    }
}
