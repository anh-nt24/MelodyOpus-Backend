package org.anhnt24.melodyopus.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class FileUtil {

    @Value("${upload.directory.mp3}")
    private String mp3Directory;

    @Value("${upload.directory.image}")
    private String imageDirectory;

    private String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return now.format(formatter);
    }

    public String saveMp3File(MultipartFile file) throws IOException {
        return saveFile(file, mp3Directory);
    }

    public String saveImageFile(MultipartFile file) throws IOException {
        return saveFile(file, imageDirectory);
    }

    public String saveFile(MultipartFile file, String directory) throws IOException {
        // Ensure the directory exists
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filename = UUID.randomUUID() + "-" + getCurrentTime();
        String filePath = Paths.get(directory, filename).toString();

        OutputStream os = new FileOutputStream(filePath);
        os.write(file.getBytes());
        return filePath;
    }

    public Resource getFileAsResource(String filePath) {
        return new FileSystemResource(filePath);
    }

    public void validateFile(MultipartFile file, String expectedType) throws RuntimeException {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }
        if (!file.getContentType().contains(expectedType)) {
            throw new RuntimeException("Invalid file type");
        }
    }
}

