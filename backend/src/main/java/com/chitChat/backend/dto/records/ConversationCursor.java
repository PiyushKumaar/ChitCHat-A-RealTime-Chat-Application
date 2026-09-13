package com.chitChat.backend.dto.records;

import java.time.Instant;
import java.util.UUID;

public record ConversationCursor(
        Instant updatedAt,
        UUID conversationId
){

}
