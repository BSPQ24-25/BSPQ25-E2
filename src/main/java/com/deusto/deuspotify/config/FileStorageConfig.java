package com.deusto.deuspotify.config;
/**
 * @file FileStorageConfig.java
 * @brief Configuration class for serving static resources related to song files.
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @class FileStorageConfig
 * @brief Spring configuration class to expose a directory for serving uploaded song files.
 *
 * Maps requests to `/songs_files/**` to the local filesystem path `uploads/songs/`.
 */
@Configuration
public class FileStorageConfig implements WebMvcConfigurer {

    /**
     * @brief Configures resource handlers to serve static song files.
     * @param registry The ResourceHandlerRegistry to add the handler to.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/songs_files/**")
                .addResourceLocations("file:uploads/songs/");
    }
}
