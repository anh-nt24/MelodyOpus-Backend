package org.anhnt24.melodyopus.repository;

import org.anhnt24.melodyopus.entity.Follow;
import org.anhnt24.melodyopus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerAndFollowed(User follower, User followed);

    List<Follow> findByFollower(User follower);

    List<Follow> findByFollowed(User followed);

    Long countByFollowed(User followed);
}
