package org.anhnt24.melodyopus.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.anhnt24.melodyopus.dto.SongDTO;
import org.anhnt24.melodyopus.entity.Song;
import org.anhnt24.melodyopus.entity.User;
import org.anhnt24.melodyopus.service.LikeService;
import org.anhnt24.melodyopus.service.SongService;
import org.anhnt24.melodyopus.service.UserService;
import org.anhnt24.melodyopus.utils.UserUtil;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
public class LikeController {
    @Autowired
    private SongService songService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    private UserUtil userUtil;

    @PostMapping("/like")
    public ResponseEntity<?> likeASong(HttpServletRequest request, @RequestParam Long songId) {
        try {
            User user = userUtil.getUserFromRequest(request);
            Song song = songService.getASongById(songId);
            if (user == null || song == null) {
                return ResponseEntity.notFound().build();
            }
            likeService.likeSong(user, song);
            return ResponseEntity.ok().build();
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/unlike")
    public ResponseEntity<?> unlikeASong(HttpServletRequest request, @RequestParam Long songId) {
        try {
            User user = userUtil.getUserFromRequest(request);
            Song song = songService.getASongById(songId);
            if (user == null || song == null) {
                return ResponseEntity.notFound().build();
            }
            likeService.unlikeSong(user, song);
            return ResponseEntity.ok().build();
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkUserLikesSong(HttpServletRequest request, @RequestParam Long songId) {
        try {
            User user = userUtil.getUserFromRequest(request);
            Song song = songService.getASongById(songId);
            if (user == null || song == null) {
                return ResponseEntity.notFound().build();
            }
            Boolean userLikesSong = likeService.userLikesSong(user, song).isPresent();
            return ResponseEntity.ok(userLikesSong);
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @GetMapping("/user/{userId}/songs")
    public ResponseEntity<?> getSongsLikedByUser(HttpServletRequest request, @PathVariable Long userId) {
        try {
            User user = userUtil.getUserFromRequest(request);
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }
            List<SongDTO> songs = likeService.getSongsLikedByUser(user);
            return ResponseEntity.ok(songs);
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/song/{songId}/likes")
    public ResponseEntity<?> getLikeCountForSong(@PathVariable Long songId) {
        try {
            Song song = songService.getASongById(songId);
            if (song == null) {
                return ResponseEntity.badRequest().body("Song not found");
            }
            Long likeCount = likeService.getLikeCountForSong(song);
            return ResponseEntity.ok(likeCount);
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
