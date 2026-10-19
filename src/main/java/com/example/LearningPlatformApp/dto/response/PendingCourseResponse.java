package com.example.LearningPlatformApp.dto.response;

import com.example.LearningPlatformApp.entity.enums.CourseStatus;

import java.time.LocalDateTime;

public record PendingCourseResponse(
        Long id,
        String title,
        CourseStatus status,
        Long instructorId,
        String instructorName,
        LocalDateTime createdAt
) {}