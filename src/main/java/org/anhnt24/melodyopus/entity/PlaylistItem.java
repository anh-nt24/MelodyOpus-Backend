package org.anhnt24.melodyopus.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "playlist_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    @ManyToOne
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    public static class Builder {
        private Playlist playlist;
        private Song song;

        public Builder playlist(Playlist playlist) {
            this.playlist = playlist;
            return this;
        }

        public Builder song(Song song) {
            this.song = song;
            return this;
        }

        public PlaylistItem build() {
            PlaylistItem item = new PlaylistItem();
            item.setPlaylist(this.playlist);
            item.setSong(this.song);
            return item;
        }
    }
}
