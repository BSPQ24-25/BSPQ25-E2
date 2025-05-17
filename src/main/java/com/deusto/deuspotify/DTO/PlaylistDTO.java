package com.deusto.deuspotify.DTO;

import java.util.List;

/**
 * Data Transfer Object representing a playlist.
 * Used to transfer playlist data between layers of the application.
 */
public class PlaylistDTO {

    /** Unique identifier of the playlist. */
    private Long id;

    /** Name of the playlist. */
    private String name;

    /** List of usernames who own the playlist. */
    private List<String> owners;

    /** Indicates whether the playlist is public or private. */
    private boolean isPublic;

    /** List of songs contained in the playlist. */
    private List<SongDTO> songs;

    /** Total number of songs in the playlist. */
    private int numberOfSongs;

    /** Order of the songs in the playlist, represented as a list of indices. */
    private List<Integer> order;

    /** Default constructor. */
    public PlaylistDTO() {}

    /**
     * Constructs a PlaylistDTO with all attributes.
     *
     * @param id             the unique ID of the playlist
     * @param name           the name of the playlist
     * @param owners         the list of playlist owners
     * @param isPublic       whether the playlist is public
     * @param songs          the list of songs in the playlist
     * @param numberOfSongs  the number of songs in the playlist
     * @param order          the order of songs by index
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

    /** @return the playlist ID */
    public Long getId() { return id; }

    /** @param id the playlist ID */
    public void setId(Long id) { this.id = id; }

    /** @return the playlist name */
    public String getName() { return name; }

    /** @param name the playlist name */
    public void setName(String name) { this.name = name; }

    /** @return the list of playlist owners */
    public List<String> getOwners() { return owners; }

    /** @param owners the list of playlist owners */
    public void setOwners(List<String> owners) { this.owners = owners; }

    /** @return true if the playlist is public, false otherwise */
    public boolean isPublic() { return isPublic; }

    /** @param isPublic set whether the playlist is public */
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }

    /** @return the list of songs */
    public List<SongDTO> getSongs() { return songs; }

    /** @param songs the list of songs */
    public void setSongs(List<SongDTO> songs) { this.songs = songs; }

    /** @return the number of songs in the playlist */
    public int getNumberOfSongs() { return numberOfSongs; }

    /** @param numberOfSongs the number of songs in the playlist */
    public void setNumberOfSongs(int numberOfSongs) { this.numberOfSongs = numberOfSongs; }

    /** @return the song order list */
    public List<Integer> getOrder() { return order; }

    /** @param order the song order list */
    public void setOrder(List<Integer> order) { this.order = order; }
}
