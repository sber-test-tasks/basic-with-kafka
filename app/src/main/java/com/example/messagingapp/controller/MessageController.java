package com.example.messagingapp.controller;

import com.example.messagingapp.dto.MessageRequest;
import com.example.messagingapp.dto.MessageResponse;
import com.example.messagingapp.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Сообщения", description = "Операции с сообщениями")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @Operation(
            summary = "Отправить новое сообщение",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Пример сообщения",
                                            summary = "Стандартное сообщение",
                                            value = """
                                                        {
                                                          "id": "23e4567-e89b-12d3-a456-426614174001",
                                                          "content": "Тестовое сообщение 1",
                                                          "timestamp": "2023-01-01T12:00:00Z"
                                                        }
                                                    """
                                    )
                            }
                    )
            )
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse createMessage(
            @Valid @RequestBody MessageRequest messageRequest
    ) {
        MessageResponse response = messageService.create(messageRequest);
        return response;
    }


    @Operation(
            summary = "Получить список всех сообщений"
    )
    @GetMapping
    public List<MessageResponse> getAllMessages() {
        return messageService.getAll();
    }


    @Operation(
            summary = "Получить сообщение по ID"
    )
    @GetMapping("/{id}")
    public MessageResponse getMessageById(@PathVariable UUID id) {
        return messageService.getMessageById(id);
    }
}
