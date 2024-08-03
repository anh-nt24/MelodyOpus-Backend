package org.anhnt24.melodyopus.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.anhnt24.melodyopus.dto.SongDTO;
import org.anhnt24.melodyopus.entity.User;
import org.anhnt24.melodyopus.service.AuthService;
import org.anhnt24.melodyopus.service.LikeService;
import org.anhnt24.melodyopus.service.SongService;
import org.anhnt24.melodyopus.service.UserService;
import org.anhnt24.melodyopus.utils.UserUtil;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/song")
public class SongController {
    @Autowired
    private SongService songService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserUtil userUtil;

//    @GetMapping("/filter")
//

    @GetMapping("/search")
    public ResponseEntity<?> searchSongByName(@RequestParam String query) {
        if (query == null || query.isEmpty()) {
            return ResponseEntity.badRequest().body("Keyword not found");
        }
        try {
            List<SongDTO> songDTOs = songService.searchSongs(query);
            return ResponseEntity.ok(songDTOs);
        } catch (ServiceException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllSongs(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size) {
        try {
            PageRequest req = PageRequest.of(page, size, Sort.by("releaseDate").descending());
            Page<SongDTO> songs = songService.getAllSongs(req);
            return ResponseEntity.ok(songs);
        } catch (ServiceException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong when fetching songs: " + e.getMessage());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getAllSongsOfUser(HttpServletRequest request, @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size) {
        try {
            User user = userUtil.getUserFromRequest(request);
            PageRequest req = PageRequest.of(page, size, Sort.by("releaseDate").descending());
            Page<SongDTO> songs = songService.getSongsByUser(user, req);
            return ResponseEntity.ok(songs);
        } catch (ServiceException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{songId}")
    public ResponseEntity<?> getASongById(@PathVariable Long songId) {
        try {
            SongDTO song = songService.mapToDTO(songService.getASongById(songId));
            return ResponseEntity.ok(song);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong when fetching songs: " + e.getMessage());
        }
    }

    @PutMapping("/listen")
    public ResponseEntity<?> updateListen(@RequestParam Long songId) {
        try {
            songService.updateListened(songId);
            return ResponseEntity.ok("Song's listen updated");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong when fetching songs: " + e.getMessage());
        }
    }

    @DeleteMapping("/{songId}")
    public ResponseEntity<?> deleteSong(HttpServletRequest request, @PathVariable Long songId) {
        try {
            User user = userUtil.getUserFromRequest(request);
            songService.deleteASong(user, songId);
            return ResponseEntity.ok("The song has been removed");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{songId}")
    public ResponseEntity<?> editSong(
            HttpServletRequest request,
            @PathVariable Long songId,
            @RequestPart("title") String title,
            @RequestPart("genre") String genre,
            @RequestPart("lyric") String lyric,
            @RequestPart("mp3File") MultipartFile mp3File,
            @RequestPart("thumbnail") MultipartFile thumbnail) {

        try {
            User user = userUtil.getUserFromRequest(request);
            songService.updateASong(user, songId, title, genre, lyric, mp3File, thumbnail);
            return ResponseEntity.ok("The song has been updated");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> addNewSong(HttpServletRequest request,
                                        @RequestPart("title") String title, // use RequestPart for form data, request body for json data
                                        @RequestPart("genre") String genre,
                                        @RequestPart("lyric") String lyric,
                                        @RequestPart("mp3File") MultipartFile mp3File,
                                        @RequestPart("thumbnail") MultipartFile thumbnail) {
        try {
            User user = userUtil.getUserFromRequest(request);
            songService.addNewSong(user, title, genre, lyric, mp3File, thumbnail);
            return ResponseEntity.ok("New song has been added");
        } catch (ServiceException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage());
        }
    }
}
