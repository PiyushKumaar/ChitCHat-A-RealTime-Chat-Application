package com.chitChat.backend.dao;

import com.chitChat.backend.entity.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
}
