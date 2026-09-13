package com.chitChat.backend.controller.conversation;

import com.chitChat.backend.controller.conversation.dto.ConversationListResponse;
import com.chitChat.backend.controller.conversation.dto.ConversationRequest;
import com.chitChat.backend.controller.conversation.dto.ConversationResponse;
import com.chitChat.backend.service.ConversationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    @Autowired
    ConversationService conversationService;

    @PostMapping("/direct")
    public ResponseEntity<ConversationResponse> createDirectConversation(
            Authentication authentication,
            @Valid @RequestBody ConversationRequest request){
        String username = authentication.getName();

        ConversationResponse conversation = conversationService.createDirectConversation(username,request);

        return ResponseEntity.ok(conversation);
    }

    @GetMapping()
    public ResponseEntity<ConversationListResponse> getMyConversation(
            Authentication authentication,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String cursor){
        String username = authentication.getName();

        return ResponseEntity.ok(
                conversationService.getMyConversations(username,limit,cursor)
        );
    }
}
