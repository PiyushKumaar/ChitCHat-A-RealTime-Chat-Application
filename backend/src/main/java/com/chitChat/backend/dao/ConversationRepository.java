package com.chitChat.backend.dao;

import com.chitChat.backend.entity.conversation.Conversation;
import com.chitChat.backend.entity.conversation.ConversationMember;
import com.chitChat.backend.enums.ConversationType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    @Query("""
            SELECT c
            FROM Conversation c
            WHERE c.type = :type
            AND EXISTS (
                SELECT cm1
                FROM ConversationMember cm1
                WHERE cm1.conversation = c
                AND cm1.user.id = :user1
            )
            AND EXISTS (
                SELECT cm2
                FROM ConversationMember cm2
                WHERE cm2.conversation = c
                AND cm2.user.id = :user2
            )
            """)
    Optional<Conversation> findDirectConversation(
            @Param("type") ConversationType type,
            @Param("user1") UUID user1,
            @Param("user2") UUID user2
    );


}
