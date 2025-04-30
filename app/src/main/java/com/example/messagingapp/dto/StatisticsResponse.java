package com.example.messagingapp.dto;

import com.example.messagingapp.entity.MessageStatus;

import java.util.Map;

public record StatisticsResponse(
        Long totalMessages,
        Map<MessageStatus, Long> messagesByStatus,
        ProcessingTimeStats receivedStats,
        ProcessingTimeStats processingStats,
        ProcessingTimeStats processedStats
) {
}