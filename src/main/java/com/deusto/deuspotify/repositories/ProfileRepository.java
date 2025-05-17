package com.deusto.deuspotify.repositories;

import com.deusto.deuspotify.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * @interface ProfileRepository
 * @brief Repository interface for {@link Profile} entities.
 *
 * Provides CRUD operations and additional queries for user profiles.
 */
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    /**
     * Retrieves a profile by its username.
     * 
     * @param username The username to search for.
     * @return An {@link Optional} containing the found {@link Profile}, or empty if not found.
     */
    Optional<Profile> findByUsername(String username);
}
