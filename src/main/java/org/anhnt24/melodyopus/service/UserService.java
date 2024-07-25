package org.anhnt24.melodyopus.service;

import jakarta.transaction.Transactional;
import org.anhnt24.melodyopus.dto.UserDTO;
import org.anhnt24.melodyopus.entity.User;
import org.anhnt24.melodyopus.repository.UserRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private Boolean checkIfEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    private Boolean checkIfUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public void registerUser(UserDTO userDTO) {
        // Check if email already exists
        if (checkIfEmailExists(userDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (checkIfUsernameExists(userDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User.Builder()
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .name(userDTO.getName())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();

        userRepository.save(user);
    }

    @Transactional
    public void createDefaultUser() {
        String adminUsername = "_admin_melodyopus_00";
        try {
            authService.loadUserByUsername(adminUsername);
        } catch (UsernameNotFoundException e) {
            User user = new User.Builder()
                    .email("admin@melodyopus.anh-nt24")
                    .password(passwordEncoder.encode("admin@melodyopus.anh-nt24@lalala"))
                    .name("ADMIN")
                    .isAdmin(true)
                    .username("_admin_melodyopus_00").build();
            userRepository.save(user);
        }
    }

    @Transactional
    public User getUserByUsername(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ServiceException("Error on get user by username: " + e.getMessage());
        }
    }

    @Transactional
    public User getUserById(Long userId) {
        try {
            return userRepository.findById(userId).orElse(null);
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ServiceException("Error on get user by username: " + e.getMessage());
        }
    }
}
