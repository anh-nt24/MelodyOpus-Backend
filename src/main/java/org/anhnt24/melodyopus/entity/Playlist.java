package org.anhnt24.melodyopus.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "playlist")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate dateCreated;

    @Column(nullable = false)
    private Boolean privacy;

    @Column
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static class Builder {
        private String name;
        private Boolean privacy = true, status = true; // true = public, false = private
        private User user;
        private  LocalDate dateCreated;

        public Builder dateCreated(LocalDate dateCreated) {
            this.dateCreated = dateCreated;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder privacy(Boolean privacy) {
            this.privacy = privacy;
            return this;
        }

        public Builder status(Boolean status) {
            this.status = status;
            return this;
        }
        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Playlist build() {
            Playlist playlist = new Playlist();
            playlist.setName(this.name);
            playlist.setPrivacy(this.privacy);
            playlist.setUser(this.user);
            playlist.setDateCreated(LocalDate.now());
            return playlist;
        }
    }
}