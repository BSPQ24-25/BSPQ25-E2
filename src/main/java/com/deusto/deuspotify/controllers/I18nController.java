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

@RestController
public class I18nController {

    private final MessageSource messageSource;

    @Value("classpath:messages.properties") // General message file
    private Resource messagesProperties;

    @Value("classpath:messages_es.properties") // Spanish messages
    private Resource messagesEsProperties;

    @Value("classpath:messages_en.properties") // English messages
    private Resource messagesEnProperties;

    @Value("classpath:messages_eu.properties") // Basque messages
    private Resource messagesEuProperties;

    public I18nController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping("/api/i18n")
    public Map<String, String> getTranslations(@RequestParam(value = "lang", required = false) String lang) throws IOException {
        // Usar 'es' como valor por defecto si no se pasa ningún idioma
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

    private Map<String, String> loadAllPropertiesKeys() throws IOException {
        // Cargar todas las propiedades de los archivos de mensajes
        Map<String, String> keys = new HashMap<>();
        
        // Cargar el archivo de mensajes general
        keys.putAll(loadPropertiesKeys(messagesProperties));
        
        // Cargar el archivo de mensajes en español
        keys.putAll(loadPropertiesKeys(messagesEsProperties));
        
        // Cargar el archivo de mensajes en inglés
        keys.putAll(loadPropertiesKeys(messagesEnProperties));

        // Cargar el archivo de mensajes en euskera
        keys.putAll(loadPropertiesKeys(messagesEuProperties));
        
        return keys;
    }

    private Map<String, String> loadPropertiesKeys(Resource resource) throws IOException {
        Map<String, String> keys = new HashMap<>();
        try (Reader reader = new InputStreamReader(resource.getInputStream())) {
            Properties properties = new Properties();
            properties.load(reader);

            // Guardar todas las claves del archivo en el mapa
            for (String key : properties.stringPropertyNames()) {
                keys.put(key, properties.getProperty(key));
            }
        }
        return keys;
    }
}
