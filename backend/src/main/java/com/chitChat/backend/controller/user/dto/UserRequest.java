package com.chitChat.backend.controller.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserRequest {

    private String firstName;

    private String lastName;

    private String profileImage;

    private String bio;

}
