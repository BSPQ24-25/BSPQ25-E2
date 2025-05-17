package com.deusto.deuspotify.repositories;

import com.deusto.deuspotify.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository interface for accessing and managing {@link Profile} entities.
 * <p>
 * Extends {@link JpaRepository} to provide standard CRUD operations and custom
 * query methods.
 * </p>
 */
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    /**
     * Finds a profile by its username.
     *
     * @param username the username of the profile to find
     * @return an {@link Optional} containing the profile if found, or empty if not found
     */
    Optional<Profile> findByUsername(String username);
}
