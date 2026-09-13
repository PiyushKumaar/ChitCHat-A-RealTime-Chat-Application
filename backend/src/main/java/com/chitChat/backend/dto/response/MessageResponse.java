package com.chitChat.backend.dto.response;

import com.chitChat.backend.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private UUID messageId;
    private UUID conversationId;

    private UUID senderId;
    private String senderName;
    private String senderProfileImage;

    private String content;
    private MessageType messageType;

    private UUID replyToMessageId;

    private boolean edited;
    private boolean deleted;

    private Instant createdAt;
    private Instant updatedAt;
}
