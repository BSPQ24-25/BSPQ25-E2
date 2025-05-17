/**
 * @file I18nController.java
 * @brief Controller that provides translations for different locales using message resource files.
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

/**
 * @class I18nController
 * @brief REST controller for retrieving internationalized message strings based on locale.
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
     * @brief Constructor for I18nController.
     * @param messageSource Spring's message source for resolving messages.
     */
    public I18nController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * @brief Returns translated key-value pairs based on the provided language.
     * @param lang Language code ("es", "en", "eu"). Defaults to "es".
     * @return A map of message keys and their localized translations.
     * @throws IOException If there's an error reading the property files.
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
     * @brief Loads all keys from all localized message property files.
     * @return A map containing keys from all message files.
     * @throws IOException If any file fails to load.
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
     * @brief Loads keys and values from a single message property file.
     * @param resource The resource pointing to a properties file.
     * @return A map of the keys and values in the file.
     * @throws IOException If the file cannot be read.
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
