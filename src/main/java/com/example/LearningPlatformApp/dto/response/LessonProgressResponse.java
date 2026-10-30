
package com.example.LearningPlatformApp.dto.response;

import java.time.LocalDateTime;

public record LessonProgressResponse(
        Long lessonId,
        String lessonTitle,
        boolean completed,
        LocalDateTime completedAt
) {
}
