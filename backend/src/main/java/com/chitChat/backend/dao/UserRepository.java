package com.chitChat.backend.dao;

import com.chitChat.backend.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User , UUID> {

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("""
    SELECT u FROM User u
    WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))
       OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%'))
       OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    Page<User> searchUsers(
            @Param("query") String query,
            Pageable pageable
    );
}
