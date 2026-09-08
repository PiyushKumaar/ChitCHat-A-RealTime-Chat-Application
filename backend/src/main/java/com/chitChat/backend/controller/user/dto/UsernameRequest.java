package com.chitChat.backend.controller.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsernameRequest {

    @NotBlank(message = "Username is required")
    private String username;
}
