package com.example.LearningPlatformApp.dto.request;

import com.example.LearningPlatformApp.entity.enums.CourseLevel;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CourseRequest(

        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,

        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.00", message = "Price must be zero or greater")
        BigDecimal price,

        @NotNull(message = "Level is required")
        CourseLevel level,

        @NotNull(message = "Category ID is required")
        Long categoryId,

        @Size(max = 255, message = "Thumbnail URL must not exceed 255 characters")
        String thumbnail

) {
}
