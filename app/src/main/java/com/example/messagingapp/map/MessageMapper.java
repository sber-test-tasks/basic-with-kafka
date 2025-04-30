package com.example.messagingapp.map;

import com.example.messagingapp.dto.MessageRequest;
import com.example.messagingapp.dto.MessageResponse;
import com.example.messagingapp.entity.Message;
import com.example.messagingapp.entity.MessageStatus;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public Message toEntity(MessageRequest messageRequest) {
        Message message = new Message();
        message.setId(messageRequest.id());
        message.setContent(messageRequest.content());
        message.setTimestamp(messageRequest.timestamp());
        message.setStatus(MessageStatus.RECEIVED);
        return message;
    }

    public MessageResponse toResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getStatus(),
                message.getTimestamp()
        );
    }



}
