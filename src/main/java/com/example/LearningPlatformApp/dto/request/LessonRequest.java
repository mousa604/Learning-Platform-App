package com.example.LearningPlatformApp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record LessonRequest(

        @NotBlank(message = "Lesson title is required")
        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,

        String description,

        @Size(max = 255, message = "Video URL must not exceed 255 characters")
        String videoUrl,

        @Positive(message = "Duration must be greater than zero")
        Integer duration,

        @Positive(message = "Lesson order must be greater than zero")
        @NotNull(message = "Lesson order is required")
        Integer lessonOrder

) {}