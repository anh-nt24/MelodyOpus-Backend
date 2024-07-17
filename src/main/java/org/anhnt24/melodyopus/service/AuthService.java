package org.anhnt24.melodyopus.service;

import org.anhnt24.melodyopus.entity.User;
import org.anhnt24.melodyopus.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements UserDetailsService {
    private final AuthRepository authRepository;
    @Autowired
    AuthServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        User user = authRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Email not found " + email);
        } else {
            return 
        }
    }
}
