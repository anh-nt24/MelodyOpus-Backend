package org.anhnt24.melodyopus.repository;

import org.anhnt24.melodyopus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);
}
