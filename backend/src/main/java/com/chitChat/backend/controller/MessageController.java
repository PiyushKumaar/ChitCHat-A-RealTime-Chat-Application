package com.chitChat.backend.controller;

import com.chitChat.backend.dto.request.SendMessageRequest;
import com.chitChat.backend.dto.response.MessageListResponse;
import com.chitChat.backend.dto.response.MessageResponse;
import com.chitChat.backend.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<MessageListResponse> getMessages(
            Authentication authentication,
            @PathVariable UUID conversationId,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) String cursor) {

        String username = authentication.getName();

        return ResponseEntity.ok(
                messageService.getMessages(
                        username,
                        conversationId,
                        limit,
                        cursor
                )
        );
    }

    @PostMapping("/{conversationId}/messages")
    public ResponseEntity<MessageResponse> sendMessage(
            Authentication authentication,
            @PathVariable UUID conversationId,
            @Valid @RequestBody SendMessageRequest request) {

        String username = authentication.getName();

        return ResponseEntity.ok(
                messageService.sendMessage(
                        username,
                        conversationId,
                        request
                )
        );
    }
}
