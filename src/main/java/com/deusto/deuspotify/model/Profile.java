package com.deusto.deuspotify.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String profileImageUrl;

    @ElementCollection
    @CollectionTable(name = "profile_friends", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "friend")
    private List<String> friendsList;

    @ManyToMany
    @JoinTable(
        name = "profile_favourite_song",
        joinColumns = @JoinColumn(name = "profile_id"),
        inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Song> favouriteSongs;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
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

    // Getters and setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    @Override
    public String toString() {
        return "Profile{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
