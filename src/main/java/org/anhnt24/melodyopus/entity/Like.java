package org.anhnt24.melodyopus.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_like")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    public static class Builder {
        private User user;
        private Song song;

        public Builder song(Song song) {
            this.song = song;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Like build() {
            Like like = new Like();
            like.setSong(song);
            like.setUser(user);
            return like;
        }
    }
}
