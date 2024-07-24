package org.anhnt24.melodyopus.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "song")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Song {
    @Id
    @Column()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 256, nullable = false)
    private String title;

    @Column(length = 100, nullable = false)
    private String genre;

    @Column(length = 5000, nullable = false)
    private String lyric;

    @Column(nullable = false)
    private Long duration;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE) // store date only (ignore time)
    private Date releaseDate;

    @Column(nullable = false, unique = true)
    private String filePath;

    @Column
    private String thumbnail;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Long listened;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean status;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public static class Builder {
        private String title, genre, filePath, thumbnail, lyric;
        private Long duration=0L, listened=0L;
        private Date releaseDate = new Date();
        private User user;
        private Boolean status = true;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder genre(String genre) {
            this.genre = genre;
            return this;
        }

        public Builder lyric(String lyric) {
            this.lyric = lyric;
            return this;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder thumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        public Builder duration(Long duration) {
            this.duration = duration;
            return this;
        }

        public Builder listened(Long listened) {
            this.listened = listened;
            return this;
        }

        public Builder releaseDate(Date releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder status(Boolean status) {
            this.status = status;
            return this;
        }

        public Song build() {
            Song song = new Song();
            song.setTitle(title);
            song.setGenre(genre);
            song.setLyric(lyric);
            song.setFilePath(filePath);
            song.setThumbnail(thumbnail);
            song.setDuration(duration);
            song.setListened(listened);
            song.setReleaseDate(releaseDate);
            song.setUser(user);
            song.setStatus(status);
            return song;
        }
    }
}
