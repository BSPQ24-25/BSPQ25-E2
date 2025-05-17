package com.deusto.deuspotify.model;

import jakarta.persistence.*;
import java.util.List;

/**
 * @class Profile
 * @brief Represents a user profile in the Deuspotify application.
 *
 * A profile includes personal information, friend list, favorite songs,
 * associated playlists, and admin privileges.
 */
@Entity
@Table(name = "profiles")
public class Profile {

    /** Unique identifier for the profile. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Username used for login. */
    private String username;

    /** Encrypted password. */
    private String password;

    /** Email address associated with the profile. */
    private String email;

    /** List of friends' usernames. */
    @ElementCollection
    @CollectionTable(name = "profile_friends", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "friend")
    private List<String> friendsList;

    /** List of songs marked as favorites by the user. */
    @ManyToMany
    @JoinTable(
        name = "profile_favourite_song",
        joinColumns = @JoinColumn(name = "profile_id"),
        inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Song> favouriteSongs;

    /** List of playlists owned by the user. */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private List<Playlist> playlists;

    /** Indicates if the user has administrative privileges. */
    private boolean isAdmin;

    /**
     * Default constructor.
     */
    public Profile() {}

    /**
     * Full constructor.
     *
     * @param username Username for the profile.
     * @param password Encrypted password.
     * @param email Email address.
     * @param friendsList List of friends.
     * @param favouriteSongs Favorite songs.
     * @param playlists User's playlists.
     * @param isAdmin Whether the user is an admin.
     */
    public Profile(String username, String password, String email, List<String> friendsList, List<Song> favouriteSongs, List<Playlist> playlists, boolean isAdmin) {
        this.username = username;
        this.password = password;
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

    /** @param id Profile ID. */
    public void setId(Long id) { this.id = id; }

    /** @return Username. */
    public String getUsername() { return username; }

    /** @param username Username. */
    public void setUsername(String username) { this.username = username; }

    /** @return Password. */
    public String getPassword() { return password; }

    /** @param password Encrypted password. */
    public void setPassword(String password) { this.password = password; }

    /** @return Email. */
    public String getEmail() { return email; }

    /** @param email Email address. */
    public void setEmail(String email) { this.email = email; }

    /** @return List of friends. */
    public List<String> getFriendsList() { return friendsList; }

    /** @param friendsList List of friends. */
    public void setFriendsList(List<String> friendsList) { this.friendsList = friendsList; }

    /** @return Favorite songs. */
    public List<Song> getFavouriteSongs() { return favouriteSongs; }

    /** @param favouriteSongs List of favorite songs. */
    public void setFavouriteSongs(List<Song> favouriteSongs) { this.favouriteSongs = favouriteSongs; }

    /** @return Playlists. */
    public List<Playlist> getPlaylists() { return playlists; }

    /** @param playlists List of playlists. */
    public void setPlaylists(List<Playlist> playlists) { this.playlists = playlists; }

    /** @return True if the profile is admin. */
    public boolean isAdmin() { return isAdmin; }

    /** @param isAdmin Set admin status. */
    public void setAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }

    /// @}

    /**
     * @brief String representation of the profile.
     * @return A summary of the profile including username and email.
     */
    @Override
    public String toString() {
        return "Profile{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
