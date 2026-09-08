package com.chitChat.backend.controller.files;

import com.chitChat.backend.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    @Autowired
    private FileService fileService;

    @PostMapping("/profilePic")
    public ResponseEntity<String> uploadProfilePicture(
            Authentication authentication,
            @RequestParam("file")MultipartFile file){
        String username = authentication.getName();
        return ResponseEntity.ok(fileService.saveProfilePic(username,file));
    }

    @GetMapping("/profilePic/{uuid}")
    public ResponseEntity<Resource> loadProfilePic(
            @PathVariable String uuid) throws IOException {

        Resource resource = fileService.loadProfilePicture(uuid);

        String contentType = Files.probeContentType(
                Paths.get(resource.getURI())
        );

        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}
