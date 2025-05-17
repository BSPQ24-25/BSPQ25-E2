package com.deusto.deuspotify.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.deusto.deuspotify.model.Playlist;

/**
 * Repository interface for managing Playlist entities.
 * <p>
 * This interface extends JpaRepository to provide CRUD operations and
 * query methods for the {@link Playlist} entity.
 * </p>
 */
@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    // You can define custom query methods here if needed
}
