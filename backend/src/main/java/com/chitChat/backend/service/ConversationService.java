package com.chitChat.backend.service;

import com.chitChat.backend.dto.response.ConversationListResponse;
import com.chitChat.backend.dto.request.ConversationRequest;
import com.chitChat.backend.dto.response.ConversationResponse;

public interface ConversationService {

    ConversationResponse createDirectConversation(
            String username,
            ConversationRequest request
    );

    ConversationListResponse getMyConversations(String username, int limit, String cursor);
}
