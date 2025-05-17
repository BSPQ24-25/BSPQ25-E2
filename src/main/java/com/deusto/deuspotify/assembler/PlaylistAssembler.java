/**
 * @file PlaylistAssembler.java
 * @brief Provides conversion methods between Playlist and PlaylistDTO.
 */

package com.deusto.deuspotify.assembler;

import com.deusto.deuspotify.model.Playlist;
import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.DTO.PlaylistDTO;
import com.deusto.deuspotify.DTO.SongDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @class PlaylistAssembler
 * @brief Utility class to convert between Playlist entities and PlaylistDTOs.
 */
public class PlaylistAssembler {

    /**
     * @brief Converts a Playlist entity to a PlaylistDTO.
     * @param playlist The Playlist entity to convert.
     * @return The resulting PlaylistDTO, or null if the input is null.
     */
    public static PlaylistDTO toDTO(Playlist playlist) {
        if (playlist == null) return null;

        List<SongDTO> songDTOs = SongAssembler.toDTOList(playlist.getSongs());

        return new PlaylistDTO(
                playlist.getId(),
                playlist.getName(),
                playlist.getOwners(),
                playlist.isPublic(),
                songDTOs,
                playlist.getNumberOfSongs(),
                playlist.getOrder()
        );
    }

    /**
     * @brief Converts a PlaylistDTO to a Playlist entity.
     * @param dto The PlaylistDTO to convert.
     * @return The resulting Playlist entity, or null if the input is null.
     */
    public static Playlist toEntity(PlaylistDTO dto) {
        if (dto == null) return null;

        List<Song> songs = SongAssembler.toEntityList(dto.getSongs());

        Playlist playlist = new Playlist();
        playlist.setId(dto.getId());
        playlist.setName(dto.getName());
        playlist.setOwners(dto.getOwners());
        playlist.setPublic(dto.isPublic());
        playlist.setSongs(songs);
        playlist.setOrder(dto.getOrder());

        return playlist;
    }

    /**
     * @brief Converts a list of Playlist entities to a list of PlaylistDTOs.
     * @param playlists List of Playlist entities.
     * @return List of PlaylistDTOs, or null if input is null.
     */
    public static List<PlaylistDTO> toDTOList(List<Playlist> playlists) {
        if (playlists == null) return null;

        return playlists.stream()
                .map(PlaylistAssembler::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * @brief Converts a list of PlaylistDTOs to a list of Playlist entities.
     * @param dtos List of PlaylistDTOs.
     * @return List of Playlist entities, or null if input is null.
     */
    public static List<Playlist> toEntityList(List<PlaylistDTO> dtos) {
        if (dtos == null) return null;

        return dtos.stream()
                .map(PlaylistAssembler::toEntity)
                .collect(Collectors.toList());
    }
}
