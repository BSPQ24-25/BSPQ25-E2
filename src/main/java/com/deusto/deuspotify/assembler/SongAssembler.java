package com.deusto.deuspotify.assembler;

import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.DTO.SongDTO;

import java.util.List;
import java.util.stream.Collectors;

public class SongAssembler {

    public static SongDTO toDTO(Song song) {
        if (song == null) return null;

        return new SongDTO(
                song.getId(),
                song.getName(),
                song.getArtists(),
                song.getDuration(),
                song.getGenres(),
                song.getDateOfRelease(),
                song.getAlbum()
        );
    }

    public static Song toEntity(SongDTO dto) {
        if (dto == null) return null;

        Song song = new Song();
        song.setId(dto.getId());
        song.setName(dto.getName());
        song.setArtists(dto.getArtists());
        song.setDuration(dto.getDuration());
        song.setGenres(dto.getGenres());
        song.setDateOfRelease(dto.getDateOfRelease());
        song.setAlbum(dto.getAlbum());

        return song;
    }

    public static List<SongDTO> toDTOList(List<Song> songs) {
        if (songs == null) return null;

        return songs.stream()
                .map(SongAssembler::toDTO)
                .collect(Collectors.toList());
    }

    public static List<Song> toEntityList(List<SongDTO> dtos) {
        if (dtos == null) return null;

        return dtos.stream()
                .map(SongAssembler::toEntity)
                .collect(Collectors.toList());
    }
}
