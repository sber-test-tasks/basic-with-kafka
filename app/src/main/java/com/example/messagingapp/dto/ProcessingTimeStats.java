package com.example.messagingapp.dto;

import java.time.Duration;

public record ProcessingTimeStats(
        Duration minDuration,
        Duration maxDuration,
        Duration avgDuration
) {}