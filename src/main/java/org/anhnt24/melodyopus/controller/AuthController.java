package org.anhnt24.melodyopus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Clock;
import org.anhnt24.melodyopus.dto.AuthRequestDTO;
import org.anhnt24.melodyopus.dto.AuthResponseDTO;
import org.anhnt24.melodyopus.dto.UserDTO;
import org.anhnt24.melodyopus.entity.User;
import org.anhnt24.melodyopus.service.AuthService;
import org.anhnt24.melodyopus.service.UserService;
import org.anhnt24.melodyopus.strategy.LoginContext;
import org.anhnt24.melodyopus.utils.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private LoginContext loginContext;

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
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }



    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody AuthRequestDTO authRequestDTO) {
        try {
            String credentials = new ObjectMapper().writeValueAsString(authRequestDTO);
            AuthResponseDTO authResponseDTO = loginContext.login("usernameLoginStrategy", credentials);
            return ResponseEntity.ok(authResponseDTO);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: " + e.getMessage());
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/google")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequestDTO authRequestDTO) {
        try {
            String credentials = authRequestDTO.getCredentials();
            AuthResponseDTO authResponseDTO = loginContext.login("googleLoginStrategy", credentials);
            return ResponseEntity.ok(authResponseDTO);
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/google/success")
    public ResponseEntity<?> loginSuccess(@RequestParam("email") String email,
                                          @RequestParam("name") String name,
                                          @RequestParam("avatar") String avatar) {
        User user = userService.findOrCreateUser(email, name, avatar);
        String jwtToken = tokenManager.generateJwtToken(user);
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(user, jwtToken);
        return ResponseEntity.ok(authResponseDTO);
    }

    @GetMapping("/google/failure")
    public ResponseEntity<?> loginFailure(@RequestParam("error") String error) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
