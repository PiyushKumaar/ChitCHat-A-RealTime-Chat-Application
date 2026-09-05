package com.chitChat.backend.dao;

import com.chitChat.backend.entity.message.MessageAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageAttachmentRepository extends JpaRepository<MessageAttachment , UUID> {
}
