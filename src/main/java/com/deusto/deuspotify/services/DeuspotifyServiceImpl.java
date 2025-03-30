package com.deusto.deuspotify.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.repositories.SongRepository;

@Service
public class DeuspotifyServiceImpl implements DeuspotifyService{
    
    @Autowired
    private SongRepository songRepository;

    
    public List<Song> retrieveAllSongs() {
        
        return songRepository.findAll();
    }

    public Optional<Song> findSong(Long id){
        return songRepository.findById(id);
    }

    public Song addSong(Song song) {
        
        return songRepository.save(song);
    }
    
}