package org.anhnt24.melodyopus.service;

import org.anhnt24.melodyopus.entity.User;
import org.anhnt24.melodyopus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public User loadUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}
