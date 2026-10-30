package com.example.LearningPlatformApp.repository;

import com.example.LearningPlatformApp.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    Page<Notification> findByUserIdOrderByCreatedAtDesc(
            Long userId,
            Pageable pageable
    );

    long countByUserIdAndReadFalse(Long userId);

    Optional<Notification> findByIdAndUserId(
            Long notificationId,
            Long userId
    );

    @Modifying
    @Query("""
        UPDATE Notification n
        SET n.read = true,
            n.readAt = :readAt,
            n.updatedAt = :updatedAt
        WHERE n.user.id = :userId
          AND n.read = false
        """)
    int markAllAsReadByUserId(
            @Param("userId") Long userId,
            @Param("readAt") LocalDateTime readAt,
            @Param("updatedAt") LocalDateTime updatedAt
    );

    long deleteByIdAndUserId(Long notificationId, Long userId);
}