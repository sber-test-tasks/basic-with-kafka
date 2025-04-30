package com.example.messagingapp.service;

import com.example.messagingapp.dto.MessageRequest;
import com.example.messagingapp.entity.Message;
import com.example.messagingapp.entity.MessageStatus;
import com.example.messagingapp.repository.MessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class StatisticsControllerIntegrationTest {

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
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        messageRepository.deleteAll();
    }

    @Test
    void testGetStatistics() throws Exception {
        MessageRequest request1 = new MessageRequest(
                UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
                "Тестовое сообщение 1",
                Instant.parse("2023-01-01T12:00:00Z")
        );

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request1))
                .when()
                .post("/api/messages")
                .then()
                .statusCode(201);

        Message message2 = new Message(
                UUID.fromString("123e4567-e89b-12d3-a456-426614174002"),
                "Тестовое сообщение 2",
                Instant.parse("2023-01-01T12:01:00Z"),
                MessageStatus.PROCESSING
        );

        message2.setProcessedAt(Instant.parse("2023-01-01T12:01:05Z"));

        messageRepository.save(message2);

        Message message3 = new Message(
                UUID.fromString("123e4567-e89b-12d3-a456-426614174003"),
                "Тестовое сообщение 3",
                Instant.parse("2023-01-01T12:02:00Z"),
                MessageStatus.PROCESSED
        );

        message3.setProcessedAt(Instant.parse("2023-01-01T12:02:10Z"));

        messageRepository.save(message3);

        given()
                .when()
                .get("/api/statistics")
                .then()
                .statusCode(200)
                .body("totalMessages", equalTo(3))
                .body("messagesByStatus.RECEIVED", equalTo(1))
                .body("messagesByStatus.PROCESSING", equalTo(1))
                .body("messagesByStatus.PROCESSED", equalTo(1));

    }
}

