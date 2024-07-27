package org.anhnt24.melodyopus.service;

import io.jsonwebtoken.io.IOException;
import org.anhnt24.melodyopus.entity.User;
import org.anhnt24.melodyopus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found " + username);
        } else {
            return user;
        }
    }
}
