package com.chitChat.backend.dao;

import com.chitChat.backend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User , UUID> {
}
