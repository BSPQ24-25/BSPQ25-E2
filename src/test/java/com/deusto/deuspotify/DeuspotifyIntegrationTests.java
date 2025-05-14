package com.deusto.deuspotify;

import com.deusto.deuspotify.model.Playlist;
import com.deusto.deuspotify.model.Profile;
import com.deusto.deuspotify.model.Song;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase
@Transactional
public class DeuspotifyIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Profile testProfile;
    private Song testSong;
    private Playlist testPlaylist;

    @BeforeEach
    void setUp() {
        testProfile = new Profile();
        testProfile.setUsername("johndoe");
        testProfile.setPassword("password123");
        testProfile.setEmail("john@example.com");

        testSong = new Song();
        testSong.setName("Test Song");
        testSong.setAlbum("Test Album");
        testSong.setArtists(Arrays.asList("Artist1", "Artist2"));
        testSong.setGenres(Arrays.asList("Rock"));
        testSong.setDuration(180.0);
        testSong.setDateOfRelease(new Date());
        testSong.setFilePath("test.mp3");

        testPlaylist = new Playlist();
        testPlaylist.setName("My Playlist");
        testPlaylist.setOwners(List.of("johndoe"));
        testPlaylist.setPublic(true);
        testPlaylist.setSongs(List.of());
        testPlaylist.setOrder(List.of());
    }

    @Test
    void testAuthRegisterAndLogin() throws Exception {
        // Register
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProfile)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.username").value("johndoe"));

        // Login
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    Map.of("username", "johndoe", "password", "password123")
                )))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testProfileCrud() throws Exception {
        // Create
        String profileJson = mockMvc.perform(post("/api/profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProfile)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Profile created = objectMapper.readValue(profileJson, Profile.class);

        // Get all
        mockMvc.perform(get("/api/profiles"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[?(@.username=='johndoe')]").exists());

        // Get by id
        mockMvc.perform(get("/api/profiles/" + created.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("john@example.com"));

        // Update
        created.setEmail("john2@example.com");
        mockMvc.perform(put("/api/profiles/" + created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(created)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("john2@example.com"));

        // Delete
        mockMvc.perform(delete("/api/profiles/" + created.getId()))
            .andExpect(status().isOk());
        mockMvc.perform(get("/api/profiles/" + created.getId()))
            .andExpect(status().isNotFound());
    }

    @Test
    void testSongCrud() throws Exception {
        // Add song
        String songJson = mockMvc.perform(post("/api/songs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSong)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Song createdSong = objectMapper.readValue(songJson, Song.class);

        // Retrieve all
        mockMvc.perform(get("/api/songs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[?(@.id==" + createdSong.getId() + ")]").exists());

        // Retrieve by id
        mockMvc.perform(get("/api/songs/" + createdSong.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Test Song"));

        // Update release date
        long newDate = new Date().getTime();
        mockMvc.perform(put("/api/songs/" + createdSong.getId())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("date_of_release", String.valueOf(newDate)))
            .andExpect(status().isOk());

        // Delete
        mockMvc.perform(delete("/api/songs/" + createdSong.getId()))
            .andExpect(status().isOk());

        // Tras borrado, end-point devuelve 200 con cuerpo "null"
        mockMvc.perform(get("/api/songs/" + createdSong.getId()))
            .andExpect(status().isOk())
            .andExpect(content().string("null"));
    }

    @Test
    void testPlaylistCrud() throws Exception {
        String songJson = mockMvc.perform(post("/api/songs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSong)))
            .andReturn().getResponse().getContentAsString();
        Song createdSong = objectMapper.readValue(songJson, Song.class);

        // Create playlist
        String plJson = mockMvc.perform(post("/api/playlists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPlaylist)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Playlist createdPl = objectMapper.readValue(plJson, Playlist.class);

        // Add song to playlist
        mockMvc.perform(put("/api/playlists/" + createdPl.getId() + "/songs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(createdSong.getId()))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.songs[0].id").value(createdSong.getId()));

        // Retrieve all
        mockMvc.perform(get("/api/playlists"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[?(@.id==" + createdPl.getId() + ")]").exists());

        // Retrieve by id
        mockMvc.perform(get("/api/playlists/" + createdPl.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("My Playlist"));

        // Update
        createdPl.setName("Updated Playlist");
        mockMvc.perform(put("/api/playlists/" + createdPl.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdPl)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Updated Playlist"));

        // Delete
        mockMvc.perform(delete("/api/playlists/" + createdPl.getId()))
            .andExpect(status().isOk());

        // Tras borrado, end-point devuelve 200 con cuerpo "null"
        mockMvc.perform(get("/api/playlists/" + createdPl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().string("null"));
    }
}
