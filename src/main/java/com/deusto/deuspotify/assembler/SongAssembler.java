/**
 * @file SongAssembler.java
 * @brief Provides conversion methods between Song and SongDTO.
 */

package com.deusto.deuspotify.assembler;

import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.DTO.SongDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @class SongAssembler
 * @brief Utility class to convert between Song entities and SongDTOs.
 */
public class SongAssembler {

    /**
     * @brief Converts a Song entity to a SongDTO.
     * @param song The Song entity to convert.
     * @return The resulting SongDTO, or null if input is null.
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
     * @brief Converts a SongDTO to a Song entity.
     * @param dto The SongDTO to convert.
     * @return The resulting Song entity, or null if input is null.
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
     * @brief Converts a list of Song entities to a list of SongDTOs.
     * @param songs List of Song entities.
     * @return List of SongDTOs, or null if input is null.
     */
    public static List<SongDTO> toDTOList(List<Song> songs) {
        if (songs == null) return null;

        return songs.stream()
                .map(SongAssembler::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * @brief Converts a list of SongDTOs to a list of Song entities.
     * @param dtos List of SongDTOs.
     * @return List of Song entities, or null if input is null.
     */
    public static List<Song> toEntityList(List<SongDTO> dtos) {
        if (dtos == null) return null;

        return dtos.stream()
                .map(SongAssembler::toEntity)
                .collect(Collectors.toList());
    }
}
