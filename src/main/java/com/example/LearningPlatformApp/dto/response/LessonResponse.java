package com.example.LearningPlatformApp.dto.response;

import java.time.LocalDateTime;

public record LessonResponse(
        Long id,
        String title,
        String description,
        String videoUrl,
        Integer duration,
        Integer lessonOrder,
        Long courseId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}