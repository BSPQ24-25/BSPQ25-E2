package com.deusto.deuspotify;

import com.deusto.deuspotify.model.Profile;
import com.deusto.deuspotify.model.Playlist;
import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.repositories.ProfileRepository;
import com.deusto.deuspotify.repositories.PlaylistRepository;
import com.deusto.deuspotify.repositories.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class DeuspotifyApplicationTests {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private SongRepository songRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for getting al profiles
    @Test
    void getAllProfilesTest() {
        Profile profile1 = new Profile();
        Profile profile2 = new Profile();

        when(profileRepository.findAll()).thenReturn(List.of(profile1, profile2));

        List<Profile> profiles = profileRepository.findAll();

        assertEquals(2, profiles.size());
        assertTrue(profiles.contains(profile1));
        assertTrue(profiles.contains(profile2));
    }

    // Test for creating a new profile
    @Test
    void createProfileTest() {
        Profile newProfile = new Profile();
        newProfile.setUsername("newUser");
        newProfile.setEmail("new@example.com");

        when(profileRepository.save(newProfile)).thenReturn(newProfile);

        Profile savedProfile = profileRepository.save(newProfile);

        assertNotNull(savedProfile);
        assertEquals("newUser", savedProfile.getUsername());
        assertEquals("new@example.com", savedProfile.getEmail());
    }

    // Test for retrieving a profile by ID
    @Test
    void getProfileByIdTest() {
        Profile profile = new Profile();
        profile.setId(1L);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));

        Optional<Profile> foundProfile = profileRepository.findById(1L);

        assertTrue(foundProfile.isPresent());
        assertEquals(1L, foundProfile.get().getId());
    }

    // Test for updating a profile
    @Test
    void updateProfileTest() {
        Profile existingProfile = new Profile();
        existingProfile.setId(1L);
        existingProfile.setUsername("oldUser");

        Profile updatedProfile = new Profile();
        updatedProfile.setUsername("newUser");

        when(profileRepository.findById(1L)).thenReturn(Optional.of(existingProfile));
        when(profileRepository.save(existingProfile)).thenReturn(existingProfile);

        existingProfile.setUsername(updatedProfile.getUsername());
        profileRepository.save(existingProfile);

        assertEquals("newUser", existingProfile.getUsername());
    }

    // Test for profile login
    @Test
    void loginTest() {
        Profile profile = new Profile();
        profile.setUsername("testUser");
        profile.setPassword("testPassword");

        when(profileRepository.findAll()).thenReturn(List.of(profile));

        Profile loggedInProfile = profileRepository.findAll().stream()
                .filter(p -> p.getUsername().equals("testUser") && p.getPassword().equals("testPassword"))
                .findFirst()
                .orElse(null);

        assertNotNull(loggedInProfile);
        assertEquals("testUser", loggedInProfile.getUsername());
    }

    // Test for creating a new playlist
    @Test
    void createPlaylistTest() {
        Playlist newPlaylist = new Playlist();
        newPlaylist.setOwners(List.of("owner1", "owner2"));
        newPlaylist.setPublic(true);

        when(playlistRepository.save(newPlaylist)).thenReturn(newPlaylist);

        Playlist savedPlaylist = playlistRepository.save(newPlaylist);

        assertNotNull(savedPlaylist);
        assertTrue(savedPlaylist.isPublic());
        assertEquals(2, savedPlaylist.getOwners().size());
    }

    // Test for adding a song to a playlist
    @Test
    void addSongToPlaylistTest() {
        Playlist playlist = new Playlist();
        Song song = new Song();
        song.setName("Test Song");

        when(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist));
        when(songRepository.findById(1L)).thenReturn(Optional.of(song));

        playlist.setSongs(List.of(song));
        playlistRepository.save(playlist);

        assertTrue(playlist.getSongs().contains(song));
    }

    // Test for retreaving all playlists
    @Test
    void getAllPlaylistsTest() {
        Playlist playlist1 = new Playlist();
        Playlist playlist2 = new Playlist();

        when(playlistRepository.findAll()).thenReturn(List.of(playlist1, playlist2));

        List<Playlist> playlists = playlistRepository.findAll();

        assertEquals(2, playlists.size());
        assertTrue(playlists.contains(playlist1));
        assertTrue(playlists.contains(playlist2));
    }

    // Test for getting a playlist by ID
    @Test
    void getPlaylistByIdTest() {
        Playlist playlist = new Playlist();
        playlist.setId(1L);

        when(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist));

        Optional<Playlist> foundPlaylist = playlistRepository.findById(1L);

        assertTrue(foundPlaylist.isPresent());
        assertEquals(1L, foundPlaylist.get().getId());
    }

    // Test for retraving all songs
    @Test
    void getAllSongsTest() {
        Song song1 = new Song();
        Song song2 = new Song();

        when(songRepository.findAll()).thenReturn(List.of(song1, song2));

        List<Song> songs = songRepository.findAll();

        assertEquals(2, songs.size());
        assertTrue(songs.contains(song1));
        assertTrue(songs.contains(song2));
    }

    // Test for getting a song by ID
    @Test
    void getSongByIdTest() {
        Song song = new Song();
        song.setId(1L);

        when(songRepository.findById(1L)).thenReturn(Optional.of(song));

        Optional<Song> foundSong = songRepository.findById(1L);

        assertTrue(foundSong.isPresent());
        assertEquals(1L, foundSong.get().getId());
    }

    // Test for adding a new song
    @Test
    void addSongTest() {
        Song newSong = new Song();
        newSong.setName("New Song");

        when(songRepository.save(newSong)).thenReturn(newSong);

        Song savedSong = songRepository.save(newSong);

        assertNotNull(savedSong);
        assertEquals("New Song", savedSong.getName());
    }


}