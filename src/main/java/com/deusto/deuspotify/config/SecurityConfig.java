/**
 * @file SecurityConfig.java
 * @brief Configuration class for Spring Security and internationalization.
 */

package com.deusto.deuspotify.config;

import com.deusto.deuspotify.services.ProfileService;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

/**
 * @class SecurityConfig
 * @brief Configures Spring Security filters, authentication, password encoding, and internationalization.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ProfileService profileService;

    /**
     * @brief Constructor that injects the ProfileService for authentication.
     * @param profileService Service used to retrieve user details.
     */
    public SecurityConfig(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * @brief Configures the security filter chain for HTTP requests.
     * @param http The HttpSecurity object to configure.
     * @return The configured SecurityFilterChain.
     * @throws Exception if configuration fails.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/", "/login", "/login.html", "/register.html",
                    "/styles.css", "/static/**", "/api/i18n",
                    "/api/profiles", "/auth/register", "/auth/login"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/index.html", true)
                .failureUrl("/login.html?error=true")
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/login", "/logout", "/api/**", "/auth/**")
            );

        return http.build();
    }

    /**
     * @brief Provides a custom AuthenticationManager with a DAO authentication provider.
     * @param authenticationConfiguration The authentication configuration.
     * @return A configured AuthenticationManager.
     * @throws Exception if setup fails.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(profileService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(List.of(authProvider));
    }

    /**
     * @brief Bean for password encoding using BCrypt.
     * @return A BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ----------------- Internationalization -----------------

    /**
     * @brief Configures the message source for i18n.
     * @return A ResourceBundleMessageSource configured with UTF-8.
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * @brief Configures the default locale resolver.
     * @return A LocaleResolver with default locale set to Spanish.
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(new Locale("es"));
        return resolver;
    }
}
