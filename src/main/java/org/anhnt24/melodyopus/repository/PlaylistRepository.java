package org.anhnt24.melodyopus.repository;

import org.anhnt24.melodyopus.entity.Playlist;
import org.anhnt24.melodyopus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    List<Playlist> findByPrivacyTrueAndStatusTrue();

    List<Playlist> findByUserIdAndStatusTrueAndPrivacyTrue(Long userId);

    List<Playlist> findByUserIdAndStatusTrue(Long userId);

    Optional<Playlist> findByIdAndUserAndStatusTrue(Long playlistId, User user);

    Optional<Playlist> findByIdAndPrivacyTrueAndStatusTrue(Long playlistId);
}
