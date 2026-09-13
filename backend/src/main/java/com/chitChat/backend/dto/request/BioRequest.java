package com.chitChat.backend.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BioRequest {

    @Size(max = 250, message = "Bio cannot exceed 250 characters")
    private String bio;
}
