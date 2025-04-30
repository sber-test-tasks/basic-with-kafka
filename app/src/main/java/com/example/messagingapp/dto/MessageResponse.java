package com.example.messagingapp.dto;

import com.example.messagingapp.entity.MessageStatus;

import java.time.Instant;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        MessageStatus status,
        Instant processedAt
) {
}
