/**
 * @file DeuspotifyApplication.java
 * @brief Main entry point of the Deuspotify Spring Boot application.
 */

package com.deusto.deuspotify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @class DeuspotifyApplication
 * @brief Starts the Spring Boot application.
 *
 * This is the main class that bootstraps the Deuspotify application.
 * It uses Spring Boot's auto-configuration mechanism to launch the app.
 */
@SpringBootApplication
public class DeuspotifyApplication {

    /**
     * Main method that runs the Spring Boot application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(DeuspotifyApplication.class, args);
    }
}
