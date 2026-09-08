package com.chitChat.backend.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String saveProfilePic(String username,MultipartFile file);

    Resource loadProfilePicture(String uuid);
}
