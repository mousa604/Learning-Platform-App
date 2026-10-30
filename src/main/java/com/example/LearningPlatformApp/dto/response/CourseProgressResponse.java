
package com.example.LearningPlatformApp.dto.response;

public record CourseProgressResponse(
        Long courseId,
        long totalLessons,
        long completedLessons,
        double progressPercentage
) {
}
