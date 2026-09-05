package com.chitChat.backend.dao;

import com.chitChat.backend.entity.conversation.ConversationMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConversationMemberRepository extends JpaRepository<ConversationMember, UUID> {
}
