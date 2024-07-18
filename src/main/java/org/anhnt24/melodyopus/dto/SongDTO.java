package org.anhnt24.melodyopus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongDTO {
    private Long id;
    private String title;
    private String genre;
    private String lyric;
    private Long duration;
    private Date releaseDate;
    private String filePath;
    private String thumbnail;
    private Long listened;
    private Long userId;
    private String userName;
}
