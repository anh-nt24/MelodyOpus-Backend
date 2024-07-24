package org.anhnt24.melodyopus.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "follow")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Follow {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false)
    private User followed;

    public static class Builder {
        private User follower, followed;

        public Builder follower(User follower) {
            this.follower = follower;
            return this;
        }

        public Builder followed(User followed) {
            this.followed = followed;
            return this;
        }

        public Follow build() {
            Follow follow = new Follow();
            follow.setFollowed(this.followed);
            follow.setFollower(this.follower);
            return follow;
        }
    }
}
