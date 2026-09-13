package com.chitChat.backend.dao;

import com.chitChat.backend.entity.message.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query("""
        SELECT m
        FROM Message m
        JOIN FETCH m.sender s
        WHERE m.conversation.id = :conversationId
        ORDER BY m.createdAt DESC, m.id DESC
        """)
    List<Message> findConversationMessages(
            @Param("conversationId") UUID conversationId,
            Pageable pageable
    );

    @Query("""
        SELECT m
        FROM Message m
        JOIN FETCH m.sender s
        WHERE m.conversation.id = :conversationId
          AND (
                m.createdAt < :cursorCreatedAt
                OR (
                    m.createdAt = :cursorCreatedAt
                    AND m.id < :cursorMessageId
                )
              )
        ORDER BY m.createdAt DESC, m.id DESC
        """)
    List<Message> findConversationMessagesAfterCursor(
            @Param("conversationId") UUID conversationId,
            @Param("cursorCreatedAt") Instant cursorCreatedAt,
            @Param("cursorMessageId") UUID cursorMessageId,
            Pageable pageable
    );
}
