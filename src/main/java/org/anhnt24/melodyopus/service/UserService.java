package org.anhnt24.melodyopus.service;

import jakarta.transaction.Transactional;
import org.anhnt24.melodyopus.dto.PasswordUpdateDTO;
import org.anhnt24.melodyopus.dto.UserDTO;
import org.anhnt24.melodyopus.entity.User;
import org.anhnt24.melodyopus.repository.PasswordResetTokenRepository;
import org.anhnt24.melodyopus.repository.UserRepository;
import org.anhnt24.melodyopus.utils.FileUtil;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private EmailService emailService;


    private Boolean checkIfEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    private Boolean checkIfUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public void registerUser(UserDTO userDTO) {
        // check if email already exists
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
            throw new UsernameNotFoundException(e.getMessage());
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ServiceException("Error on get user by username: " + e.getMessage());
        }
    }

    public User findOrCreateUser(String email, String name, String avatar) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            String username = email.split("@")[0] + UUID.randomUUID();
            username = username.substring(0, 20);
            User userCreation = new User.Builder()
                    .email(email)
                    .name(name)
                    .password(passwordEncoder.encode(""))
                    .username(username)
                    .avatar(avatar)
                    .build();

            User userResult = userRepository.save(userCreation);
            return userResult;
        }
        return user;
    }

    public List<UserDTO> getAllUsers() {
        try {
            List<User> users = userRepository.findAllByEnabledTrue();
            return users
                    .stream()
                    .map((user -> mapToDTO(user)))
                    .toList();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getUsername(),
                user.getAvatar()
        );
    }

    public void updateUserAvatar(User user, MultipartFile file) {
        try {
            fileUtil.validateFile(file, Arrays.asList("image"));
            String avatarUrl = fileUtil.saveImageFile(file);

            user.setAvatar(avatarUrl);
            userRepository.save(user);
        }
        catch (IOException e) {
            throw new ServiceException("Failed to save file ", e);
        }
    }

    public void removeUser(User user) {
        user.setEnabled(false);
        userRepository.save(user);
    }

    public void updatePassword(User user, PasswordUpdateDTO passwordUpdateDTO) {
        String oldPassword = passwordUpdateDTO.getOldPassword();
        String newPassword = passwordUpdateDTO.getNewPassword();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(), oldPassword
                    )
            );

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        } catch (DisabledException e) {
            throw new DisabledException(e.getMessage());
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }


    }

    public void updateUserInfo(User user, UserDTO userDTO) {
        try {
            if (checkIfUsernameExists(user.getUsername())) {
                throw new ServiceException("Username already exists.");
            }

            user.setName(userDTO.getName());
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());

            userRepository.save(user);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
