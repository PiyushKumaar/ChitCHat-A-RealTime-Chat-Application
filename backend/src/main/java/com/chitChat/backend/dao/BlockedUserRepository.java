package com.chitChat.backend.dao;

import com.chitChat.backend.entity.user.BlockedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BlockedUserRepository extends JpaRepository<BlockedUser , UUID> {
}
