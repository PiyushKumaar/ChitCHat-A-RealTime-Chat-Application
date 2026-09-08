package com.chitChat.backend.serviceImpl;

import com.chitChat.backend.dao.UserRepository;
import com.chitChat.backend.entity.user.User;
import com.chitChat.backend.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String saveProfilePic(String username, MultipartFile file) {

        try {

            if (file.isEmpty()) {
                throw new RuntimeException(
                        "Cannot upload an empty file"
                );}

                Path profilePicDir = Paths.get(uploadDir, "profilePicture");

                Files.createDirectories(profilePicDir);

                String originalFilename = file.getOriginalFilename();

                String extension = "";

                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(
                            originalFilename.lastIndexOf(".")
                    );
                }

                String uuid = UUID.randomUUID().toString();

                String filename = uuid + extension;

                Path filePath = profilePicDir.resolve(filename);

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                user.setProfileImage(uuid);

                userRepository.save(user);

                return uuid;

            }catch(IOException e){
                throw new RuntimeException("Could not save profile picture", e);
            }
        }

    @Override
    public Resource loadProfilePicture(String uuid) {
        try {
            Path profilePictureDir =
                    Paths.get(uploadDir, "profilePicture");

            try (DirectoryStream<Path> stream =
                         Files.newDirectoryStream(
                                 profilePictureDir,
                                 uuid + ".*")) {

                for (Path path : stream) {

                    Resource resource =
                            new UrlResource(path.toUri());

                    if (resource.exists() && resource.isReadable()) {
                        return resource;
                    }
                }
            }

            throw new RuntimeException("Profile picture not found");

        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not load profile picture", e
            );
        }
    }

}



