package com.example.messagingapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public record MessageRequest(
        @NotNull(message = "ID cannot be null") UUID id,
        @NotBlank(message = "Content cannot be blank")
        @Size(min = 1, message = "Content must have at least 1 character") String content,
        @NotNull(message = "Timestamp cannot be null") Instant timestamp
) {}