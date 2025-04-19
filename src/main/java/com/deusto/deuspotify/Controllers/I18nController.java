package com.deusto.deuspotify.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.io.InputStreamReader;
import java.io.Reader;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class I18nController {

    private final MessageSource messageSource;

    @Value("classpath:messages.properties") // Cargar el archivo de mensajes general
    private Resource messagesProperties;

    @Value("classpath:messages_es.properties") // Cargar el archivo de mensajes en español
    private Resource messagesEsProperties;

    @Value("classpath:messages_en.properties") // Cargar el archivo de mensajes en inglés
    private Resource messagesEnProperties;

    public I18nController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping("/api/i18n")
    public Map<String, String> getTranslations(HttpServletRequest request) throws IOException {
        Locale locale = request.getLocale(); // Obtiene el locale del request

        // Cargar todas las claves de todos los archivos de propiedades
        Map<String, String> allKeys = loadAllPropertiesKeys();

        // Traducir todas las claves usando el MessageSource
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
