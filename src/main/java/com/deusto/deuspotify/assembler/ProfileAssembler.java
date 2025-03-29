package com.deusto.deuspotify.assembler;

import com.deusto.deuspotify.model.Profile;
import com.deusto.deuspotify.model.Song;
import com.deusto.deuspotify.model.Playlist;
import com.deusto.deuspotify.DTO.ProfileDTO;
import com.deusto.deuspotify.DTO.SongDTO;
import com.deusto.deuspotify.DTO.PlaylistDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ProfileAssembler {

    public static ProfileDTO toDTO(Profile profile) {
        if (profile == null) return null;

        List<SongDTO> songDTOs = SongAssembler.toDTOList(profile.getFavouriteSongs());
        List<PlaylistDTO> playlistDTOs = PlaylistAssembler.toDTOList(profile.getPlaylists());

        return new ProfileDTO(
                profile.getId(),
                profile.getUsername(),
                profile.getEmail(),
                profile.getFriendsList(),
                songDTOs,
                playlistDTOs,
                profile.isAdmin()
        );
    }

    public static Profile toEntity(ProfileDTO dto) {
        if (dto == null) return null;

        List<Song> songs = SongAssembler.toEntityList(dto.getFavouriteSongs());
        List<Playlist> playlists = PlaylistAssembler.toEntityList(dto.getPlaylists());

        Profile profile = new Profile();
        profile.setId(dto.getId());
        profile.setUsername(dto.getUsername());
        profile.setEmail(dto.getEmail());
        profile.setFriendsList(dto.getFriendsList());
        profile.setFavouriteSongs(songs);
        profile.setPlaylists(playlists);
        profile.setAdmin(dto.isAdmin());

        return profile;
    }

    public static List<ProfileDTO> toDTOList(List<Profile> profiles) {
        if (profiles == null) return null;

        return profiles.stream()
                .map(ProfileAssembler::toDTO)
                .collect(Collectors.toList());
    }

    public static List<Profile> toEntityList(List<ProfileDTO> dtos) {
        if (dtos == null) return null;

        return dtos.stream()
                .map(ProfileAssembler::toEntity)
                .collect(Collectors.toList());
    }
}
