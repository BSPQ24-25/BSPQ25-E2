package com.deusto.deuspotify.model;

import jakarta.persistence.*;
import java.util.List;

/**
 * Entity representing a user profile in the system.
 * Each profile includes user credentials, social connections, favorite songs, and playlists.
 */
@Entity
@Table(name = "profiles")
public class Profile {

    /** Unique identifier of the profile. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Username used for login and identification. */
    private String username;

    /** User's password (should be stored in hashed form in a real-world application). */
    private String password;

    /** Email address associated with the user profile. */
    private String email;

    /** List of usernames representing the profile's friends. */
    @ElementCollection
    @CollectionTable(name = "profile_friends", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "friend")
    private List<String> friendsList;

    /** Songs marked as favorites by the user. */
    @ManyToMany
    @JoinTable(
        name = "profile_favourite_song",
        joinColumns = @JoinColumn(name = "profile_id"),
        inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Song> favouriteSongs;

    /** Playlists created by the user. */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private List<Playlist> playlists;

    /** Indicates whether the profile belongs to an administrator. */
    private boolean isAdmin;

    /** Default constructor. */
    public Profile() {}

    /**
     * Constructs a Profile with the specified attributes.
     *
     * @param username        the username
     * @param password        the password
     * @param email           the email address
     * @param friendsList     the list of friends
     * @param favouriteSongs  the list of favorite songs
     * @param playlists       the user's playlists
     * @param isAdmin         true if the user is an admin
     */
    public Profile(String username, String password, String email, List<String> friendsList,
                   List<Song> favouriteSongs, List<Playlist> playlists, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.friendsList = friendsList;
        this.favouriteSongs = favouriteSongs;
        this.playlists = playlists;
        this.isAdmin = isAdmin;
    }

    /** @return the profile ID */
    public Long getId() { return id; }

    /** @param id the profile ID to set */
    public void setId(Long id) { this.id = id; }

    /** @return the username */
    public String getUsername() { return username; }

    /** @param username the username to set */
    public void setUsername(String username) { this.username = username; }

    /** @return the password */
    public String getPassword() { return password; }

    /** @param password the password to set */
    public void setPassword(String password) { this.password = password; }

    /** @return the email address */
    public String getEmail() { return email; }

    /** @param email the email address to set */
    public void setEmail(String email) { this.email = email; }

    /** @return the list of friends */
    public List<String> getFriendsList() { return friendsList; }

    /** @param friendsList the list of friends to set */
    public void setFriendsList(List<String> friendsList) { this.friendsList = friendsList; }

    /** @return the list of favorite songs */
    public List<Song> getFavouriteSongs() { return favouriteSongs; }

    /** @param favouriteSongs the list of favorite songs to set */
    public void setFavouriteSongs(List<Song> favouriteSongs) { this.favouriteSongs = favouriteSongs; }

    /** @return the list of playlists created by the user */
    public List<Playlist> getPlaylists() { return playlists; }

    /** @param playlists the list of playlists to set */
    public void setPlaylists(List<Playlist> playlists) { this.playlists = playlists; }

    /** @return true if the profile has admin privileges */
    public boolean isAdmin() { return isAdmin; }

    /** @param isAdmin sets whether the profile has admin privileges */
    public void setAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }

    @Override
    public String toString() {
        return "Profile{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
