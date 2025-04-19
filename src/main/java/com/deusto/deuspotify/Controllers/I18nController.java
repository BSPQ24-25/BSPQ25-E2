package com.deusto.deuspotify.controllers;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class I18nController {

    private final MessageSource messageSource;

    public I18nController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping("/api/i18n")
    public Map<String, String> getTranslations(HttpServletRequest request) {
        Locale locale = request.getLocale();

        // Claves de ejemplo. Añade más si las usas en el HTML
        List<String> keys = Arrays.asList(
            "login.title", "login.username", "login.password",
            "login.button", "login.newUser", "login.error"
        );

        return keys.stream()
                .collect(Collectors.toMap(
                        key -> key,
                        key -> messageSource.getMessage(key, null, locale)
                ));
    }
}
