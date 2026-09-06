package com.chitChat.backend.entity.notification;

import com.chitChat.backend.entity.message.Message;
import com.chitChat.backend.entity.user.User;
import com.chitChat.backend.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "receiver_id" ,nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "type" , nullable = false)
    private NotificationType type;

    @Column(name = "title",nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @JoinColumn(name = "reference_id")
    private UUID referenceId;

    @Column(nullable = false)
    private Boolean read = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;







}
