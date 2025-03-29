package com.deusto.deuspotify.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.deusto.deuspotify.model.Song;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> { 

}
