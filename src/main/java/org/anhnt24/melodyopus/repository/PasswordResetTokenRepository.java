package org.anhnt24.melodyopus.repository;

import org.anhnt24.melodyopus.entity.PasswordResetToken;
import org.anhnt24.melodyopus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUser(User user);
}
