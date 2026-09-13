package com.chitChat.backend.serviceImpl;

import com.chitChat.backend.dao.ConversationMemberRepository;
import com.chitChat.backend.dao.ConversationRepository;
import com.chitChat.backend.dao.MessageRepository;
import com.chitChat.backend.dao.UserRepository;
import com.chitChat.backend.dto.records.MessageCursor;
import com.chitChat.backend.dto.request.SendMessageRequest;
import com.chitChat.backend.dto.response.MessageListResponse;
import com.chitChat.backend.dto.response.MessageResponse;
import com.chitChat.backend.entity.conversation.Conversation;
import com.chitChat.backend.entity.conversation.ConversationMember;
import com.chitChat.backend.entity.message.Message;
import com.chitChat.backend.entity.user.User;
import com.chitChat.backend.enums.MessageType;
import com.chitChat.backend.exceptions.ResourceNotFoundException;
import com.chitChat.backend.service.MessageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ConversationMemberRepository conversationMemberRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public MessageListResponse getMessages(String username, UUID conversationId, int limit, String cursor) {

        if (limit < 1 || limit > 50) {
            throw new IllegalArgumentException(
                    "Limit must be between 1 and 50"
            );
        }

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with username " + username
                        ));

        conversationRepository
                .findById(conversationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Conversation not found with id: " + conversationId
                        ));

        conversationMemberRepository
                        .findByConversationIdAndUserId(
                                conversationId,
                                currentUser.getId()
                        ).orElseThrow(() ->
                                new AccessDeniedException(
                                        "You are not a member of this conversation"
                                ));

        Pageable pageable = PageRequest.of(0, limit + 1);

        List<Message> messages;

        if (cursor == null || cursor.isBlank()) {

            messages = messageRepository.findConversationMessages(
                    conversationId,
                    pageable
            );

        } else {

            MessageCursor decodedCursor = decodeMessageCursor(cursor);

            messages = messageRepository.findConversationMessagesAfterCursor(
                    conversationId,
                    decodedCursor.createdAt(),
                    decodedCursor.messageId(),
                    pageable
            );
        }

        boolean hasMore = messages.size() > limit;

        if (hasMore) {
            messages = messages.subList(0, limit);
        }

        List<MessageResponse> messageResponses =
                messages.stream()
                        .map(message -> {

                            User sender = message.getSender();

                            UUID replyToMessageId =
                                    message.getReplyToMessage() != null
                                            ? message.getReplyToMessage().getId()
                                            : null;

                            return new MessageResponse(
                                    message.getId(),
                                    conversationId,
                                    sender.getId(),
                                    sender.getFullName(),
                                    sender.getProfileImage(),
                                    message.getContent(),
                                    message.getMessageType(),
                                    replyToMessageId,
                                    message.isEdited(),
                                    message.isDeleted(),
                                    message.getCreatedAt(),
                                    message.getUpdatedAt()
                            );
                        })
                        .toList();

        String nextCursor = null;

        if (hasMore && !messages.isEmpty()) {

            Message lastMessage =
                    messages.get(messages.size() - 1);

            nextCursor = encodeMessageCursor(
                    lastMessage.getCreatedAt(),
                    lastMessage.getId()
            );
        }

        return new MessageListResponse(
                messageResponses,
                nextCursor,
                hasMore
        );
    }

    @Transactional
    @Override
    public MessageResponse sendMessage(String username, UUID conversationId, SendMessageRequest request) {
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with username " + username
                        ));

        Conversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Conversation not found with id: " + conversationId
                        ));

        conversationMemberRepository
                .findByConversationIdAndUserId(
                        conversationId,
                        currentUser.getId()
                )
                .orElseThrow(() ->
                        new AccessDeniedException(
                                "You are not a member of this conversation"
                        ));

        Message message = new Message();

        message.setConversation(conversation);
        message.setSender(currentUser);
        message.setContent(request.getContent());
        message.setMessageType(MessageType.TEXT);
        message.setEdited(false);
        message.setDeleted(false);

        Message savedMessage = messageRepository.save(message);
        conversation.setUpdatedAt(Instant.now());
        conversationRepository.save(conversation);

        return new MessageResponse(
                savedMessage.getId(),
                conversationId,
                currentUser.getId(),
                currentUser.getFullName(),
                currentUser.getProfileImage(),
                savedMessage.getContent(),
                savedMessage.getMessageType(),
                null,
                savedMessage.isEdited(),
                savedMessage.isDeleted(),
                savedMessage.getCreatedAt(),
                savedMessage.getUpdatedAt()
        );
    }

    private String encodeMessageCursor(
            Instant createdAt,
            UUID messageId) {

        String value = createdAt.toString() + "|" + messageId;

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(
                        value.getBytes(StandardCharsets.UTF_8)
                );
    }

    private MessageCursor decodeMessageCursor(String cursor) {

        try {
            String decoded = new String(
                    Base64.getUrlDecoder().decode(cursor),
                    StandardCharsets.UTF_8
            );

            String[] parts = decoded.split("\\|");

            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid cursor");
            }

            Instant createdAt = Instant.parse(parts[0]);
            UUID messageId = UUID.fromString(parts[1]);

            return new MessageCursor(
                    createdAt,
                    messageId
            );

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid cursor");
        }
    }
}
