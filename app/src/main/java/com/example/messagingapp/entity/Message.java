package com.example.messagingapp.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, updatable = false)
    private Instant timestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageStatus status;

    @UpdateTimestamp
    private Instant processedAt;

    public Message() {
    }

    public Message(UUID id, String content, Instant timestamp, MessageStatus status) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
        this.status = status;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }
}
