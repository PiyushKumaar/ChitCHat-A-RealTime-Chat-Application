package com.chitChat.backend.serviceImpl;

import com.chitChat.backend.controller.conversation.dto.*;
import com.chitChat.backend.dao.ConversationMemberRepository;
import com.chitChat.backend.dao.ConversationRepository;
import com.chitChat.backend.dao.UserRepository;
import com.chitChat.backend.entity.conversation.Conversation;
import com.chitChat.backend.entity.conversation.ConversationMember;
import com.chitChat.backend.entity.user.User;
import com.chitChat.backend.enums.ConversationMemberRole;
import com.chitChat.backend.enums.ConversationType;
import com.chitChat.backend.exceptions.ResourceNotFoundException;
import com.chitChat.backend.service.ConversationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ConversationMemberRepository conversationMemberRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public ConversationResponse createDirectConversation(String username, ConversationRequest request) {

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username " + username));

        User targetUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id : " + request.getUserId()));

        if (currentUser.getId().equals(targetUser.getId())) {
            throw new IllegalArgumentException(
                    "You cannot create a direct conversation with yourself"
            );
        }

        return conversationRepository.findDirectConversation(
                        ConversationType.DIRECT,
                        currentUser.getId(),
                        targetUser.getId()
                ).map(existingConversation ->
                        new ConversationResponse(
                                existingConversation.getId(),
                                "Conversation already exists"
                        ))
                .orElseGet(() -> {

                    //Create conversation
                    Conversation conversation = new Conversation();

                    conversation.setType(ConversationType.DIRECT);
                    conversation.setCreatedBy(currentUser);

                    Conversation savedConversation = conversationRepository.save(conversation);

                    // add current user as admin
                    ConversationMember currentMember = new ConversationMember();

                    currentMember.setConversation(savedConversation);
                    currentMember.setUser(currentUser);
                    currentMember.setRole(ConversationMemberRole.ADMIN);

                    // add target user as member
                    ConversationMember targetMember = new ConversationMember();

                    targetMember.setConversation(savedConversation);
                    targetMember.setUser(targetUser);
                    targetMember.setRole(ConversationMemberRole.MEMBER);

                    conversationMemberRepository.save(currentMember);
                    conversationMemberRepository.save(targetMember);

                    return new ConversationResponse(savedConversation.getId(), "Conversation has established");

                });

    }

    @Override
    public ConversationListResponse getMyConversations(String username, int limit, String cursor) {

        if (limit < 1 || limit > 50) {
            throw new IllegalArgumentException(
                    "Limit must be between 1 and 50"
            );
        }

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found by username " + username));

        Pageable pageable = PageRequest.of(0, limit + 1);

        List<ConversationMember> conversationMembers;

        if (cursor == null || cursor.isBlank()) {

            // First request
            conversationMembers =
                    conversationMemberRepository.findUserConversations(
                            currentUser.getId(),
                            pageable
                    );

        } else {

            // Next request
            ConversationCursor decodedCursor =
                    decodeCursor(cursor);

            conversationMembers =
                    conversationMemberRepository
                            .findUserConversationsAfterCursor(
                                    currentUser.getId(),
                                    decodedCursor.updatedAt(),
                                    decodedCursor.conversationId(),
                                    pageable
                            );
        }



        // Did we receive more than the requested limit?
        boolean hasMore = conversationMembers.size() > limit;

        // Only return the requested number of conversations
        if (hasMore) {
            conversationMembers =
                    conversationMembers.subList(0, limit);
        }

        // Get conversation IDs
        List<UUID> conversationIds =
                conversationMembers.stream()
                        .map(member ->
                                member.getConversation().getId())
                        .toList();

        // Query 2: Get all other users for DIRECT conversations
        List<ConversationMember> otherDirectMembers =
                conversationIds.isEmpty()
                        ? List.of()
                        : conversationMemberRepository.findOtherDirectMembers(
                        conversationIds,
                        currentUser.getId()
                );

        // Map conversationId -> other user
        Map<UUID, User> otherUserMap = otherDirectMembers.stream()
                .collect(Collectors.toMap(
                        member -> member.getConversation().getId(),
                        ConversationMember::getUser
                ));

        // Build response
        List<ConversationSummaryResponse> conversations = conversationMembers.stream()
                .map(member -> {
                    Conversation conversation = member.getConversation();

                    String name;
                    String profileImage;

                    if (conversation.getType() == ConversationType.DIRECT) {

                        User otherUser = otherUserMap.get(conversation.getId());

                        name = otherUser != null
                                ? otherUser.getFullName()
                                : null;

                        profileImage = otherUser != null
                                ? otherUser.getProfileImage()
                                : null;
                    } else {
                        name = conversation.getName();
                        profileImage = conversation.getGroupIcon();
                    }

                    return new ConversationSummaryResponse(
                            conversation.getId(),
                            conversation.getType(),
                            name,
                            profileImage
                    );
                }).toList();

        // Generate cursor from the LAST returned conversation
        String nextCursor = null;

        if (hasMore && !conversationMembers.isEmpty()) {

            Conversation lastConversation =
                    conversationMembers
                            .get(conversationMembers.size() - 1)
                            .getConversation();

            nextCursor = encodeCursor(
                    lastConversation.getUpdatedAt(),
                    lastConversation.getId()
            );
        }

        return new ConversationListResponse(
                conversations,
                nextCursor,
                hasMore
        );
    }

    private String encodeCursor(Instant updatedAt, UUID conversationId) {

        String value = updatedAt.toString() + "|" + conversationId;

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(
                        value.getBytes(StandardCharsets.UTF_8)
                );
    }

    private ConversationCursor decodeCursor(String cursor) {

        try {
            String decoded = new String(
                    Base64.getUrlDecoder().decode(cursor),
                    StandardCharsets.UTF_8
            );

            String[] parts = decoded.split("\\|");

            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid cursor");
            }

            Instant updatedAt = Instant.parse(parts[0]);
            UUID conversationId = UUID.fromString(parts[1]);

            return new ConversationCursor(
                    updatedAt,
                    conversationId
            );

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid cursor");
        }
    }
}
