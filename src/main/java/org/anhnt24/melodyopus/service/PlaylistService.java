package org.anhnt24.melodyopus.service;

import org.anhnt24.melodyopus.dto.PlaylistDTO;
import org.anhnt24.melodyopus.entity.Playlist;
import org.anhnt24.melodyopus.entity.User;
import org.anhnt24.melodyopus.repository.PlaylistRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    // add a new playlist
    public void addPlaylist(PlaylistDTO playlistDTO, User user) {
        Playlist playlist = new Playlist.Builder()
                .name(playlistDTO.getName())
                .privacy(playlistDTO.getPrivacy())
                .user(user)
                .build();
        playlistRepository.save(playlist);
    }

    // get playlists by user id (public)
    public List<Playlist> getPlaylistsByUserIdPublic(Long userId) {
        return playlistRepository.findByUserIdAndStatusTrueAndPrivacyTrue(userId);
    }

    // get playlists by user id (private & public)
    public List<Playlist> getPlaylistsByUserId(Long userId) {
        return playlistRepository.findByUserIdAndStatusTrue(userId);
    }

    // get all public playlists
    public List<Playlist> getAllPublicPlaylists() {
        return playlistRepository.findByPrivacyTrueAndStatusTrue();
    }

    // remove playlist
    public void removePlaylist(User user, Long playlistId) throws ServiceException {
        Playlist playlist = playlistRepository.findByIdAndUserAndStatusTrue(playlistId, user)
                .orElseThrow(() -> new ServiceException("Playlist not found or already removed"));
        playlist.setStatus(false);
        playlistRepository.save(playlist);
    }
}