package com.deusto.deuspotify.DTO;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a Playlist.
 * Used to transfer playlist data between the frontend and backend without exposing the internal model.
 */
public class PlaylistDTO {

    /** Unique identifier of the playlist. */
    private Long id;

    /** Name of the playlist. */
    private String name;

    /** List of usernames who own the playlist. */
    private List<String> owners;

    /** Indicates whether the playlist is public. */
    private boolean isPublic;

    /** List of songs included in the playlist. */
    private List<SongDTO> songs;

    /** Number of songs in the playlist. */
    private int numberOfSongs;

    /** Order of the songs in the playlist, represented by a list of indices. */
    private List<Integer> order;

    /** Default constructor. */
    public PlaylistDTO() {}

    /**
     * Constructs a new PlaylistDTO with the specified attributes.
     *
     * @param id            Unique identifier of the playlist.
     * @param name          Name of the playlist.
     * @param owners        List of playlist owners.
     * @param isPublic      Visibility status of the playlist.
     * @param songs         Songs contained in the playlist.
     * @param numberOfSongs Total number of songs.
     * @param order         Order of the songs.
     */
    public PlaylistDTO(Long id, String name, List<String> owners, boolean isPublic, List<SongDTO> songs, int numberOfSongs, List<Integer> order) {
        this.id = id;
        this.name = name;
        this.owners = owners;
        this.isPublic = isPublic;
        this.songs = songs;
        this.numberOfSongs = numberOfSongs;
        this.order = order;
    }

    // Getters and setters with Javadoc

    /**
     * Gets the playlist ID.
     * @return Playlist ID.
     */
    public Long getId() { return id; }

    /**
     * Sets the playlist ID.
     * @param id Playlist ID.
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Gets the playlist name.
     * @return Playlist name.
     */
    public String getName() { return name; }

    /**
     * Sets the playlist name.
     * @param name Playlist name.
     */
    public void setName(String name) { this.name = name; }

    /**
     * Gets the list of playlist owners.
     * @return List of usernames.
     */
    public List<String> getOwners() { return owners; }

    /**
     * Sets the playlist owners.
     * @param owners List of usernames.
     */
    public void setOwners(List<String> owners) { this.owners = owners; }

    /**
     * Checks if the playlist is public.
     * @return {@code true} if public, {@code false} otherwise.
     */
    public boolean isPublic() { return isPublic; }

    /**
     * Sets the playlist visibility.
     * @param isPublic {@code true} to make public, {@code false} to make private.
     */
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }

    /**
     * Gets the list of songs in the playlist.
     * @return List of {@link SongDTO}.
     */
    public List<SongDTO> getSongs() { return songs; }

    /**
     * Sets the list of songs in the playlist.
     * @param songs List of {@link SongDTO}.
     */
    public void setSongs(List<SongDTO> songs) { this.songs = songs; }

    /**
     * Gets the total number of songs.
     * @return Number of songs.
     */
    public int getNumberOfSongs() { return numberOfSongs; }

    /**
     * Sets the number of songs.
     * @param numberOfSongs Number of songs.
     */
    public void setNumberOfSongs(int numberOfSongs) { this.numberOfSongs = numberOfSongs; }

    /**
     * Gets the order of the songs.
     * @return List of song order indices.
     */
    public List<Integer> getOrder() { return order; }

    /**
     * Sets the order of the songs.
     * @param order List of song order indices.
     */
    public void setOrder(List<Integer> order) { this.order = order; }
}
