package com.deusto.deuspotify.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for serving static files from a local folder.
 * <p>
 * Maps requests to "/songs_files/**" to files located in the "uploads/songs/"
 * directory on the file system.
 * </p>
 */
@Configuration
public class FileStorageConfig implements WebMvcConfigurer {

    /**
     * Adds a resource handler to serve files from the local file system.
     *
     * @param registry the resource handler registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/songs_files/**")
                .addResourceLocations("file:uploads/songs/");
    }
}
