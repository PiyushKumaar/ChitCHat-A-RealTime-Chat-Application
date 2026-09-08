package com.chitChat.backend.controller.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BioRequest {

    @Size(max = 250, message = "Bio cannot exceed 250 characters")
    private String bio;
}
