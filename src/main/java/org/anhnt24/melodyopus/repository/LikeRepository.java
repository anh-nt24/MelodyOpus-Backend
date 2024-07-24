package org.anhnt24.melodyopus.repository;

import org.anhnt24.melodyopus.entity.Like;
import org.anhnt24.melodyopus.entity.Song;
import org.anhnt24.melodyopus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndSong(User user, Song song);

    List<Song> findSongsByUser(User user);

    Long countBySong(Song song);
}
