package com.deusto.deuspotify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.deusto.deuspotify.services.DeuspotifyService;
import com.deusto.deuspotify.services.ProfileService;
import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestActiveConfig;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith({ SpringExtension.class, JUnitPerfInterceptor.class })
@SpringBootTest
public class DeuspotifyPerfIT {

    @Autowired
    private DeuspotifyService deuspotifyService;

    @Autowired
    private ProfileService profileService;

    // Configuración global de reporting de JUnitPerf
    @JUnitPerfTestActiveConfig
    private static final JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
        .reportGenerator(new HtmlReportGenerator(
            System.getProperty("user.dir") + "/target/reports/perf-report.html"))
        .build();

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 10_000, warmUpMs = 2_000)
    @JUnitPerfTestRequirement(executionsPerSec = 50, percentiles = "95:250ms")
    public void testRetrieveAllSongsPerformance() {
        deuspotifyService.retrieveAllSongs();
    }

    @Test
    @JUnitPerfTest(threads = 20, durationMs = 8_000, warmUpMs = 1_500)
    @JUnitPerfTestRequirement(executionsPerSec = 75, percentiles = "90:200ms,95:300ms")
    public void testRetrieveAllPlaylistsPerformance() {
        deuspotifyService.retrieveAllPlaylists();
    }

    @Test
    @JUnitPerfTest(threads = 5, durationMs = 6_000, warmUpMs = 1_000)
    @JUnitPerfTestRequirement(executionsPerSec = 25, percentiles = "95:500ms")
    public void testFindSongByIdPerformance() {
        deuspotifyService.findSong(1L);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 6_000, warmUpMs = 1_000)
    @JUnitPerfTestRequirement(executionsPerSec = 40, percentiles = "95:300ms")
    public void testFindPlaylistByIdPerformance() {
        deuspotifyService.findPlaylist(1L);
    }

    @Test
    @JUnitPerfTest(threads = 15, durationMs = 8_000, warmUpMs = 1_500)
    @JUnitPerfTestRequirement(executionsPerSec = 50, percentiles = "90:200ms,95:300ms")
    public void testGetAllProfilesPerformance() {
        profileService.getAllProfiles();
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 8_000, warmUpMs = 1_000)
    @JUnitPerfTestRequirement(executionsPerSec = 40, percentiles = "95:350ms")
    public void testLoginPerformance() {
        profileService.login("admin", "password");
    }
}
