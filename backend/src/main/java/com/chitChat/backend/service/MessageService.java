package com.chitChat.backend.service;

import com.chitChat.backend.dto.request.SendMessageRequest;
import com.chitChat.backend.dto.response.MessageListResponse;
import com.chitChat.backend.dto.response.MessageResponse;
import jakarta.validation.Valid;

import java.util.UUID;

public interface MessageService {

    MessageListResponse getMessages(String username, UUID conversationId, int limit, String cursor);

    MessageResponse sendMessage(String username, UUID conversationId, SendMessageRequest request);
}
