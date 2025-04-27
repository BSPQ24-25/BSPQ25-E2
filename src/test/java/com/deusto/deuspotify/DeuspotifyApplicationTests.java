package com.deusto.deuspotify;

import com.deusto.deuspotify.model.Profile;
import com.deusto.deuspotify.model.Playlist;
import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.repositories.ProfileRepository;
import com.deusto.deuspotify.repositories.PlaylistRepository;
import com.deusto.deuspotify.repositories.SongRepository;
import com.deusto.deuspotify.security.JwtUtil;
import com.deusto.deuspotify.services.DeuspotifyService;
import com.deusto.deuspotify.services.DeuspotifyServiceImpl;
import com.deusto.deuspotify.services.ProfileService;
import com.deusto.deuspotify.Controllers.AuthController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class DeuspotifyApplicationTests {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private ProfileService profileService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private SongRepository songRepository;

    private AuthController authController;
    private DeuspotifyService deuspotifyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authenticationManager, profileService, jwtUtil);
        deuspotifyService = new DeuspotifyServiceImpl(songRepository, playlistRepository);
    }

    // Auth Controller tests

    @Test
    void loginTest() {
        String username = "testUser";
        String password = "testPass";
        String token = "dummy-token";
        Map<String, String> loginRequest = Map.of("username", username, "password", password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(username, password));
        when(jwtUtil.generateToken(username)).thenReturn(token);

        ResponseEntity<?> response = authController.login(loginRequest);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(responseBody);
        assertEquals(token, responseBody.get("token"));
    }

    @Test
    void registerTest() {
        Profile newProfile = new Profile();
        newProfile.setUsername("newUser");
        newProfile.setEmail("new@example.com");

        when(profileService.registerUser(newProfile)).thenReturn(newProfile);
        ResponseEntity<?> response = authController.register(newProfile);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(newProfile, response.getBody());
    }

    @Test
    void logoutTest() {
        ResponseEntity<?> response = authController.logout();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("Logged out successfully", responseBody.get("message"));
    }

    // Profile Repository tests
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

    @Test
    void getProfileByIdTest() {
        Profile profile = new Profile();
        profile.setId(1L);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));

        Optional<Profile> foundProfile = profileRepository.findById(1L);

        assertTrue(foundProfile.isPresent());
        assertEquals(1L, foundProfile.get().getId());
    }

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

    @Test
    void profileLoginTest() {
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

    @Test
    void deleteProfileTest() {
        Profile profile = new Profile();
        profile.setId(1L);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        doNothing().when(profileRepository).delete(any(Profile.class));

        // Call the delete method
        profileRepository.delete(profile);

        // Verify that the profile was deleted
        verify(profileRepository, times(1)).delete(profile);
    }

    @Test
    void getMyProfileTest() {
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setUsername("testUser");

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));

        Optional<Profile> foundProfile = profileRepository.findById(1L);

        assertTrue(foundProfile.isPresent());
        assertEquals("testUser", foundProfile.get().getUsername());
    }

    // Playlist Repository tests
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

    @Test
    void getPlaylistByIdTest() {
        Playlist playlist = new Playlist();
        playlist.setId(1L);

        when(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist));

        Optional<Playlist> foundPlaylist = playlistRepository.findById(1L);

        assertTrue(foundPlaylist.isPresent());
        assertEquals(1L, foundPlaylist.get().getId());
    }

    @Test
    void updatePlaylistTest() {
        Playlist existingPlaylist = new Playlist();
        existingPlaylist.setId(1L);
        existingPlaylist.setPublic(true);
        existingPlaylist.setOwners(List.of("owner1", "owner2"));

        Playlist updatedPlaylist = new Playlist();
        updatedPlaylist.setPublic(false);
        updatedPlaylist.setOwners(List.of("owner3"));

        when(playlistRepository.findById(1L)).thenReturn(Optional.of(existingPlaylist));
        when(playlistRepository.save(existingPlaylist)).thenReturn(existingPlaylist);

        existingPlaylist.setPublic(updatedPlaylist.isPublic());
        existingPlaylist.setOwners(updatedPlaylist.getOwners());
        playlistRepository.save(existingPlaylist);

        assertFalse(existingPlaylist.isPublic());
        assertEquals(1, existingPlaylist.getOwners().size());
        assertTrue(existingPlaylist.getOwners().contains("owner3"));
    }

    @Test
    void deletePlaylistTest() {
        Playlist playlist = new Playlist();
        playlist.setId(1L);

        when(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist));
        doNothing().when(playlistRepository).delete(any(Playlist.class));

        // Call the delete method
        playlistRepository.delete(playlist);

        // Explicitly call findById to match the expected interaction
        playlistRepository.findById(1L);

        // Verify that the playlist was deleted
        verify(playlistRepository, times(1)).delete(playlist);
        verify(playlistRepository, times(1)).findById(1L);
    }

    @Test
    void addSongToPlaylistByIdTest() {
        Playlist playlist = new Playlist();
        playlist.setId(1L);
        Song song = new Song();
        song.setId(1L);
        song.setName("Test Song");

        when(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist));
        when(songRepository.findById(1L)).thenReturn(Optional.of(song));

        if (playlist.getSongs() == null) {
            playlist.setSongs(new ArrayList<>());
        }
        playlist.getSongs().add(song);
        playlistRepository.save(playlist);

        assertTrue(playlist.getSongs().contains(song));
    }

    @Test
    void removeSongFromPlaylistTest() {
        Playlist playlist = new Playlist();
        playlist.setId(1L);
        Song song = new Song();
        song.setId(1L);
        song.setName("Test Song");

        playlist.setSongs(new ArrayList<>(List.of(song))); // Initialize with the song

        when(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist));
        when(songRepository.findById(1L)).thenReturn(Optional.of(song));

        // Remove the song from the playlist
        playlist.getSongs().removeIf(s -> s.getId().equals(song.getId()));
        playlistRepository.save(playlist);

        assertTrue(playlist.getSongs().isEmpty());
    }

    @Test
    void updatePlaylistSongsTest() {
        Playlist playlist = new Playlist();
        playlist.setId(1L);
        Song song1 = new Song();
        song1.setId(1L);
        song1.setName("Song 1");
        Song song2 = new Song();
        song2.setId(2L);
        song2.setName("Song 2");

        when(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist));
        when(songRepository.findById(1L)).thenReturn(Optional.of(song1));
        when(songRepository.findById(2L)).thenReturn(Optional.of(song2));

        // Initialize the songs list if null and add songs to the playlist
        if (playlist.getSongs() == null) {
            playlist.setSongs(new ArrayList<>());
        }
        playlist.getSongs().add(song1);
        playlist.getSongs().add(song2);
        playlistRepository.save(playlist);

        // Verify the songs were added
        assertEquals(2, playlist.getSongs().size());
        assertTrue(playlist.getSongs().stream().anyMatch(song -> song.getName().equals("Song 1")));
        assertTrue(playlist.getSongs().stream().anyMatch(song -> song.getName().equals("Song 2")));
    }

    // Song Repository tests
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

    @Test
    void getSongByIdTest() {
        Song song = new Song();
        song.setId(1L);

        when(songRepository.findById(1L)).thenReturn(Optional.of(song));

        Optional<Song> foundSong = songRepository.findById(1L);

        assertTrue(foundSong.isPresent());
        assertEquals(1L, foundSong.get().getId());
    }

    @Test
    void addSongTest() {
        Song newSong = new Song();
        newSong.setName("New Song");

        when(songRepository.save(newSong)).thenReturn(newSong);

        Song savedSong = songRepository.save(newSong);

        assertNotNull(savedSong);
        assertEquals("New Song", savedSong.getName());
    }
    
    @Test
    void addSongWithFileTest() throws Exception {
        String originalFilename = "Tom Petty - Love Is A Long Road (Official Audio)[xAgUYyosqVM].mp3";
        byte[] content = "dummy audio content".getBytes();
        MultipartFile file = new MockMultipartFile("file", originalFilename, "audio/mp3", content);
        
        String name = "Test Song";
        String album = "Test Album";
        String artists = "Artist1,Artist2";
        String genres = "Rock,Pop";
        double duration = 3.5;
        Date dateOfRelease = new Date();
        
        // When saving, return the same Song instance as argument
        when(songRepository.save(any(Song.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Song savedSong = deuspotifyService.addSongWithFile(name, album, artists, genres, duration, dateOfRelease, file);
        assertNotNull(savedSong);
        assertNotNull(savedSong.getFilePath());
        // Check that the filename is sanitized (no spaces or special characters like parentheses)
        assertFalse(savedSong.getFilePath().contains(" "));
        assertFalse(savedSong.getFilePath().contains("("));
        assertFalse(savedSong.getFilePath().contains(")"));
        
        // Optionally, verify that the file was created in the uploads directory
        Path filePath = Paths.get("uploads/songs", savedSong.getFilePath());
        assertTrue(Files.exists(filePath));
        // Clean up the file created during test
        Files.deleteIfExists(filePath);
    }

    @Test
    void createSharedSongTest() {
        Song song = new Song();
        song.setName("Shared Song");
        song.setAlbum("Shared Album");
        song.setArtists(List.of("Artist1", "Artist2"));
        song.setGenres(List.of("Rock", "Pop"));
        song.setDuration(3.5);
        song.setDateOfRelease(new Date());
        song.setFilePath("path/to/shared/song.mp3");

        // When saving, return the same Song instance as argument
        when(songRepository.save(any(Song.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Song savedSong = deuspotifyService.addSong(song);
        assertNotNull(savedSong);
        assertEquals(song.getName(), savedSong.getName());
        assertEquals(song.getAlbum(), savedSong.getAlbum());
        assertEquals(song.getArtists(), savedSong.getArtists());
        assertEquals(song.getGenres(), savedSong.getGenres());
        assertEquals(song.getDuration(), savedSong.getDuration());
        assertEquals(song.getDateOfRelease(), savedSong.getDateOfRelease());
        assertEquals(song.getFilePath(), savedSong.getFilePath());
    }

    @Test
    void updateSongTest() {
        Song existingSong = new Song();
        existingSong.setId(1L);
        existingSong.setName("Old Song");
        existingSong.setAlbum("Old Album");
        existingSong.setArtists(List.of("Old Artist"));
        existingSong.setGenres(List.of("Old Genre"));
        existingSong.setDuration(3.0);
        existingSong.setDateOfRelease(new Date(0));

        //Create the new song object with updated values
        Song updatedSong = new Song();
        updatedSong.setName("New Song");
        updatedSong.setAlbum("New Album");
        updatedSong.setArtists(List.of("New Artist1", "New Artist2"));
        updatedSong.setGenres(List.of("New Genre1", "New Genre2"));
        updatedSong.setDuration(4.5);
        updatedSong.setDateOfRelease(new Date());

        when(songRepository.findById(1L)).thenReturn(Optional.of(existingSong));
        when(songRepository.save(existingSong)).thenReturn(existingSong);

        // Update the existing song with new values
        existingSong.setName(updatedSong.getName());
        existingSong.setAlbum(updatedSong.getAlbum());
        existingSong.setArtists(updatedSong.getArtists());
        existingSong.setGenres(updatedSong.getGenres());
        existingSong.setDuration(updatedSong.getDuration());
        existingSong.setDateOfRelease(updatedSong.getDateOfRelease());
        songRepository.save(existingSong);

        assertEquals("New Song", existingSong.getName());
        assertEquals("New Album", existingSong.getAlbum());
        assertEquals(List.of("New Artist1", "New Artist2"), existingSong.getArtists());
        assertEquals(List.of("New Genre1", "New Genre2"), existingSong.getGenres());
        assertEquals(4.5, existingSong.getDuration());
        assertEquals(updatedSong.getDateOfRelease(), existingSong.getDateOfRelease());
    }

    @Test
    void deleteSongTest() {
        Song song = new Song();
        song.setId(1L);

        when(songRepository.findById(1L)).thenReturn(Optional.of(song));
        doNothing().when(songRepository).delete(song);

        // Call the delete method
        songRepository.delete(song);

        // Verify that the song was deleted
        verify(songRepository, times(1)).delete(song);
    }
}
