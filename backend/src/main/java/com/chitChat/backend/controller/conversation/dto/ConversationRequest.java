package com.chitChat.backend.controller.conversation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ConversationRequest {

    @NotNull
    private UUID userId;
}
