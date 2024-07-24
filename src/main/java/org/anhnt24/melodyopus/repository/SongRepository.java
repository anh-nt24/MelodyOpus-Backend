package org.anhnt24.melodyopus.repository;

import org.anhnt24.melodyopus.dto.SongDTO;
import org.anhnt24.melodyopus.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    Optional<Song> findByIdAndStatusTrue(Long songId);

    Page<Song> findAllByStatusTrue(Pageable pageable);

    // select * from Song where
    // lower(title) like lower(concat('%', ?, '%'))
    // or lower (lyric) like lower(concat('%', ?, '%' ))
    @Query("SELECT s FROM Song s WHERE " +
            "LOWER(s.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(s.lyric) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Song> searchByTitleOrLyric(@Param("query") String query);

    Optional<Song> findByFilePath(String filePath);
}
