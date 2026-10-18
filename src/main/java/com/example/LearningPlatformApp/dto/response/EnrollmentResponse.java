package com.example.LearningPlatformApp.dto.response;

import com.example.LearningPlatformApp.entity.enums.EnrollmentStatus;

import java.time.LocalDateTime;

public record EnrollmentResponse(
        Long id,
        Long studentId,
        String studentName,
        Long courseId,
        String courseTitle,
        LocalDateTime enrolledAt,
        LocalDateTime completedAt,
        EnrollmentStatus status
) {}