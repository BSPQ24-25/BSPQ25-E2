package com.deusto.deuspotify.assembler;

import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.DTO.SongDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for converting between Song entities and SongDTOs.
 */
public class SongAssembler {

    /**
     * Converts a Song entity to a SongDTO.
     *
     * @param song the Song entity to convert
     * @return the corresponding SongDTO
     */
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

    /**
     * Converts a SongDTO to a Song entity.
     *
     * @param dto the SongDTO to convert
     * @return the corresponding Song entity
     */
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

    /**
     * Converts a list of Song entities to a list of SongDTOs.
     *
     * @param songs the list of Song entities
     * @return list of SongDTOs
     */
    public static List<SongDTO> toDTOList(List<Song> songs) {
        if (songs == null) return null;
        return songs.stream()
                .map(SongAssembler::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts a list of SongDTOs to a list of Song entities.
     *
     * @param dtos the list of SongDTOs
     * @return list of Song entities
     */
    public static List<Song> toEntityList(List<SongDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
                .map(SongAssembler::toEntity)
                .collect(Collectors.toList());
    }
}
