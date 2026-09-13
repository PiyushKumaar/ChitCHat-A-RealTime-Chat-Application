package com.chitChat.backend.dao;

import com.chitChat.backend.entity.conversation.ConversationMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationMemberRepository extends JpaRepository<ConversationMember, UUID> {

    @Query("""
        SELECT cm
        FROM ConversationMember cm
        JOIN FETCH cm.conversation c
        JOIN FETCH cm.user u
        WHERE cm.user.id = :userId
        ORDER BY c.updatedAt DESC, c.id DESC
        """)
    List<ConversationMember> findUserConversations(
            @Param("userId") UUID userId,
            Pageable pageable
    );

    @Query("""
    SELECT cm
    FROM ConversationMember cm
    JOIN FETCH cm.user u
    JOIN FETCH cm.conversation c
    WHERE c.id IN :conversationIds
      AND cm.user.id <> :userId
      AND c.type = com.chitChat.backend.enums.ConversationType.DIRECT
    """)
    List<ConversationMember> findOtherDirectMembers(
            @Param("conversationIds") List<UUID> conversationIds,
            @Param("userId") UUID userId
    );

    @Query("""
    SELECT cm
    FROM ConversationMember cm
    JOIN FETCH cm.conversation c
    JOIN FETCH cm.user u
    WHERE cm.user.id = :userId
      AND (
            c.updatedAt < :cursorUpdatedAt
            OR (
                c.updatedAt = :cursorUpdatedAt
                AND c.id < :cursorConversationId
            )
          )
    ORDER BY c.updatedAt DESC, c.id DESC
    """)
    List<ConversationMember> findUserConversationsAfterCursor(
            @Param("userId") UUID userId,
            @Param("cursorUpdatedAt") Instant cursorUpdatedAt,
            @Param("cursorConversationId") UUID cursorConversationId,
            Pageable pageable
    );

    Optional<ConversationMember> findByConversationIdAndUserId(
            UUID conversationId,
            UUID userId
    );
}
