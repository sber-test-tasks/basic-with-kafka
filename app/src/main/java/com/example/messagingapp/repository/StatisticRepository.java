package com.example.messagingapp.repository;

import com.example.messagingapp.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface StatisticRepository extends JpaRepository<Message, UUID> {

    @Query(value = """
                SELECT status, COUNT(*) as count 
                FROM messages 
                GROUP BY status
            """, nativeQuery = true)
    List<Object[]> countByStatus();

    @Query(value = """
                SELECT 
                    MIN(EXTRACT(EPOCH FROM (processed_at - timestamp)) * 1000) AS min_duration,
                    MAX(EXTRACT(EPOCH FROM (processed_at - timestamp)) * 1000) AS max_duration,
                    AVG(EXTRACT(EPOCH FROM (processed_at - timestamp)) * 1000) AS avg_duration
                FROM messages
                WHERE status = :status
                AND processed_at IS NOT NULL
            """, nativeQuery = true)
    Map<String, Object> findProcessingTimes(@Param("status") String status);

}