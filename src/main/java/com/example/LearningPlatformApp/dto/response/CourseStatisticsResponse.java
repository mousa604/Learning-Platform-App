package com.example.LearningPlatformApp.dto.response;

import com.example.LearningPlatformApp.entity.enums.CourseStatus;

public record CourseStatisticsResponse(
        Long courseId,
        String courseTitle,
        CourseStatus courseStatus,
        long enrollmentCount,
        long reviewCount,
        Double averageRating
) {}