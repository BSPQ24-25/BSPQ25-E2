/**
 * @file I18nController.java
 * @brief Provides internationalization (i18n) support by loading translation keys and values.
 */

package com.deusto.deuspotify.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.io.InputStreamReader;
import java.io.Reader;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @class I18nController
 * @brief REST controller for internationalization messages in multiple languages.
 */
@RestController
public class I18nController {

    private final MessageSource messageSource;

    @Value("classpath:messages.properties")
    private Resource messagesProperties;

    @Value("classpath:messages_es.properties")
    private Resource messagesEsProperties;

    @Value("classpath:messages_en.properties")
    private Resource messagesEnProperties;

    @Value("classpath:messages_eu.properties")
    private Resource messagesEuProperties;

    /**
     * Constructor for I18nController.
     * @param messageSource Spring message source for localization.
     */
    public I18nController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Loads translated messages for the requested language.
     * @param lang Language code (e.g. "es", "en", "eu").
     * @return A map containing translation keys and their values.
     * @throws IOException If reading message files fails.
     */
    @GetMapping("/api/i18n")
    public Map<String, String> getTranslations(@RequestParam(value = "lang", required = false) String lang) throws IOException {
        Locale locale = switch (lang != null ? lang : "es") {
            case "en" -> Locale.ENGLISH;
            case "eu" -> new Locale("eu");
            default -> new Locale("es");
        };

        Map<String, String> allKeys = loadAllPropertiesKeys();

        return allKeys.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> messageSource.getMessage(entry.getKey(), null, locale)
                ));
    }

    /**
     * Loads all translation keys from all message files.
     * @return A map of all translation keys and values.
     * @throws IOException If reading from the files fails.
     */
    private Map<String, String> loadAllPropertiesKeys() throws IOException {
        Map<String, String> keys = new HashMap<>();
        keys.putAll(loadPropertiesKeys(messagesProperties));
        keys.putAll(loadPropertiesKeys(messagesEsProperties));
        keys.putAll(loadPropertiesKeys(messagesEnProperties));
        keys.putAll(loadPropertiesKeys(messagesEuProperties));
        return keys;
    }

    /**
     * Helper method to load keys from a specific properties file.
     * @param resource The resource to load.
     * @return A map of keys and values from the file.
     * @throws IOException If reading the file fails.
     */
    private Map<String, String> loadPropertiesKeys(Resource resource) throws IOException {
        Map<String, String> keys = new HashMap<>();
        try (Reader reader = new InputStreamReader(resource.getInputStream())) {
            Properties properties = new Properties();
            properties.load(reader);

            for (String key : properties.stringPropertyNames()) {
                keys.put(key, properties.getProperty(key));
            }
        }
        return keys;
    }
}
