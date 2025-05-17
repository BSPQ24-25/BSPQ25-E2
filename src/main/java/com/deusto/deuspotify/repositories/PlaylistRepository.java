package com.deusto.deuspotify.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.deusto.deuspotify.model.Playlist;

/**
 * @interface PlaylistRepository
 * @brief Repository interface for managing {@link Playlist} entities.
 *
 * Provides CRUD operations and query methods for the Playlist entity using Spring Data JPA.
 */
@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    // Inherits default CRUD operations from JpaRepository
}
