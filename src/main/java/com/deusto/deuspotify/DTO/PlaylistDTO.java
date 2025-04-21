package com.deusto.deuspotify.DTO;

import java.util.List;

public class PlaylistDTO {

    private Long id;
    private String name;
    private List<String> owners;
    private boolean isPublic;
    private List<SongDTO> songs;
    private int numberOfSongs;
    private List<Integer> order;

    public PlaylistDTO() {}

    public PlaylistDTO(Long id, List<String> owners, boolean isPublic, List<SongDTO> songs, int numberOfSongs, List<Integer> order) {
        this.id = id;
        this.name = name;
        this.owners = owners;
        this.isPublic = isPublic;
        this.songs = songs;
        this.numberOfSongs = numberOfSongs;
        this.order = order;
    }

    // Getters and setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<String> getOwners() { return owners; }
    public void setOwners(List<String> owners) { this.owners = owners; }

    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }

    public List<SongDTO> getSongs() { return songs; }
    public void setSongs(List<SongDTO> songs) { this.songs = songs; }

    public int getNumberOfSongs() { return numberOfSongs; }
    public void setNumberOfSongs(int numberOfSongs) { this.numberOfSongs = numberOfSongs; }

    public List<Integer> getOrder() { return order; }
    public void setOrder(List<Integer> order) { this.order = order; }
}