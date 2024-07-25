package org.anhnt24.melodyopus.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.anhnt24.melodyopus.dto.PlaylistDTO;
import org.anhnt24.melodyopus.dto.SongDTO;
import org.anhnt24.melodyopus.entity.Playlist;
import org.anhnt24.melodyopus.entity.User;
import org.anhnt24.melodyopus.service.PlaylistItemService;
import org.anhnt24.melodyopus.service.PlaylistService;
import org.anhnt24.melodyopus.service.UserService;
import org.anhnt24.melodyopus.utils.UserUtil;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlist")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private PlaylistItemService playlistItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserUtil userUtil;

    // get all songs in a public playlist by playlist id
    @GetMapping("/{playlistId}/songs")
    public ResponseEntity<?> getAllSongsInPublicPlaylist(@PathVariable Long playlistId) {
        try {
            List<SongDTO> songs = playlistItemService.getSongsByPlaylistId(playlistId);
            return ResponseEntity.ok(songs);
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get playlists of a user by id
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPlaylistsByUserId(HttpServletRequest request, @PathVariable Long userId) {
        try {
            User user = userUtil.getUserFromRequest(request);
            if (user == null || !(user.getId().equals(userId))) {
                List<Playlist> playlists = playlistService.getPlaylistsByUserIdPublic(userId);
                return ResponseEntity.ok(playlists);
            }
            List<Playlist> playlists = playlistService.getPlaylistsByUserId(userId);
            return ResponseEntity.ok(playlists);
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // add new playlist (requires JWT)
    @PostMapping("/")
    public ResponseEntity<String> addNewPlaylist(HttpServletRequest request, @RequestBody PlaylistDTO playlistDTO) {
        try {
            User user = userUtil.getUserFromRequest(request);
            playlistService.addPlaylist(playlistDTO, user);
            return ResponseEntity.ok("Playlist added successfully");
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // add new song in playlist (requires JWT)
    @PostMapping("/{playlistId}/")
    public ResponseEntity<String> addNewSongToPlaylist(HttpServletRequest request, @PathVariable Long playlistId, @RequestParam Long songId) {
        try {
            User user = userUtil.getUserFromRequest(request);
            playlistItemService.addSongToPlaylist(user, playlistId, songId);
            return ResponseEntity.ok("Song added to playlist successfully.");
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // remove song from playlist (requires JWT)
    @DeleteMapping("/{playlistId}/")
    public ResponseEntity<String> removeSongFromPlaylist(HttpServletRequest request, @PathVariable Long playlistId, @RequestParam Long songId) {
        try {
            User user = userUtil.getUserFromRequest(request);
            playlistItemService.removeSongFromPlaylist(user, playlistId, songId);
            return ResponseEntity.ok("Song removed from playlist successfully.");
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get all public playlists
    @GetMapping("/public")
    public ResponseEntity<?> getAllPublicPlaylists() {
        try {
            List<Playlist> publicPlaylists = playlistService.getAllPublicPlaylists();
            return ResponseEntity.ok(publicPlaylists);
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // remove a playlist (requires JWT)
    @DeleteMapping("/{playlistId}/remove")
    public ResponseEntity<String> removePlaylist(HttpServletRequest request, @PathVariable Long playlistId) {
        try {
            User user = userUtil.getUserFromRequest(request);
            playlistService.removePlaylist(user, playlistId);
            return ResponseEntity.ok("Playlist removed successfully.");
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}