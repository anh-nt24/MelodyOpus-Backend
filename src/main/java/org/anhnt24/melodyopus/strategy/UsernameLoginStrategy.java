package org.anhnt24.melodyopus.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.anhnt24.melodyopus.dto.AuthRequestDTO;
import org.anhnt24.melodyopus.dto.AuthResponseDTO;
import org.anhnt24.melodyopus.entity.User;
import org.anhnt24.melodyopus.service.AuthService;
import org.anhnt24.melodyopus.utils.TokenManager;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service("usernameLoginStrategy")
public class UsernameLoginStrategy implements LoginStrategy{
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private AuthService authService;

    @Override
    public AuthResponseDTO login(String credentials) throws GeneralSecurityException, IOException {
        AuthRequestDTO authRequestDTO = new ObjectMapper().readValue(credentials, AuthRequestDTO.class);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequestDTO.getUsername(), authRequestDTO.getPassword()
                    )
            );
            User user = authService.loadUserByUsername(authRequestDTO.getUsername());
            String jwtToken = tokenManager.generateJwtToken(user);
            AuthResponseDTO authResponseDTO = new AuthResponseDTO(user, jwtToken);
            return authResponseDTO;
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
}
