package com.deusto.deuspotify.model;

import java.util.List;

public class Playlist {
    private List<String> owners;
    private boolean isPublic;
    private List<Song> songs;
    private int numberOfSongs;
    private List<Integer> order;

    public Playlist() {}

    public Playlist(List<String> owners, boolean isPublic, List<Song> songs, List<Integer> order) {
        this.owners = owners;
        this.isPublic = isPublic;
        this.songs = songs;
        this.numberOfSongs = songs != null ? songs.size() : 0;
        this.order = order;
    }

    // Getters y setters
    public List<String> getOwners() { return owners; }
    public void setOwners(List<String> owners) { this.owners = owners; }

    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }

    public List<Song> getSongs() { return songs; }
    public void setSongs(List<Song> songs) { this.songs = songs; this.numberOfSongs = songs.size(); }

    public int getNumberOfSongs() { return numberOfSongs; }

    public List<Integer> getOrder() { return order; }
    public void setOrder(List<Integer> order) { this.order = order; }

    @Override
    public String toString() {
        return "Playlist{" + "owners=" + owners + ", isPublic=" + isPublic + ", numberOfSongs=" + numberOfSongs + ", order=" + order + '}';
    }
}