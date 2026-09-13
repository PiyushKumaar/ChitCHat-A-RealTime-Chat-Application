package com.chitChat.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ConversationRequest {

    @NotNull
    private UUID userId;
}
