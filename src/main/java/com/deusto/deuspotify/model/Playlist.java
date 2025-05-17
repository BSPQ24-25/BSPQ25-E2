package com.deusto.deuspotify.model;

import jakarta.persistence.*;
import java.util.List;

/**
 * @class Playlist
 * @brief Represents a music playlist in the system.
 *
 * A playlist contains a name, list of owners, visibility flag, songs, and order information.
 */
@Entity
@Table(name = "playlists")
public class Playlist {

    /** Unique identifier of the playlist (primary key). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name of the playlist. */
    private String name;

    /** List of usernames who own the playlist. */
    @ElementCollection
    private List<String> owners;

    /** Flag indicating if the playlist is public. */
    private boolean isPublic;

    /**
     * List of songs included in the playlist.
     * Many-to-many relationship mapped through the playlist_song join table.
     */
    @ManyToMany
    @JoinTable(
        name = "playlist_song",
        joinColumns = @JoinColumn(name = "playlist_id"),
        inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Song> songs;

    /** Number of songs in the playlist. */
    private int numberOfSongs;

    /** Custom order of songs (list of song indices). */
    @ElementCollection
    @Column(name = "song_order")
    private List<Integer> order;

    /**
     * Type of order applied to the playlist.
     * Possible values: "none", "duration", "creation".
     */
    private String orderType = "none";

    /**
     * Default constructor.
     */
    public Playlist() {}

    /**
     * Full constructor.
     *
     * @param name Name of the playlist.
     * @param owners List of playlist owners.
     * @param isPublic Visibility flag.
     * @param songs List of songs.
     * @param order Custom order of songs.
     */
    public Playlist(String name, List<String> owners, boolean isPublic, List<Song> songs, List<Integer> order) {
        this.name = name;
        this.owners = owners;
        this.isPublic = isPublic;
        this.songs = songs;
        this.numberOfSongs = songs != null ? songs.size() : 0;
        this.order = order;
    }

    /// @name Getters and Setters
    /// @{

    /** @return Playlist ID. */
    public Long getId() { return id; }

    /** @param id Playlist ID. */
    public void setId(Long id) { this.id = id; }

    /** @return Playlist name. */
    public String getName() { return name; }

    /** @param name New name. */
    public void setName(String name) { this.name = name; }

    /** @return List of owners. */
    public List<String> getOwners() { return owners; }

    /** @param owners New list of owners. */
    public void setOwners(List<String> owners) { this.owners = owners; }

    /** @return True if the playlist is public. */
    public boolean isPublic() { return isPublic; }

    /** @param isPublic Set playlist visibility. */
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }

    /** @return List of songs in the playlist. */
    public List<Song> getSongs() { return songs; }

    /**
     * Set the list of songs and update the number of songs.
     * @param songs New list of songs.
     */
    public void setSongs(List<Song> songs) {
        this.songs = songs;
        this.numberOfSongs = songs != null ? songs.size() : 0;
    }

    /** @return Number of songs. */
    public int getNumberOfSongs() { return numberOfSongs; }

    /** @return Custom song order. */
    public List<Integer> getOrder() { return order; }

    /** @param order New order. */
    public void setOrder(List<Integer> order) { this.order = order; }

    /** @return Type of order applied to the playlist. */
    public String getOrderType() { return orderType; }

    /** @param orderType Set the type of order. */
    public void setOrderType(String orderType) { this.orderType = orderType; }

    /// @}

    /**
     * @brief String representation of the playlist object.
     * @return Formatted string.
     */
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
