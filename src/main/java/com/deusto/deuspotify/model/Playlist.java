package com.deusto.deuspotify.model;

import jakarta.persistence.*;
import java.util.List;

/**
 * Entity representing a Playlist in the system.
 * A playlist is a collection of songs associated with one or more users.
 */
@Entity
@Table(name = "playlists")
public class Playlist {

    /** Unique identifier of the playlist. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name of the playlist. */
    private String name;

    /** List of usernames who own the playlist. */
    @ElementCollection
    private List<String> owners;

    /** Indicates whether the playlist is public or private. */
    private boolean isPublic;

    /** Songs included in the playlist. Many-to-many relationship. */
    @ManyToMany
    @JoinTable(
        name = "playlist_song",
        joinColumns = @JoinColumn(name = "playlist_id"),
        inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Song> songs;

    /** Number of songs currently in the playlist. */
    private int numberOfSongs;

    /** Custom order of songs in the playlist, represented by a list of indices. */
    @ElementCollection
    @Column(name = "song_order")
    private List<Integer> order;

    /** Type of ordering applied to the playlist (e.g., none, duration, creation). */
    private String orderType = "none";

    /** Default constructor. */
    public Playlist() {}

    /**
     * Constructs a Playlist with specified properties.
     *
     * @param name        the name of the playlist
     * @param owners      the list of owners
     * @param isPublic    true if public, false if private
     * @param songs       the list of songs
     * @param order       the custom order of song indices
     */
    public Playlist(String name, List<String> owners, boolean isPublic, List<Song> songs, List<Integer> order) {
        this.name = name;
        this.owners = owners;
        this.isPublic = isPublic;
        this.songs = songs;
        this.numberOfSongs = songs != null ? songs.size() : 0;
        this.order = order;
    }

    /** @return the playlist ID */
    public Long getId() { return id; }

    /** @param id the playlist ID */
    public void setId(Long id) { this.id = id; }

    /** @return the name of the playlist */
    public String getName() { return name; }

    /** @param name the name of the playlist */
    public void setName(String name) { this.name = name; }

    /** @return the list of playlist owners */
    public List<String> getOwners() { return owners; }

    /** @param owners the list of playlist owners */
    public void setOwners(List<String> owners) { this.owners = owners; }

    /** @return true if the playlist is public */
    public boolean isPublic() { return isPublic; }

    /** @param isPublic set whether the playlist is public */
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }

    /** @return the list of songs */
    public List<Song> getSongs() { return songs; }

    /**
     * Sets the list of songs and updates the number of songs.
     *
     * @param songs the list of songs to set
     */
    public void setSongs(List<Song> songs) {
        this.songs = songs;
        this.numberOfSongs = songs != null ? songs.size() : 0;
    }

    /** @return the number of songs in the playlist */
    public int getNumberOfSongs() { return numberOfSongs; }

    /** @return the order of song indices */
    public List<Integer> getOrder() { return order; }

    /** @param order the order of song indices */
    public void setOrder(List<Integer> order) { this.order = order; }

    /** @return the type of order applied to the playlist */
    public String getOrderType() { return orderType; }

    /** @param orderType the type of order to set (e.g., none, duration, creation) */
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
