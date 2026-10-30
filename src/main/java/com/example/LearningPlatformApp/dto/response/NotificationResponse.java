package com.example.LearningPlatformApp.dto.response;

import com.example.LearningPlatformApp.entity.enums.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String title,
        String message,
        NotificationType type,
        boolean read,
        LocalDateTime readAt,
        LocalDateTime createdAt
) {
}