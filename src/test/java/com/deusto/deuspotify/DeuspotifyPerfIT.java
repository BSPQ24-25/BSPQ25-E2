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

/**
 * @file DeuspotifyPerfIT.java
 * @brief Performance integration tests for Deuspotify using JUnitPerf.
 *
 * This class defines performance benchmarks for key service methods in the Deuspotify application.
 * It uses the JUnitPerf library to simulate concurrent load and validate performance metrics such as
 * throughput and latency percentiles.
 *
 * The results are saved in an HTML report under `target/reports/perf-report.html`.
 *
 * @author 
 * @date 2025
 */
@ExtendWith({ SpringExtension.class, JUnitPerfInterceptor.class })
@SpringBootTest
public class DeuspotifyPerfIT {

    @Autowired
    private DeuspotifyService deuspotifyService;

    @Autowired
    private ProfileService profileService;

    /**
     * Global JUnitPerf configuration for HTML reporting.
     * Report file is generated in: /target/reports/perf-report.html
     */
    // Configuración global de reporting de JUnitPerf
    @JUnitPerfTestActiveConfig
    private static final JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
        .reportGenerator(new HtmlReportGenerator(
            System.getProperty("user.dir") + "/target/reports/perf-report.html"))
        .build();

    /**
     * @test Tests performance of retrieving all songs from the database.
     * @junitperf 10 threads, 10 seconds, 2 seconds warm-up.
     * @requirement 50 executions/sec with 95% under 250ms.
     */
    @Test
    @JUnitPerfTest(threads = 10, durationMs = 10_000, warmUpMs = 2_000)
    @JUnitPerfTestRequirement(executionsPerSec = 50, percentiles = "95:250ms")
    public void testRetrieveAllSongsPerformance() {
        deuspotifyService.retrieveAllSongs();
    }

    /**
     * @test Tests performance of retrieving all playlists.
     * @junitperf 20 threads, 8 seconds, 1.5 seconds warm-up.
     * @requirement 75 executions/sec with 90% under 200ms and 95% under 300ms.
     */
    @Test
    @JUnitPerfTest(threads = 20, durationMs = 8_000, warmUpMs = 1_500)
    @JUnitPerfTestRequirement(executionsPerSec = 75, percentiles = "90:200ms,95:300ms")
    public void testRetrieveAllPlaylistsPerformance() {
        deuspotifyService.retrieveAllPlaylists();
    }

    /**
     * @test Tests performance of finding a song by ID.
     * @junitperf 5 threads, 6 seconds, 1 second warm-up.
     * @requirement 25 executions/sec with 95% under 500ms.
     */
    @Test
    @JUnitPerfTest(threads = 5, durationMs = 6_000, warmUpMs = 1_000)
    @JUnitPerfTestRequirement(executionsPerSec = 25, percentiles = "95:500ms")
    public void testFindSongByIdPerformance() {
        deuspotifyService.findSong(1L);
    }

    /**
     * @test Tests performance of finding a playlist by ID.
     * @junitperf 10 threads, 6 seconds, 1 second warm-up.
     * @requirement 40 executions/sec with 95% under 300ms.
     */
    @Test
    @JUnitPerfTest(threads = 10, durationMs = 6_000, warmUpMs = 1_000)
    @JUnitPerfTestRequirement(executionsPerSec = 40, percentiles = "95:300ms")
    public void testFindPlaylistByIdPerformance() {
        deuspotifyService.findPlaylist(1L);
    }

    /**
     * @test Tests performance of retrieving all user profiles.
     * @junitperf 15 threads, 8 seconds, 1.5 seconds warm-up.
     * @requirement 50 executions/sec with 90% under 200ms and 95% under 300ms.
     */
    @Test
    @JUnitPerfTest(threads = 15, durationMs = 8_000, warmUpMs = 1_500)
    @JUnitPerfTestRequirement(executionsPerSec = 50, percentiles = "90:200ms,95:300ms")
    public void testGetAllProfilesPerformance() {
        profileService.getAllProfiles();
    }

     /**
     * @test Tests performance of the login operation.
     * @junitperf 10 threads, 8 seconds, 1 second warm-up.
     * @requirement 40 executions/sec with 95% under 350ms.
     */
    @Test
    @JUnitPerfTest(threads = 10, durationMs = 8_000, warmUpMs = 1_000)
    @JUnitPerfTestRequirement(executionsPerSec = 40, percentiles = "95:350ms")
    public void testLoginPerformance() {
        profileService.login("admin", "password");
    }
}
