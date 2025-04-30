package com.example.messagingapp.exception;

public record ErrorResponse(
        Integer status,
        String message
) {
}
