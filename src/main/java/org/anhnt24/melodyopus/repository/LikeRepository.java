package org.anhnt24.melodyopus.repository;

import org.anhnt24.melodyopus.entity.Like;
import org.anhnt24.melodyopus.entity.Song;
import org.anhnt24.melodyopus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndSong(User user, Song song);

    @Query("SELECT l.song FROM Like l WHERE l.user = :user")
    List<Song> findSongsByUser(@Param("user") User user);

    Long countBySong(Song song);
}
