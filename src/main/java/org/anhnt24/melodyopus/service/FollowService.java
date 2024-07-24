package org.anhnt24.melodyopus.service;

import org.anhnt24.melodyopus.entity.Follow;
import org.anhnt24.melodyopus.entity.User;
import org.anhnt24.melodyopus.repository.FollowRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FollowService {
    @Autowired
    private FollowRepository followRepository;

    public void followUser(User follower, User followed) throws ServiceException {
        Optional<Follow> existingFollow = followRepository.findByFollowerAndFollowed(follower, followed);
        if (existingFollow.isPresent()) {
            throw new ServiceException("User is already following this user.");
        }

        Follow follow = new Follow.Builder()
                .follower(follower)
                .followed(followed)
                .build();

        followRepository.save(follow);
    }

    public void unfollowUser(User follower, User followed) throws ServiceException {
        Optional<Follow> existingFollow = followRepository.findByFollowerAndFollowed(follower, followed);
        if (existingFollow.isEmpty()) {
            throw new ServiceException("User is not following this user.");
        }

        followRepository.delete(existingFollow.get());
    }

    // check if a user follows another user
    public Boolean isFollowing(User follower, User followed) {
        return followRepository.findByFollowerAndFollowed(follower, followed).isPresent();
    }

    public Long getNumberOfFollowers(User user) {
        return followRepository.countByFollowed(user);
    }
}
