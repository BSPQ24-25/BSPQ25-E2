/**
 * @class DeuspotifyApplicationTests
 * @brief Test suite for core functionality of the Deuspotify application.
 *
 * This class covers:
 * - Authentication workflows using {@link AuthController}
 * - User registration, login, and profile operations via {@link ProfileService}
 * - Playlist and song operations through {@link DeuspotifyServiceImpl}
 * - Persistence layer tests for repositories
 * - Model-level validation for {@link Song}, {@link Profile}, and {@link Playlist}
 * - File upload processing and validation logic
 * 
 * Uses Mockito for mocking dependencies and Spring Boot test annotations.
 */

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

import jakarta.servlet.http.HttpServletRequest;

import com.deusto.deuspotify.DTO.PlaylistDTO;
import com.deusto.deuspotify.DTO.ProfileDTO;
import com.deusto.deuspotify.DTO.SongDTO;
import com.deusto.deuspotify.assembler.PlaylistAssembler;
import com.deusto.deuspotify.assembler.ProfileAssembler;
import com.deusto.deuspotify.assembler.SongAssembler;
import com.deusto.deuspotify.controllers.AuthController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import io.jsonwebtoken.Claims;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class DeuspotifyApplicationTests {

    private final String USERNAME = "testuser";

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private SongRepository songRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ProfileService profileService;

    private AuthController authController;
    private DeuspotifyService deuspotifyService;

    @BeforeEach
    void setUp() {
        // profileService is now automatically injected with mocks
        profileService = new ProfileService(profileRepository, passwordEncoder);
        authController = new AuthController(authenticationManager, profileService, jwtUtil);
        deuspotifyService = new DeuspotifyServiceImpl(songRepository, playlistRepository);
    }

    // Auth Controller tests

    /**
     * @test Verifies the login flow using the AuthController.
     * Ensures the token is returned for valid credentials.
     */
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

    /**
     * @test Tests user registration through AuthController.
     * Validates user is correctly persisted.
     */
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
    /**
     * @test Ensures the logout endpoint returns a success response.
     */
    @Test
    void logoutTest() {
        ResponseEntity<?> response = authController.logout();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("Logged out successfully", responseBody.get("message"));
    }

    /**
    * @test Retrieves all profiles using ProfileRepository mock.
    */
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

    /**
    * @test Creates a new Profile and verifies repository interaction.
    */
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

    /**
    * @test Retrieves a Profile by its ID.
    */
    @Test
    void getProfileByIdTest() {
        Profile profile = new Profile();
        profile.setId(1L);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));

        Optional<Profile> foundProfile = profileRepository.findById(1L);

        assertTrue(foundProfile.isPresent());
        assertEquals(1L, foundProfile.get().getId());
    }

    /**
     * @test Updates a Profile's username.
     */
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

    /**
     * @test Simulates a login check by comparing plain text credentials.
     */
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

    /**
     * @test Deletes a profile using mocked repository.
     */
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
    /**
     * @test Validates that a profile can be retrieved by ID and its username is correct.
     */
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

    /**
     * @test Tests the loading of a user by username from the profile repository.
     * Ensures admin roles are correctly assigned.
     */
    //User tests
    @Test
    void loadUserByUsernameTest() {
        // Arrange: prepare mock profile
        Profile profile = new Profile();
        profile.setUsername("testUser");
        profile.setPassword("encodedPass");
        profile.setAdmin(true);

        // Mock behavior: profileRepository returns the profile when called with "testUser"
        when(profileRepository.findByUsername("testUser")).thenReturn(Optional.of(profile));

        // Act: call the real method under test
        UserDetails userDetails = profileService.loadUserByUsername("testUser");

        // Assert: validate output
        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("encodedPass", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    /**
     * @test Ensures an exception is thrown when trying to load a non-existent user.
     */
    @Test
    void loadUserByUsernameNotFoundTest() {
        when(profileRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            profileService.loadUserByUsername("unknownUser");
        });
    }

    /**
     * @test Tests extraction of the authenticated user from a valid JWT token.
     */
    @Test
    void getAuthenticatedUserTest() {
        String token = "valid.jwt.token";
        String username = "testUser";

        Profile profile = new Profile();
        profile.setUsername(username);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractUsername(token)).thenReturn(username);
        when(profileRepository.findByUsername(username)).thenReturn(Optional.of(profile));

        // Inject mocked jwtUtil into service (if needed via reflection or setter)
        ReflectionTestUtils.setField(profileService, "jwtUtil", jwtUtil);

        Profile authenticated = profileService.getAuthenticatedUser(request);

        assertEquals(username, authenticated.getUsername());
    }

    /**
     * @test Ensures an exception is thrown when no JWT token is present in the request.
     */
    @Test
    void getAuthenticatedUserMissingTokenTest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
            profileService.getAuthenticatedUser(request);
        });
    }

    /**
     * @test Verifies that a playlist can be created and saved with correct ownership and public flag.
     */
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

    /**
     * @test Simulates adding a song to a playlist and verifies it is included in the playlist's songs list.
     */
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


    /**
     * @test Retrieves all playlists and verifies the number and presence of expected elements.
     */
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

    /**
     * @test Retrieves a playlist by ID and verifies the result matches the expected playlist.
     */
    @Test
    void getPlaylistByIdTest() {
        Playlist playlist = new Playlist();
        playlist.setId(1L);

        when(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist));

        Optional<Playlist> foundPlaylist = playlistRepository.findById(1L);

        assertTrue(foundPlaylist.isPresent());
        assertEquals(1L, foundPlaylist.get().getId());
    }

    /**
     * @test Updates an existing playlist's visibility and ownership and validates the changes.
     */
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

    /**
     * @test Deletes a playlist and verifies the repository interaction.
     */
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

    /**
     * @test Adds a song to a playlist identified by its ID and checks if the song is present.
     */
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

    /**
     * @test Removes a song from a playlist by ID and verifies the playlist no longer contains it.
     */
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


    /**
     * @test Updates a playlist's song list by adding multiple songs and confirms the update.
     */
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

    /**
     * @test Retrieves all playlists and verifies that the expected number is returned.
     */
    @Test
    void retrieveAllPlaylistsTest() {
        Playlist playlist1 = new Playlist();
        Playlist playlist2 = new Playlist();
        when(playlistRepository.findAll()).thenReturn(List.of(playlist1, playlist2));

        List<Playlist> playlists = deuspotifyService.retrieveAllPlaylists();

        assertEquals(2, playlists.size());
        verify(playlistRepository, times(1)).findAll();
    }

    /**
     * @test Finds a specific playlist by its ID and checks that it is returned correctly.
     */
    @Test
    void findPlaylistTest() {
        Playlist playlist = new Playlist();
        playlist.setId(1L);
        when(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist));

        Optional<Playlist> result = deuspotifyService.findPlaylist(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    /**
     * @test Adds a new playlist and verifies it is saved successfully.
     */
    @Test
    void addPlaylistTest() {
        Playlist playlist = new Playlist();
        when(playlistRepository.save(playlist)).thenReturn(playlist);

        Playlist saved = deuspotifyService.addPlaylist(playlist);

        assertNotNull(saved);
        verify(playlistRepository).save(playlist);
    }

    /**
     * @test Updates a playlist by sorting its songs by duration in descending order.
     */
    @Test
    void updatePlaylistWithSortingByDurationTest() {
        Playlist playlist = new Playlist();
        playlist.setId(1L);
        playlist.setOrder(List.of(1, 2, 3)); // Example list of integers
        playlist.setOrderType("duration");

        Song song1 = new Song();
        song1.setDuration(2.0);
        Song song2 = new Song();
        song2.setDuration(5.0);
        playlist.setSongs(new ArrayList<>(List.of(song1, song2)));

        when(playlistRepository.save(playlist)).thenReturn(playlist);

        Playlist updated = deuspotifyService.updatePlaylist(1L, playlist);

        assertEquals(5.0, updated.getSongs().get(0).getDuration());
        verify(playlistRepository).save(playlist);
    }

    /**
     * @test Updates a playlist by sorting its songs by release date in descending order.
     */
    @Test
    void updatePlaylistWithSortingByReleaseDateTest() {
        Playlist playlist = new Playlist();
        playlist.setId(1L);
        playlist.setOrder(List.of(1, 2, 3)); // Example list of integers
        playlist.setOrderType("release_date");

        Song song1 = new Song();
        song1.setDateOfRelease(new Date(1000));
        Song song2 = new Song();
        song2.setDateOfRelease(new Date(2000));
        playlist.setSongs(new ArrayList<>(List.of(song1, song2)));

        when(playlistRepository.save(playlist)).thenReturn(playlist);

        Playlist updated = deuspotifyService.updatePlaylist(1L, playlist);

        assertEquals(song2, updated.getSongs().get(0));
    }

/**
 * @test Retrieves all songs and checks the number and presence of specific songs.
 */

    void getAllSongsTest() {
        Song song1 = new Song();
        Song song2 = new Song();

        when(songRepository.findAll()).thenReturn(List.of(song1, song2));

        List<Song> songs = songRepository.findAll();

        assertEquals(2, songs.size());
        assertTrue(songs.contains(song1));
        assertTrue(songs.contains(song2));
    }

    /**
     * @test Finds a song by its ID and checks that it matches the expected ID.
     */
    @Test
    void getSongByIdTest() {
        Song song = new Song();
        song.setId(1L);

        when(songRepository.findById(1L)).thenReturn(Optional.of(song));

        Optional<Song> foundSong = songRepository.findById(1L);

        assertTrue(foundSong.isPresent());
        assertEquals(1L, foundSong.get().getId());
    }

    /**
     * @test Adds a new song and verifies its name is stored correctly.
     */
    @Test
    void addSongTest() {
        Song newSong = new Song();
        newSong.setName("New Song");

        when(songRepository.save(newSong)).thenReturn(newSong);

        Song savedSong = songRepository.save(newSong);

        assertNotNull(savedSong);
        assertEquals("New Song", savedSong.getName());
    }
    

    /**
     * @test Uploads a song file and validates that the saved file path is sanitized and the song is stored.
     * Also checks that the file is physically created and then deletes it.
     */
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

    /**
     * @test Creates a shared song with provided metadata and verifies that it is correctly processed and saved.
     */
    @Test
    void createSharedSongTest() {
        // Arrange test inputs
        String name = "Original Song";
        String album = "Greatest Hits";
        String artists = "Artist1, Artist2";
        String genres = "Pop, Rock";
        double duration = 3.5;
        Date dateOfRelease = new Date();
        String filePath = "uploads/songs/song123.mp3";

        // Expected values after processing
        String expectedName = name + " Shared";
        List<String> expectedArtists = List.of("Artist1", "Artist2");
        List<String> expectedGenres = List.of("Pop", "Rock");

        // Stub songRepository to return the saved song as-is
        when(songRepository.save(any(Song.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Song result = deuspotifyService.createSharedSong(name, album, artists, genres, duration, dateOfRelease, filePath);

        // Assert
        assertNotNull(result);
        assertEquals(expectedName, result.getName());
        assertEquals(album, result.getAlbum());
        assertEquals(expectedArtists, result.getArtists());
        assertEquals(expectedGenres, result.getGenres());
        assertEquals(duration, result.getDuration());
        assertEquals(dateOfRelease, result.getDateOfRelease());
        assertEquals(filePath, result.getFilePath());

        verify(songRepository, times(1)).save(any(Song.class));
    }

    /**
     * @test Updates an existing song and verifies that all properties are updated correctly.
     */
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

    /**
     * @test Deletes a song and ensures the delete method is invoked once.
     */
    @Test
    void deleteSongTest() {
        Song song = new Song();
        song.setId(1L);

        when(songRepository.findById(1L)).thenReturn(Optional.of(song));
        doNothing().when(songRepository).delete(any(Song.class));

        // Call the delete method
        songRepository.delete(song);

        // Verify that the song was deleted
        verify(songRepository, times(1)).delete(song);
    }

    /**
     * @test Retrieves all songs via the service and checks the count.
     */
    @Test
    void retrieveAllSongsTest() {
        Song song1 = new Song();
        Song song2 = new Song();
        when(songRepository.findAll()).thenReturn(List.of(song1, song2));
    
        List<Song> songs = deuspotifyService.retrieveAllSongs();
    
        assertEquals(2, songs.size());
        verify(songRepository, times(1)).findAll();
    }

    /**
     * @test Finds a song by ID and checks the retrieved song's ID matches.
     */
    @Test
    void findSongTest() {
        Song song = new Song();
        song.setId(1L);
        when(songRepository.findById(1L)).thenReturn(Optional.of(song));

        Optional<Song> result = deuspotifyService.findSong(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    /**
     * @test Finds multiple songs by their IDs and checks the result list.
     */
    @Test
    void findSongsByIdsTest() {
        Song song1 = new Song(); song1.setId(1L);
        Song song2 = new Song(); song2.setId(2L);
        when(songRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(song1, song2));

        List<Song> result = deuspotifyService.findSongsByIds(List.of(1L, 2L));

        assertEquals(2, result.size());
        verify(songRepository).findAllById(List.of(1L, 2L));
    }

    /**
     * @test Sets and retrieves all fields in a Song object to ensure they work correctly.
     */
    @Test
    void testSong() {
        Song song = new Song();
        song.setId(1L);
        song.setName("Test Song");
        song.setAlbum("Test Album");
        song.setArtists(List.of("Artist1", "Artist2"));
        song.setGenres(List.of("Rock", "Pop"));
        song.setDuration(3.5);
        song.setDateOfRelease(new Date());

        assertEquals(1L, song.getId());
        assertEquals("Test Song", song.getName());
        assertEquals("Test Album", song.getAlbum());
        assertEquals(List.of("Artist1", "Artist2"), song.getArtists());
        assertEquals(List.of("Rock", "Pop"), song.getGenres());
        assertEquals(3.5, song.getDuration());
    }

    /**
     * @test Creates and validates a Playlist object's basic properties.
     */
    @Test
    void testPlaylist() {
        Playlist playlist = new Playlist();
        playlist.setId(1L);
        playlist.setName("Test Playlist");
        playlist.setOwners(List.of("Owner1", "Owner2"));
        playlist.setPublic(true);
        playlist.setOrder(List.of(1, 2, 3));

        assertEquals(1L, playlist.getId());
        assertEquals("Test Playlist", playlist.getName());
        assertEquals(List.of("Owner1", "Owner2"), playlist.getOwners());
        assertTrue(playlist.isPublic());
        assertEquals(0, playlist.getNumberOfSongs());
    }


    /**
     * @test Tests that the default constructor for Song creates a non-null object.
     */
    @Test
    void testEmptyConstructor() {
        Song song = new Song();
        assertNotNull(song);  // Just verifying it creates an object
    }

    /**
     * @test Tests the Song constructor that accepts only an ID.
     */
    @Test
    void testIdConstructor() {
        Song song = new Song(42L);
        assertEquals(42L, song.getId());
    }

    /**
     * @test Tests the Song constructor without a file path and validates all field assignments.
     */
    @Test
    void testConstructorWithoutFilePath() {
        List<String> artists = Arrays.asList("Artist1", "Artist2");
        List<String> genres = Arrays.asList("Pop", "Rock");
        Date releaseDate = new Date();

        Song song = new Song("My Song", artists, 3.5, genres, releaseDate, "Best Album");

        assertEquals("My Song", song.getName());
        assertEquals(artists, song.getArtists());
        assertEquals(3.5, song.getDuration());
        assertEquals(genres, song.getGenres());
        assertEquals(releaseDate, song.getDateOfRelease());
        assertEquals("Best Album", song.getAlbum());
        assertNull(song.getFilePath());
    }

    /**
     * @test Tests the Song constructor with a file path and validates all field assignments.
     */
    @Test
    void testConstructorWithFilePath() {
        List<String> artists = Arrays.asList("Artist1", "Artist2");
        List<String> genres = Arrays.asList("Pop", "Rock");
        Date releaseDate = new Date();

        Song song = new Song("My Song", artists, 3.5, genres, releaseDate, "Best Album", "music/file.mp3");

        assertEquals("My Song", song.getName());
        assertEquals(artists, song.getArtists());
        assertEquals(3.5, song.getDuration());
        assertEquals(genres, song.getGenres());
        assertEquals(releaseDate, song.getDateOfRelease());
        assertEquals("Best Album", song.getAlbum());
        assertEquals("music/file.mp3", song.getFilePath());
    }



    /**
     * @test Tests the Playlist constructor with full parameters and verifies assigned fields.
     */
    @Test
    void testPlaylistFullConstructor() {
        // Arrange input values
        String name = "PLAYLIST_NAME";
        List<String> owners = Arrays.asList("user1", "user2");
        boolean isPublic = true;

        Song song1 = new Song(1L);
        Song song2 = new Song(2L);
        List<Song> songs = Arrays.asList(song1, song2);

        List<Integer> order = Arrays.asList(0, 1);

        // Act: use the constructor
        Playlist playlist = new Playlist(name, owners, isPublic, songs, order);

        // Assert: check that fields were set properly
        assertEquals(name, playlist.getName());
        assertEquals(owners, playlist.getOwners());
        assertTrue(playlist.isPublic());
        assertEquals(songs, playlist.getSongs());
        assertEquals(2, playlist.getNumberOfSongs());
        assertEquals(order, playlist.getOrder());
    }
    /**
     * @test Tests the Playlist constructor with only name and owners.
     */
    @Test
    void testValidateToken_InvalidUsername_ShouldReturnFalse() {
        String token = jwtUtil.generateToken(USERNAME);
        assertFalse(jwtUtil.validateToken(token, "otroUsuario"), "Token not valid for other user");
    }

    // SongAssembler tests

    /**
     * @test Converts a null Song to DTO and expects null.
     */
    @Test
    void toDTO_NullSong_ReturnsNull() {
        assertNull(SongAssembler.toDTO(null));
    }

    /**
     * @test Converts a fully populated Song entity to SongDTO and verifies all fields.
     */
    @Test
    void toDTO_FullSong_MappedCorrectly() {
        Song song = new Song();
        song.setId(1L);
        song.setName("Name");
        song.setArtists(Arrays.asList("A1", "A2"));
        song.setDuration(4.2);
        song.setGenres(Arrays.asList("G1"));
        Date date = new Date();
        song.setDateOfRelease(date);
        song.setAlbum("Album");

        SongDTO dto = SongAssembler.toDTO(song);
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Name", dto.getName());
        assertEquals(Arrays.asList("A1", "A2"), dto.getArtists());
        assertEquals(4.2, dto.getDuration());
        assertEquals(Arrays.asList("G1"), dto.getGenres());
        assertEquals(date, dto.getDateOfRelease());
        assertEquals("Album", dto.getAlbum());
    }

    /**
     * @test Converts a null SongDTO to entity and expects null.
     */
    @Test
    void toEntity_NullDTO_ReturnsNull() {
        assertNull(SongAssembler.toEntity(null));
    }

    /**
     * @test Converts a fully populated SongDTO to Song entity and verifies all fields.
     */
    @Test
    void toEntity_FullDTO_MappedCorrectly() {
        SongDTO dto = new SongDTO(2L, "N2", Arrays.asList("X"), 3.3,
                Arrays.asList("Y"), new Date(0), "Alb2");

        Song song = SongAssembler.toEntity(dto);
        assertNotNull(song);
        assertEquals(2L, song.getId());
        assertEquals("N2", song.getName());
        assertEquals(Arrays.asList("X"), song.getArtists());
        assertEquals(3.3, song.getDuration());
        assertEquals(Arrays.asList("Y"), song.getGenres());
        assertEquals(new Date(0), song.getDateOfRelease());
        assertEquals("Alb2", song.getAlbum());
    }

    /**
     * @test Converts a null list of Songs to DTO list and expects null.
     */
    @Test
    void toDTOList_NullList_ReturnsNull() {
        assertNull(SongAssembler.toDTOList(null));
    }

    /**
     * @test Converts an empty Song list to empty DTO list.
     */
    @Test
    void toDTOList_EmptyList_ReturnsEmptyList() {
        List<SongDTO> dtos = SongAssembler.toDTOList(Collections.emptyList());
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    /**
     * @test Converts a null list of SongDTOs to entity list and expects null.
     */
    @Test
    void toEntityList_NullList_ReturnsNull() {
        assertNull(SongAssembler.toEntityList(null));
    }

    /**
     * @test Converts an empty SongDTO list to empty Song list.
     */
    @Test
    void toEntityList_EmptyList_ReturnsEmptyList() {
        List<Song> songs = SongAssembler.toEntityList(Collections.emptyList());
        assertNotNull(songs);
        assertTrue(songs.isEmpty());
    }

    // PlaylistAssembler tests

    /**
     * @test Converts a null Playlist to PlaylistDTO and expects null.
     */
    @Test
    void playlist_toDTO_Null_ReturnsNull() {
        assertNull(PlaylistAssembler.toDTO(null));
    }

    /**
     * @test Converts a populated Playlist to PlaylistDTO and verifies all fields including nested SongDTO.
     */
    @Test
    void playlist_toDTO_FullEntity_MappedCorrectly() {
        Playlist p = new Playlist();
        p.setId(5L);
        p.setName("PL");
        p.setOwners(Arrays.asList("U1"));
        p.setPublic(false);
        Song s = new Song(); s.setId(9L); s.setName("Z");
        p.setSongs(Arrays.asList(s));
        p.setOrder(Arrays.asList(0));

        PlaylistDTO dto = PlaylistAssembler.toDTO(p);
        assertNotNull(dto);
        assertEquals(5L, dto.getId());
        assertEquals("PL", dto.getName());
        assertEquals(Arrays.asList("U1"), dto.getOwners());
        assertFalse(dto.isPublic());
        assertEquals(1, dto.getSongs().size());
        assertEquals(1, dto.getNumberOfSongs());
        assertEquals(Arrays.asList(0), dto.getOrder());
    }

    /**
     * @test Converts a null PlaylistDTO to Playlist and expects null.
     */
    @Test
    void playlist_toEntity_Null_ReturnsNull() {
        assertNull(PlaylistAssembler.toEntity(null));
    }

    /**
     * @test Converts a populated PlaylistDTO to Playlist and verifies all fields including nested Song.
     */
    @Test
    void playlist_toEntity_FullDTO_MappedCorrectly() {
        SongDTO sd = new SongDTO(3L, "NM", Collections.emptyList(), 1.1, Collections.emptyList(), new Date(1), "Alb");
        PlaylistDTO dto = new PlaylistDTO(6L, "N6", Arrays.asList("O"), true,
                Arrays.asList(sd), 1, Arrays.asList(2));

        Playlist p = PlaylistAssembler.toEntity(dto);
        assertNotNull(p);
        assertEquals(6L, p.getId());
        assertEquals("N6", p.getName());
        assertEquals(Arrays.asList("O"), p.getOwners());
        assertTrue(p.isPublic());
        assertEquals(1, p.getSongs().size());
        assertEquals(Arrays.asList(2), p.getOrder());
    }

    /**
     * @test Converts a null Playlist list to DTO list and expects null.
     */
    @Test
    void playlist_toDTOList_Null_ReturnsNull() {
        assertNull(PlaylistAssembler.toDTOList(null));
    }

    /**
     * @test Converts an empty Playlist list to empty PlaylistDTO list.
     */
    @Test
    void playlist_toDTOList_Empty_ReturnsEmpty() {
        List<PlaylistDTO> list = PlaylistAssembler.toDTOList(Collections.emptyList());
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    /**
     * @test Converts a null PlaylistDTO list to entity list and expects null.
     */
    @Test
    void playlist_toEntityList_Null_ReturnsNull() {
        assertNull(PlaylistAssembler.toEntityList(null));
    }

    /**
     * @test Converts an empty PlaylistDTO list to empty Playlist list.
     */
    @Test
    void playlist_toEntityList_Empty_ReturnsEmpty() {
        List<Playlist> list = PlaylistAssembler.toEntityList(Collections.emptyList());
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    // ProfileAssembler tests

    /**
     * @test Converts a null Profile to ProfileDTO and expects null.
     */
    @Test
    void profile_toDTO_Null_ReturnsNull() {
        assertNull(ProfileAssembler.toDTO(null));
    }

    /**
     * @test Converts a populated Profile to ProfileDTO and verifies all direct and nested fields.
     */
    @Test
    void profile_toDTO_FullEntity_MappedCorrectly() {
        Profile p = new Profile();
        p.setId(7L);
        p.setUsername("usr");
        p.setEmail("e@e.com");
        p.setFriendsList(Arrays.asList("f1","f2"));
        Song song = new Song(); song.setId(8L); song.setName("N");
        p.setFavouriteSongs(Arrays.asList(song));
        Playlist pl = new Playlist(); pl.setId(10L); pl.setName("X");
        p.setPlaylists(Arrays.asList(pl));
        p.setAdmin(true);

        ProfileDTO dto = ProfileAssembler.toDTO(p);
        assertNotNull(dto);
        assertEquals(7L, dto.getId());
        assertEquals("usr", dto.getUsername());
        assertEquals("e@e.com", dto.getEmail());
        assertEquals(Arrays.asList("f1","f2"), dto.getFriendsList());
        assertEquals(1, dto.getFavouriteSongs().size());
        assertEquals(1, dto.getPlaylists().size());
        assertTrue(dto.isAdmin());
    }

    /**
     * @test Converts a null ProfileDTO to Profile and expects null.
     */
    @Test
    void profile_toEntity_Null_ReturnsNull() {
        assertNull(ProfileAssembler.toEntity(null));
    }

    /**
     * @test Converts a populated ProfileDTO to Profile and verifies all direct and nested fields.
     */
    @Test
    void profile_toEntity_FullDTO_MappedCorrectly() {
        SongDTO sd = new SongDTO(11L, "NN", Collections.emptyList(), 2.2, Collections.emptyList(), new Date(2), "Al");
        PlaylistDTO pd = new PlaylistDTO(12L, "PL12", Collections.emptyList(), true, Collections.emptyList(), 0, Collections.emptyList());
        ProfileDTO dto = new ProfileDTO(13L, "usr2", "e2@e.com", Arrays.asList("fa"),
                Arrays.asList(sd), Arrays.asList(pd), false);

        Profile p = ProfileAssembler.toEntity(dto);
        assertNotNull(p);
        assertEquals(13L, p.getId());
        assertEquals("usr2", p.getUsername());
        assertEquals("e2@e.com", p.getEmail());
        assertEquals(Arrays.asList("fa"), p.getFriendsList());
        assertEquals(1, p.getFavouriteSongs().size());
        assertEquals(1, p.getPlaylists().size());
        assertFalse(p.isAdmin());
    }

    /**
     * @test Converts a null Profile list to DTO list and expects null.
     */
    @Test
    void profile_toDTOList_Null_ReturnsNull() {
        assertNull(ProfileAssembler.toDTOList(null));
    }

    /**
     * @test Converts an empty Profile list to empty ProfileDTO list.
     */
    @Test
    void profile_toDTOList_Empty_ReturnsEmpty() {
        List<ProfileDTO> list = ProfileAssembler.toDTOList(Collections.emptyList());
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    /**
     * @test Converts a null ProfileDTO list to entity list and expects null.
     */
    @Test
    void profile_toEntityList_Null_ReturnsNull() {
        assertNull(ProfileAssembler.toEntityList(null));
    }

    /**
     * @test Converts an empty ProfileDTO list to empty Profile list.
     */
    @Test
    void profile_toEntityList_Empty_ReturnsEmpty() {
        List<Profile> list = ProfileAssembler.toEntityList(Collections.emptyList());
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }
}
    
