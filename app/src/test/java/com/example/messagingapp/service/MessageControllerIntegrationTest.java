package com.example.messagingapp.service;

import com.example.messagingapp.dto.MessageRequest;
import com.example.messagingapp.entity.Message;
import com.example.messagingapp.entity.MessageStatus;
import com.example.messagingapp.repository.MessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class MessageControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    public static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("postgresTest")
            .withUsername("postgresTest")
            .withPassword("postgresTest");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        messageRepository.deleteAll();
    }

    @Test
    void createMessageShouldReturnCreatedMessage() throws Exception {
        MessageRequest request = new MessageRequest(
                UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
                "Тестовое сообщение 1",
                Instant.parse("2023-01-01T12:00:00Z")
        );

        given()
                .contentType(String.valueOf(APPLICATION_JSON))
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post("/api/messages")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", equalTo("123e4567-e89b-12d3-a456-426614174001"))
                .body("status", equalTo(MessageStatus.RECEIVED.toString()));
    }

    @Test
    void createMessageShouldReturnBadRequest() throws Exception {
        // Given
        MessageRequest request = new MessageRequest(
                UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
                "",
                Instant.parse("2023-01-01T12:00:00Z")
        );

        given()
                .contentType(String.valueOf(APPLICATION_JSON))
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post("/api/messages")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void getMessageShouldReturnMessage() throws Exception {
        Message savedMessage = messageRepository.save(new Message(
                UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
                "Test content",
                Instant.parse("2023-01-01T12:00:00Z"),
                MessageStatus.PROCESSING
        ));

        given()
                .when()
                .get("/api/messages/" + savedMessage.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(savedMessage.getId().toString()))
                .body("status", equalTo(MessageStatus.PROCESSING.toString()));
    }

    @Test
    void getMessage_WhenNotExists_ShouldReturnNotFound() throws Exception {
        given()
                .when()
                .get("/api/messages/" + UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

}
