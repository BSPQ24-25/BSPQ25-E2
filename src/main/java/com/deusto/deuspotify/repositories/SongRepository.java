package com.deusto.deuspotify.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.deusto.deuspotify.model.Song;

/**
 * @interface SongRepository
 * @brief Repository interface for {@link Song} entities.
 *
 * Provides basic CRUD operations for songs using Spring Data JPA.
 */
@Repository
public interface SongRepository extends JpaRepository<Song, Long> { 

}
