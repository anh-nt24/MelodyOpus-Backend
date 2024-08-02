package org.anhnt24.melodyopus.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.anhnt24.melodyopus.dto.ForgotPasswordDTO;
import org.anhnt24.melodyopus.dto.PasswordUpdateDTO;
import org.anhnt24.melodyopus.dto.UserDTO;
import org.anhnt24.melodyopus.entity.User;
import org.anhnt24.melodyopus.service.UserService;
import org.anhnt24.melodyopus.utils.TokenManager;
import org.anhnt24.melodyopus.utils.UserUtil;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private UserUtil userUtil;

    // get all users
    @GetMapping("/")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // get user by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (ServiceException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // get user by username
    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        try {
            User user = userService.getUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (ServiceException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // update user avatar
    @PutMapping("/avatar")
    public ResponseEntity<?> updateUserAvatar(HttpServletRequest request, @RequestParam("image") MultipartFile file) {
        try {
            User user = userUtil.getUserFromRequest(request);
            userService.updateUserAvatar(user, file);
            return ResponseEntity.ok("Avatar updated successfully.");
        } catch (ServiceException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping("/info")
    public ResponseEntity<?> updateUserInfo(HttpServletRequest request, @RequestBody UserDTO userDTO) {
        try {
            User user = userUtil.getUserFromRequest(request);
            userService.updateUserInfo(user, userDTO);
            return ResponseEntity.ok("User info updated successfully.");
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(HttpServletRequest request, @RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        try {
            User user = userUtil.getUserFromRequest(request);
            userService.updatePassword(user, passwordUpdateDTO);
            return ResponseEntity.ok("Password updated successfully.");
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // remove user (JWT required)
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeUser(HttpServletRequest request) {
        try {
            User user = userUtil.getUserFromRequest(request);
            userService.removeUser(user);
            return ResponseEntity.ok("User removed successfully.");
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
