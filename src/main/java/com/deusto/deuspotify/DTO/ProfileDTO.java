package com.deusto.deuspotify.DTO;

import java.util.List;

/**
 * @class ProfileDTO
 * @brief Data Transfer Object for the Profile entity.
 *
 * This class is used to transfer profile data between layers.
 */
public class ProfileDTO {

    /** Unique identifier of the profile. */
    private Long id;

    /** Username of the profile. */
    private String username;

    /** Email associated with the profile. */
    private String email;

    /** List of usernames that are friends with this profile. */
    private List<String> friendsList;

    /** List of favorite songs for this profile. */
    private List<SongDTO> favouriteSongs;

    /** List of playlists owned by this profile. */
    private List<PlaylistDTO> playlists;

    /** Indicates if the profile has administrative privileges. */
    private boolean isAdmin;

    /**
     * Default constructor.
     */
    public ProfileDTO() {}

    /**
     * Constructs a ProfileDTO with all attributes.
     *
     * @param id Unique identifier.
     * @param username Username of the profile.
     * @param email Email of the user.
     * @param friendsList List of friends' usernames.
     * @param favouriteSongs List of favorite songs.
     * @param playlists List of owned playlists.
     * @param isAdmin Indicates if the user is an admin.
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

    /// @name Getters and Setters
    /// @{

    /** @return Profile ID. */
    public Long getId() { return id; }

    /** @param id New profile ID. */
    public void setId(Long id) { this.id = id; }

    /** @return Username. */
    public String getUsername() { return username; }

    /** @param username New username. */
    public void setUsername(String username) { this.username = username; }

    /** @return Email. */
    public String getEmail() { return email; }

    /** @param email New email address. */
    public void setEmail(String email) { this.email = email; }

    /** @return List of friend usernames. */
    public List<String> getFriendsList() { return friendsList; }

    /** @param friendsList Updated list of friend usernames. */
    public void setFriendsList(List<String> friendsList) { this.friendsList = friendsList; }

    /** @return Favorite songs. */
    public List<SongDTO> getFavouriteSongs() { return favouriteSongs; }

    /** @param favouriteSongs New list of favorite songs. */
    public void setFavouriteSongs(List<SongDTO> favouriteSongs) { this.favouriteSongs = favouriteSongs; }

    /** @return List of playlists. */
    public List<PlaylistDTO> getPlaylists() { return playlists; }

    /** @param playlists New list of playlists. */
    public void setPlaylists(List<PlaylistDTO> playlists) { this.playlists = playlists; }

    /** @return Whether the profile is an admin. */
    public boolean isAdmin() { return isAdmin; }

    /** @param isAdmin New admin status. */
    public void setAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }

    /// @}
}
