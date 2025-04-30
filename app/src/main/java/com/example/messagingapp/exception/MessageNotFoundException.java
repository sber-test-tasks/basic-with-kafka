package com.example.messagingapp.exception;

import java.util.UUID;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(UUID id) {
        super(String.format("Message with id %s not found", id));
    }
}
