package org.anhnt24.melodyopus.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uploads")
public class FileRequest {
    @Value("${upload.directory.mp3}")
    private String mp3UploadDir;

    @Value("${upload.directory.image}")
    private String imageUploadDir;

//    @GetMapping("/mp3/")
//    public ResponseEntity<?> getMp3() {
//
//    }
//
//    @GetMapping("/image/")
//    public ResponseEntity<?> getImage() {
//
//    }
}
