package org.anhnt24.melodyopus.service;

import org.anhnt24.melodyopus.dto.SongDTO;
import org.anhnt24.melodyopus.entity.Like;
import org.anhnt24.melodyopus.entity.Song;
import org.anhnt24.melodyopus.entity.User;
import org.anhnt24.melodyopus.repository.LikeRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private SongService songService;

    public Optional<Like> userLikesSong(User user, Song song) {
        return likeRepository.findByUserAndSong(user, song);
    }

    public void likeSong(User user, Song song) {
        try {
            Optional<Like> existingLike = this.userLikesSong(user, song);
            if (existingLike.isPresent()) {
                throw new ServiceException("User has already liked this song.");
            }
            Like like = new Like.Builder()
                    .song(song)
                    .user(user)
                    .build();

            likeRepository.save(like);
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void unlikeSong(User user, Song song) {
        try {
            Optional<Like> existingLike = this.userLikesSong(user, song);
            if (existingLike.isEmpty()) {
                throw new ServiceException("User has not liked this song.");
            }

            likeRepository.delete(existingLike.get());
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<SongDTO> getSongsLikedByUser(User user) {
        return likeRepository.findSongsByUser(user)
                .stream()
                .map(song -> songService.mapToDTO(song))
                .toList();
    }

    public Long getLikeCountForSong(Song song) {
        return likeRepository.countBySong(song);
    }
}
