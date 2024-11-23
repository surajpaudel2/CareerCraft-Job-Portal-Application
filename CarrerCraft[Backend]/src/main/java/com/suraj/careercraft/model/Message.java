package com.suraj.careercraft.model;

import lombok.*;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationship with User (Many-to-One)
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender; // The user who sent the message

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver; // The user who received the message

    @Column(name = "message_content")
    private String content; // The content of the message

    @Column(name = "sent_at")
    private Timestamp sentAt;

    @Column(name = "is_read")
    private boolean isRead = false; // To mark if the message has been read
}
