package com.chitChat.backend.dto.response;

import com.chitChat.backend.enums.ConversationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationSummaryResponse {

    private UUID conversationId;
    private ConversationType type;
    private String name;
    private String profileImage;
}
