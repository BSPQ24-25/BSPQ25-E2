package com.deusto.deuspotify.DTO;

import java.util.List;
/**
 * Data Transfer Object for user profile information.
 * Contains user identity and associations with playlists and songs.
 */
public class ProfileDTO {

    /** Unique identifier for the profile. */
    private Long id;

    /** Username of the profile owner. */
    private String username;

    /** Email address associated with the profile. */
    private String email;

    /** List of usernames representing the user's friends. */
    private List<String> friendsList;

    /** List of user's favourite songs. */
    private List<SongDTO> favouriteSongs;

    /** List of playlists owned by the user. */
    private List<PlaylistDTO> playlists;

    /** Flag indicating whether the user is an administrator. */
    private boolean isAdmin;

    /** Default constructor. */
    public ProfileDTO() {}

    /**
     * Constructs a new ProfileDTO with all fields.
     *
     * @param id Profile ID
     * @param username Username
     * @param email Email address
     * @param friendsList List of friend usernames
     * @param favouriteSongs List of favourite songs
     * @param playlists List of user's playlists
     * @param isAdmin Admin status
     */
    public ProfileDTO(Long id, String username, String email, List<String> friendsList,
                      List<SongDTO> favouriteSongs, List<PlaylistDTO> playlists, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.friendsList = friendsList;
        this.favouriteSongs = favouriteSongs;
        this.playlists = playlists;
        this.isAdmin = isAdmin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(List<String> friendsList) {
        this.friendsList = friendsList;
    }

    public List<SongDTO> getFavouriteSongs() {
        return favouriteSongs;
    }

    public void setFavouriteSongs(List<SongDTO> favouriteSongs) {
        this.favouriteSongs = favouriteSongs;
    }

    public List<PlaylistDTO> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<PlaylistDTO> playlists) {
        this.playlists = playlists;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
