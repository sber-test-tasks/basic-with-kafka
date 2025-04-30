package com.example.messagingapp.service;

import com.example.messagingapp.dto.ProcessingTimeStats;
import com.example.messagingapp.dto.StatisticsResponse;
import com.example.messagingapp.entity.MessageStatus;
import com.example.messagingapp.repository.MessageRepository;
import com.example.messagingapp.repository.StatisticRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Transactional
public class StatisticService {

    private static final Logger logger = Logger.getLogger(StatisticService.class.getName());

    private final StatisticRepository statisticRepository;
    private final MessageRepository messageRepository;

    public StatisticService(StatisticRepository statisticRepository, MessageRepository messageRepository) {
        this.statisticRepository = statisticRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional(readOnly = true)
    public StatisticsResponse getStatistics() {
        Long totalCount = messageRepository.count();

        Map<MessageStatus, Long> statusCount = convertStatusCount(
                statisticRepository.countByStatus()
        );

        ProcessingTimeStats receivedStats = getProcessingStats(MessageStatus.RECEIVED);
        ProcessingTimeStats processingStats = getProcessingStats(MessageStatus.PROCESSING);
        ProcessingTimeStats processedStats = getProcessingStats(MessageStatus.PROCESSED);

        return new StatisticsResponse(
                totalCount,
                statusCount,
                receivedStats,
                processingStats,
                processedStats
        );
    }

    @Transactional(readOnly = true)
    protected ProcessingTimeStats getProcessingStats(MessageStatus status) {
        Map<String, Object> times = statisticRepository.findProcessingTimes(status.name());

        BigDecimal minDuration = (BigDecimal) times.get("min_duration");
        BigDecimal maxDuration = (BigDecimal) times.get("max_duration");
        BigDecimal avgDuration = (BigDecimal) times.get("avg_duration");

        if (minDuration == null || maxDuration == null || avgDuration == null) {
            logger.severe(String.format("No processing times found for status: %s", status.name()));
            return new ProcessingTimeStats(Duration.ZERO, Duration.ZERO, Duration.ZERO);
        }

        return new ProcessingTimeStats(
                Duration.ofMillis(minDuration.longValue()),
                Duration.ofMillis(maxDuration.longValue()),
                Duration.ofMillis(avgDuration.longValue())
        );
    }

    private Map<MessageStatus, Long> convertStatusCount(List<Object[]> result) {
        return result.stream()
                .collect(Collectors.toMap(
                        arr -> MessageStatus.valueOf((String) arr[0]),
                        arr -> (Long) arr[1]
                ));
    }
}