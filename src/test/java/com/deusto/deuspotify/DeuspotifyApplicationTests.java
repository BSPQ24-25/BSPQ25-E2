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
}
