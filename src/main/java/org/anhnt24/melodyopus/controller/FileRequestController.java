package org.anhnt24.melodyopus.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/uploads")
public class FileRequestController {
    @Value("${upload.directory}")
    private String uploadDir;


    @GetMapping("/mp3/{filename:.+}")
    public ResponseEntity<?> getMp3(@PathVariable String filename) {
        return handleFetchFile(filename, "mp3");
    }

    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<?> getImage(@PathVariable String filename) {
        return handleFetchFile(filename, "image");
    }

    private ResponseEntity<?> handleFetchFile(String filename, String type) {
        try {
            Path file = Paths.get(uploadDir, type, filename).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            if (!resource.isReadable() || !resource.isFile()) {
                return ResponseEntity.badRequest().body("File error");
            }

            String contentType = "application/octet-stream";
            if (type.equals("image")) {
                if (filename.endsWith(".png")) {
                    contentType = "image/png";
                } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
                    contentType = "image/jpeg";
                }
            } else if (type.equals("mp3") && filename.endsWith(".mp3")) {
                contentType = "audio/mpeg";
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
