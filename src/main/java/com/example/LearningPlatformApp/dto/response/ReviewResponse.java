package com.example.LearningPlatformApp.dto.response;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Long studentId,
        String studentName,
        Long courseId,
        String courseTitle,
        Integer rating,
        String comment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}