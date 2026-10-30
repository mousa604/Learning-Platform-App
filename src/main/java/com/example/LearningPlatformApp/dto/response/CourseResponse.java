package com.example.LearningPlatformApp.dto.response;


import com.example.LearningPlatformApp.entity.enums.CourseLevel;
import com.example.LearningPlatformApp.entity.enums.CourseStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CourseResponse(
        Long id,
        String title,
        String description,
        BigDecimal price,
        CourseLevel level,
        CourseStatus status,
        String thumbnail,
        LocalDateTime publishedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long instructorId,
        String instructorName,
        Long categoryId,
        String categoryName,
        List<TagResponse>tags
) {
}
