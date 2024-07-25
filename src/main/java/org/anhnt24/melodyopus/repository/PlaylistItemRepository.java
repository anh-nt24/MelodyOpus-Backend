package org.anhnt24.melodyopus.repository;

import org.anhnt24.melodyopus.dto.SongDTO;
import org.anhnt24.melodyopus.entity.Playlist;
import org.anhnt24.melodyopus.entity.PlaylistItem;
import org.anhnt24.melodyopus.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistItemRepository extends JpaRepository<PlaylistItem, Long> {

    @Query("SELECT pi.song FROM PlaylistItem pi WHERE pi.playlist = :playlist")
    List<Song> findSongsByPlaylist(@Param("playlist") Playlist playlist);

    Optional<PlaylistItem> findByPlaylistAndSong(Playlist playlist, Song song);
}

