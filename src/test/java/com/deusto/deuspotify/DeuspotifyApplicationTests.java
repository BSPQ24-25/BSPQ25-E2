package com.deusto.deuspotify;

import com.deusto.deuspotify.model.Profile;
import com.deusto.deuspotify.Controllers.AuthController;
import com.deusto.deuspotify.model.Playlist;
import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.repositories.ProfileRepository;
import com.deusto.deuspotify.repositories.PlaylistRepository;
import com.deusto.deuspotify.repositories.SongRepository;
import com.deusto.deuspotify.security.JwtUtil;
import com.deusto.deuspotify.services.ProfileService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authenticationManager, profileService, jwtUtil);
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

    // Test for retrieving a profile by username
    @Test
    void findProfileByUsernameTest() {
        String username = "testUser";
        Profile profile = new Profile();
        profile.setUsername(username);

        when(profileRepository.findByUsername(username)).thenReturn(Optional.of(profile));

        Optional<Profile> foundProfile = profileRepository.findByUsername(username);

        assertTrue(foundProfile.isPresent());
        assertEquals(username, foundProfile.get().getUsername());
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
}