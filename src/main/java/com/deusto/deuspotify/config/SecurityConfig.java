package com.deusto.deuspotify.config;

import com.deusto.deuspotify.services.ProfileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ProfileService profileService;

    public SecurityConfig(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/",
                "/login",
                "/login.html",
                "/register.html",
                "/styles.css",
                "/static/**"
            ).permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login.html")
            .loginProcessingUrl("/login")  // matches form action
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
            .ignoringRequestMatchers("/login", "/logout", "/api/**") // if you want to disable CSRF for these
        );
    
    return http.build();
}

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(profileService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(List.of(authProvider));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}