package com.example.messagingapp.controller;

import com.example.messagingapp.dto.StatisticsResponse;
import com.example.messagingapp.service.StatisticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@Tag(name = "Статистика", description = "Операции с статистикой")
public class StatisticsController {
    private final StatisticService statisticService;

    public StatisticsController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @Operation(
            summary = "Получить статистику обработанных сообщений"
    )
    @GetMapping
    public ResponseEntity<StatisticsResponse> getStatistics() {
        return ResponseEntity.ok(statisticService.getStatistics());
    }
}