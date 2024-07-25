package org.anhnt24.melodyopus.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.anhnt24.melodyopus.entity.User;
import org.anhnt24.melodyopus.service.FollowService;
import org.anhnt24.melodyopus.service.UserService;
import org.anhnt24.melodyopus.utils.UserUtil;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follows")
public class FollowController {
    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    private UserUtil userUtil;

    // Endpoint to follow a user
    @PostMapping("/follow")
    public ResponseEntity<?> followUser(HttpServletRequest request, @RequestParam Long followedId) {
        try {
            User follower = userUtil.getUserFromRequest(request);
            User followed = userService.getUserById(followedId);
            if (follower == null || followed == null) {
                return ResponseEntity.badRequest().body("User not found");
            }
            followService.followUser(follower, followed);
            return ResponseEntity.ok("User followed successfully.");
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint to unfollow a user
    @PostMapping("/unfollow")
    public ResponseEntity<String> unfollowUser(HttpServletRequest request, @RequestParam Long followedId) {
        try {
            User follower = userUtil.getUserFromRequest(request);
            User followed = userService.getUserById(followedId);
            if (follower == null || followed == null) {
                return ResponseEntity.badRequest().body("User not found");
            }
            followService.unfollowUser(follower, followed);
            return ResponseEntity.ok("User unfollowed successfully.");
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> isFollowing(HttpServletRequest request, @RequestParam Long followedId) {
        try {
            User follower = userUtil.getUserFromRequest(request);
            User followed = userService.getUserById(followedId);
            if (follower == null || followed == null) {
                return ResponseEntity.badRequest().body("User not found");
            }
            boolean following = followService.isFollowing(follower, followed);
            return ResponseEntity.ok(following);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/followers/count")
    public ResponseEntity<?> getNumberOfFollowers(@RequestParam Long userId) {
        try {
            User user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }
            Long followerCount = followService.getNumberOfFollowers(user);
            return ResponseEntity.ok(followerCount);
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
