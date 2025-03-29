package com.deusto.deuspotify.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.deusto.deuspotify.model.Playlist;
import com.deusto.deuspotify.model.Song;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    Optional<Song> findByNombre(String nombre);
}