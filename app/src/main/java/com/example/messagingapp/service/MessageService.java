package com.example.messagingapp.service;

import com.example.messagingapp.dto.MessageRequest;
import com.example.messagingapp.dto.MessageResponse;
import com.example.messagingapp.entity.Message;
import com.example.messagingapp.exception.MessageNotFoundException;
import com.example.messagingapp.exception.MessageProcessingException;
import com.example.messagingapp.map.MessageMapper;
import com.example.messagingapp.repository.MessageRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class MessageService {

    private static final Logger logger = Logger.getLogger(MessageService.class.getName());

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public MessageService(MessageRepository messageRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    @Transactional
    public MessageResponse create(MessageRequest request) {
        try {
            Message message = messageMapper.toEntity(request);
            message = messageRepository.save(message);

            logger.info(String.format("Message created with id %s", message.getId().toString()));

            return messageMapper.toResponse(message);

        } catch (DataIntegrityViolationException ex) {
            logger.severe(String.format("Data integrity violation while creating message: %s", ex.getMessage()));
            throw new MessageProcessingException("Message creation failed due to data constraints", ex);
        }
    }

    public List<MessageResponse> getAll() {
        return messageRepository.findAll().stream()
                .map(messageMapper::toResponse)
                .toList();
    }

    public MessageResponse getMessageById(UUID id) {
        return messageRepository.findById(id)
                .map(messageMapper::toResponse)
                .orElseThrow(() -> new MessageNotFoundException(id));
    }
}