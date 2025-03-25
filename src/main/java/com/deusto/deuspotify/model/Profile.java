package com.deusto.deuspotify.model;
import java.util.List;

public class Profile {
    private String username;
    private String password;
    private String email;
    private List<String> friendsList;
    private List<Song> favouriteSongs;
    private List<Playlist> playlists;
    private boolean isAdmin;

    public Profile() {}

    public Profile(String username, String password, String email, List<String> friendsList, List<Song> favouriteSongs, List<Playlist> playlists, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.friendsList = friendsList;
        this.favouriteSongs = favouriteSongs;
        this.playlists = playlists;
        this.isAdmin = isAdmin;
    }

    // Getters y setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<String> getFriendsList() { return friendsList; }
    public void setFriendsList(List<String> friendsList) { this.friendsList = friendsList; }

    public List<Song> getFavouriteSongs() { return favouriteSongs; }
    public void setFavouriteSongs(List<Song> favouriteSongs) { this.favouriteSongs = favouriteSongs; }

    public List<Playlist> getPlaylists() { return playlists; }
    public void setPlaylists(List<Playlist> playlists) { this.playlists = playlists; }

    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }

    @Override
    public String toString() {
        return "Profile{" + "username='" + username + '\'' + ", email='" + email + '\'' + ", isAdmin=" + isAdmin + '}';
    }
}