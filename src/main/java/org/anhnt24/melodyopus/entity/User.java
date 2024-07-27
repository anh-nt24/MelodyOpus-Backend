package org.anhnt24.melodyopus.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 20, nullable = false, unique = true)
    private String username;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 256, nullable = false)
    private String password;

    @Column(length = 256)
    private String avatar;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean verified;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean enabled;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isAdmin;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (this.isAdmin) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("USER"));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public static class Builder {
        private String email, password, name, username, avatar;
        private Boolean enabled=true, verified=false, isAdmin=false;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder verified(boolean verified) {
            this.verified = verified;
            return this;
        }

        public Builder isAdmin(boolean isAdmin) {
            this.isAdmin = isAdmin;
            return this;
        }

        public User build() {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setName(name);
            user.setPassword(password);
            user.setAvatar(avatar);
            user.setEnabled(enabled);
            user.setVerified(verified);
            user.setIsAdmin(isAdmin);
            return user;
        }
    }
}
