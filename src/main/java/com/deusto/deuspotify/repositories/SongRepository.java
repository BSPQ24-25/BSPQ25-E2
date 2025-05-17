package com.deusto.deuspotify.repositories;

import com.deusto.deuspotify.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Song} entities in the database.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations
 * and pagination capabilities for the {@code Song} entity.
 * </p>
 */
@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    // Additional custom query methods can be defined here if needed
}
