package com.chitChat.backend.service;

import com.chitChat.backend.controller.conversation.dto.ConversationListResponse;
import com.chitChat.backend.controller.conversation.dto.ConversationRequest;
import com.chitChat.backend.controller.conversation.dto.ConversationResponse;

import java.util.UUID;

public interface ConversationService {

    ConversationResponse createDirectConversation(
            String username,
            ConversationRequest request
    );

    ConversationListResponse getMyConversations(String username, int limit, String cursor);
}
