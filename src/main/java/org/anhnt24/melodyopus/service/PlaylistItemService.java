package org.anhnt24.melodyopus.service;

import org.anhnt24.melodyopus.dto.SongDTO;
import org.anhnt24.melodyopus.entity.Playlist;
import org.anhnt24.melodyopus.entity.PlaylistItem;
import org.anhnt24.melodyopus.entity.Song;
import org.anhnt24.melodyopus.entity.User;
import org.anhnt24.melodyopus.repository.PlaylistItemRepository;
import org.anhnt24.melodyopus.repository.PlaylistRepository;
import org.anhnt24.melodyopus.repository.SongRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistItemService {

    @Autowired
    private PlaylistItemRepository playlistItemRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private SongService songService;

    // get all songs in a public playlist by playlist id
    public List<SongDTO> getSongsByPlaylistId(Long playlistId) throws ServiceException {
        Playlist playlist = playlistRepository.findByIdAndPrivacyTrueAndStatusTrue(playlistId)
                .orElseThrow(() -> new ServiceException("Public playlist not found"));
        return playlistItemRepository.findSongsByPlaylist(playlist)
                .stream()
                .map(song -> songService.mapToDTO(song))
                .toList();
    }

    // add new song to playlist
    public void addSongToPlaylist(User user, Long playlistId, Long songId) throws ServiceException {
        Playlist playlist = playlistRepository.findByIdAndUserAndStatusTrue(playlistId, user)
                .orElseThrow(() -> new ServiceException("Playlist not found or already removed"));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ServiceException("Song not found"));

        Optional<PlaylistItem> existingItem = playlistItemRepository.findByPlaylistAndSong(playlist, song);
        if (existingItem.isPresent()) {
            throw new ServiceException("Song is already in the playlist.");
        }

        PlaylistItem playlistItem = new PlaylistItem.Builder()
                .playlist(playlist)
                .song(song)
                .build();

        playlistItemRepository.save(playlistItem);
    }

    // remove song from playlist
    public void removeSongFromPlaylist(User user, Long playlistId, Long songId) throws ServiceException {
        Playlist playlist = playlistRepository.findByIdAndUserAndStatusTrue(playlistId, user)
                .orElseThrow(() -> new ServiceException("Playlist not found or already removed"));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ServiceException("Song not found"));

        Optional<PlaylistItem> existingItem = playlistItemRepository.findByPlaylistAndSong(playlist, song);
        if (existingItem.isEmpty()) {
            throw new ServiceException("Song is not in the playlist.");
        }

        playlistItemRepository.delete(existingItem.get());
    }
}