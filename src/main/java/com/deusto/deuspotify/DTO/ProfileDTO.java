package com.deusto.deuspotify.DTO;

import java.util.List;

public class ProfileDTO {

    private Long id;
    private String username;
    private String email;
    private String profileImageUrl;
    private List<String> friendsList;
    private List<SongDTO> favouriteSongs;
    private List<PlaylistDTO> playlists;
    private boolean isAdmin;

    public ProfileDTO() {}

    public ProfileDTO(Long id, String username, String email, List<String> friendsList, List<SongDTO> favouriteSongs, List<PlaylistDTO> playlists, boolean isAdmin, String profileImageUrl) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.friendsList = friendsList;
        this.favouriteSongs = favouriteSongs;
        this.playlists = playlists;
        this.isAdmin = isAdmin;
        this.profileImageUrl = profileImageUrl;
    }

    // Getters and setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public List<String> getFriendsList() { return friendsList; }
    public void setFriendsList(List<String> friendsList) { this.friendsList = friendsList; }

    public List<SongDTO> getFavouriteSongs() { return favouriteSongs; }
    public void setFavouriteSongs(List<SongDTO> favouriteSongs) { this.favouriteSongs = favouriteSongs; }

    public List<PlaylistDTO> getPlaylists() { return playlists; }
    public void setPlaylists(List<PlaylistDTO> playlists) { this.playlists = playlists; }

    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }
}