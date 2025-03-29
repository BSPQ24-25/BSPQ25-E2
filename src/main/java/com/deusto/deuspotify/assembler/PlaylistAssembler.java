package com.deusto.deuspotify.assembler;

import com.deusto.deuspotify.model.Playlist;
import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.DTO.PlaylistDTO;
import com.deusto.deuspotify.DTO.SongDTO;

import java.util.List;
import java.util.stream.Collectors;

public class PlaylistAssembler {

    public static PlaylistDTO toDTO(Playlist playlist) {
        if (playlist == null) return null;

        List<SongDTO> songDTOs = SongAssembler.toDTOList(playlist.getSongs());

        return new PlaylistDTO(
                playlist.getId(),
                playlist.getOwners(),
                playlist.isPublic(),
                songDTOs,
                playlist.getNumberOfSongs(),
                playlist.getOrder()
        );
    }

    public static Playlist toEntity(PlaylistDTO dto) {
        if (dto == null) return null;

        List<Song> songs = SongAssembler.toEntityList(dto.getSongs());

        Playlist playlist = new Playlist();
        playlist.setId(dto.getId());
        playlist.setOwners(dto.getOwners());
        playlist.setPublic(dto.isPublic());
        playlist.setSongs(songs);
        playlist.setOrder(dto.getOrder());

        return playlist;
    }

    public static List<PlaylistDTO> toDTOList(List<Playlist> playlists) {
        if (playlists == null) return null;

        return playlists.stream()
                .map(PlaylistAssembler::toDTO)
                .collect(Collectors.toList());
    }

    public static List<Playlist> toEntityList(List<PlaylistDTO> dtos) {
        if (dtos == null) return null;

        return dtos.stream()
                .map(PlaylistAssembler::toEntity)
                .collect(Collectors.toList());
    }
}
