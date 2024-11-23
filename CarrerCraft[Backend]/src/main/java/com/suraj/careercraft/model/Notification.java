package com.suraj.careercraft.model;

import lombok.*;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message")
    private String message; // The notification message

    @Column(name = "is_read")
    private boolean isRead = false; // To mark if the notification has been read

    // Relationship with User (Many-to-One)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // The user receiving the notification

    @Column(name = "created_at")
    private Timestamp createdAt;
}
