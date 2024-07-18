package org.anhnt24.melodyopus.service;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.anhnt24.melodyopus.dto.SongDTO;
import org.anhnt24.melodyopus.entity.Song;
import org.anhnt24.melodyopus.entity.User;
import org.anhnt24.melodyopus.repository.SongRepository;
import org.anhnt24.melodyopus.utils.FileUtil;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SongService {
    @Autowired
    private SongRepository songRepository;

    @Autowired
    private FileUtil fileUtil;

    public Page<SongDTO> getAllSongs(PageRequest req) throws ServiceException {
        try {
            Page<Song> songs = songRepository.findAllByStatusTrue(req);
            return songs.map((song -> mapToDTO(song)));
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public SongDTO mapToDTO(Song song) {
        return new SongDTO(
                song.getId(),
                song.getTitle(),
                song.getGenre(),
                song.getLyric(),
                song.getDuration(),
                song.getReleaseDate(),
                song.getFilePath(),
                song.getThumbnail(),
                song.getListened(),
                song.getUser().getId(),
                song.getUser().getName()
        );
    }

    private Long getMp3Duration(File mp3File) throws IOException, UnsupportedTagException, InvalidDataException {
        Mp3File mp3 = new Mp3File(mp3File);
        return mp3.getLengthInSeconds();
    }

    public void addNewSong(User user,
                           String title,
                           String genre,
                           String lyric,
                           MultipartFile mp3File,
                           MultipartFile thumbnail) throws ServiceException {
        fileUtil.validateFile(mp3File, "audio/mpeg");
        fileUtil.validateFile(thumbnail, "image");

        try {
            String mp3FilePath = fileUtil.saveMp3File(mp3File);
            String thumnailFilePath = fileUtil.saveImageFile(thumbnail);
            Long duration = getMp3Duration(new File(mp3FilePath));

            Song song = new Song.Builder()
                    .title(title)
                    .genre(genre)
                    .lyric(lyric)
                    .filePath(mp3FilePath)
                    .thumbnail(thumnailFilePath)
                    .duration(duration)
                    .user(user)
                    .build();

            songRepository.save(song);
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            e.printStackTrace();
            throw new ServiceException("Failed to save files", e);
        }
    }

    public Song getASongById(Long songId) {
        try {
            Song song = songRepository.findByIdAndStatusTrue(songId).orElse(null);
            if (song == null) {
                throw new RuntimeException("Song ID not found");
            }
            return song;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void updateASong(User user,
                            Long songId,
                            String title,
                            String genre,
                            String lyric,
                            MultipartFile mp3File,
                            MultipartFile thumbnail) throws Exception {
        Song song = getASongById(songId);
        if (user.getId() != song.getUser().getId()) {
            throw new RuntimeException("This song doesn't belong to user");
        }

        fileUtil.validateFile(mp3File, "audio/mpeg");
        fileUtil.validateFile(thumbnail, "image");

        try {
            String mp3FilePath = fileUtil.saveMp3File(mp3File);
            String thumnailFilePath = fileUtil.saveImageFile(thumbnail);
            Long duration = getMp3Duration(new File(mp3FilePath));

            song.setTitle(title);
            song.setGenre(genre);
            song.setLyric(lyric);
            song.setFilePath(mp3FilePath);
            song.setThumbnail(thumnailFilePath);
            song.setDuration(duration);

            songRepository.save(song);
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public void deleteASong(User user, Long songId) {
        Song song = getASongById(songId);
        if (user.getId() != song.getUser().getId()) {
            throw new RuntimeException("This song doesn't belong to user");
        }

        try {
            song.setStatus(false);
            songRepository.save(song);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }

    }

    public List<SongDTO> searchSongs(String query) {
        try {
            return songRepository.searchByTitleOrLyric(query).stream()
                    .map(song -> mapToDTO(song))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
