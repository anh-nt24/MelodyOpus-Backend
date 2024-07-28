package org.anhnt24.melodyopus.service;

import jakarta.mail.MessagingException;
import org.anhnt24.melodyopus.entity.PasswordResetToken;
import org.anhnt24.melodyopus.entity.User;
import org.anhnt24.melodyopus.repository.PasswordResetTokenRepository;
import org.anhnt24.melodyopus.repository.UserRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Value("${validResetMinutes}")
    private int minutes;

    public void initiatePasswordReset(String email) throws ServiceException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ServiceException("User not found");
        }

        Optional<PasswordResetToken> resetToken = passwordResetTokenRepository.findByUser(user);
        if (resetToken.isPresent()) {
            passwordResetTokenRepository.delete(resetToken.get());
        }

        String tokenStr = UUID.randomUUID().toString();
        PasswordResetToken token = new PasswordResetToken(tokenStr, user, minutes); // 1 minute validity

        passwordResetTokenRepository.save(token);

        emailService.sendPasswordResetEmail(user.getEmail(), tokenStr);
    }

    private PasswordResetToken validatePasswordResetToken(String token) throws ServiceException {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ServiceException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(Instant.now())) {
            throw new ServiceException("Token has expired");
        }

        return resetToken;
    }

    public void resetPassword(String token) throws ServiceException {
        PasswordResetToken resetToken = this.validatePasswordResetToken(token);
        User user = resetToken.getUser();
        String temporaryPassword = this.generateTemporaryPassword();
        user.setPassword(passwordEncoder.encode(temporaryPassword));
        userRepository.save(user);
        passwordResetTokenRepository.delete(resetToken);

        emailService.sendTemporaryPasswordEmail(user.getEmail(), temporaryPassword);
    }

    private String generateTemporaryPassword() {
        int len = 10;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        char[] tempPassword = new char[len];
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < len; i++) {
            int index = random.nextInt(chars.length());
            tempPassword[i] = chars.charAt(index);
        }

        return new String(tempPassword);
    }

}
