package com.deusto.deuspotify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.deusto.deuspotify.services.DeuspotifyService;
import com.deusto.deuspotify.services.ProfileService;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestActiveConfig;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

public class DeuspotifyPerfomanceTest {
    @Autowired
    private DeuspotifyService deuspotifyService;

    @Autowired
    private ProfileService profileService;

    @JUnitPerfTestActiveConfig
    private static final JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
        .reportGenerator(new HtmlReportGenerator(System.getProperty("user.dir") + "/target/reports/perf-report.html"))
        .build();

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 10000, warmUpMs = 2000)
    @JUnitPerfTestRequirement(executionsPerSec = 50, percentiles = "95:250ms")
    public void testRetrieveAllSongsPerformance() {
        deuspotifyService.retrieveAllSongs();
    }

    @Test
    @JUnitPerfTest(threads = 20, durationMs = 8000, warmUpMs = 1500)
    @JUnitPerfTestRequirement(executionsPerSec = 75, percentiles = "90:200ms,95:300ms")
    public void testRetrieveAllPlaylistsPerformance() {
        deuspotifyService.retrieveAllPlaylists();
    }

    @Test
    @JUnitPerfTest(threads = 5, durationMs = 6000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 25, percentiles = "95:500ms")
    public void testFindSongByIdPerformance() {
        deuspotifyService.findSong(1L); // Make sure song with ID 1 exists in test DB
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 6000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 40, percentiles = "95:300ms")
    public void testFindPlaylistByIdPerformance() {
        deuspotifyService.findPlaylist(1L); // Make sure playlist with ID 1 exists
    }

    @Test
    @JUnitPerfTest(threads = 15, durationMs = 8000, warmUpMs = 1500)
    @JUnitPerfTestRequirement(executionsPerSec = 50, percentiles = "90:200ms,95:300ms")
    public void testGetAllProfilesPerformance() {
        profileService.getAllProfiles();
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 8000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 40, percentiles = "95:350ms")
    public void testLoginPerformance() {
        profileService.login("admin", "password"); // Ensure test data includes user with these credentials
    }
}
