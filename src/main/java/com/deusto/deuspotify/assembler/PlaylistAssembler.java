package com.deusto.deuspotify.assembler;

import com.deusto.deuspotify.model.Playlist;
import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.DTO.PlaylistDTO;
import com.deusto.deuspotify.DTO.SongDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class to convert between {@link Playlist} entities and {@link PlaylistDTO} data transfer objects.
 */
public class PlaylistAssembler {

    /**
     * Converts a {@link Playlist} entity to a {@link PlaylistDTO}.
     *
     * @param playlist the Playlist entity to convert
     * @return the corresponding PlaylistDTO, or null if input is null
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
     * Converts a {@link PlaylistDTO} to a {@link Playlist} entity.
     *
     * @param dto the PlaylistDTO to convert
     * @return the corresponding Playlist entity, or null if input is null
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
     * Converts a list of {@link Playlist} entities to a list of {@link PlaylistDTO}s.
     *
     * @param playlists list of Playlist entities
     * @return list of PlaylistDTOs, or null if input is null
     */
    public static List<PlaylistDTO> toDTOList(List<Playlist> playlists) {
        if (playlists == null) return null;

        return playlists.stream()
                .map(PlaylistAssembler::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts a list of {@link PlaylistDTO}s to a list of {@link Playlist} entities.
     *
     * @param dtos list of PlaylistDTOs
     * @return list of Playlist entities, or null if input is null
     */
    public static List<Playlist> toEntityList(List<PlaylistDTO> dtos) {
        if (dtos == null) return null;

        return dtos.stream()
                .map(PlaylistAssembler::toEntity)
                .collect(Collectors.toList());
    }
}
