package com.chitChat.backend.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "blocked_users")
@Getter
@Setter
public class BlockedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "blocker_id",nullable = false)
    private User blocker;

    @ManyToOne
    @JoinColumn(name = "blocked_user_id",nullable = false)
    private User blockedUser;

    @CreationTimestamp
    @Column(nullable = false)
    private Instant createdAt;

}
