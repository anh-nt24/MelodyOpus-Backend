package org.anhnt24.melodyopus.controller;

import org.anhnt24.melodyopus.dto.AuthRequestDTO;
import org.anhnt24.melodyopus.dto.AuthResponseDTO;
import org.anhnt24.melodyopus.dto.UserDTO;
import org.anhnt24.melodyopus.entity.User;
import org.anhnt24.melodyopus.service.AuthService;
import org.anhnt24.melodyopus.service.UserService;
import org.anhnt24.melodyopus.utils.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenManager tokenManager;

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody AuthRequestDTO authRequestDTO) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequestDTO.getUsername(), authRequestDTO.getPassword()
                    )
            );
            User user = authService.loadUserByUsername(authRequestDTO.getUsername());
            String jwtToken = tokenManager.generateJwtToken(user);
            AuthResponseDTO authResponseDTO = new AuthResponseDTO(jwtToken, user.getId());
            return ResponseEntity.ok(authResponseDTO);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (DisabledException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: " + e.getMessage());
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> userRegistration(@RequestBody UserDTO userDTO) {
        try {
            userService.registerUser(userDTO);
            return ResponseEntity.ok("User has been created successfully");
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}
