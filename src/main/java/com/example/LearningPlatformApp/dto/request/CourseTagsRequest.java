
package com.example.LearningPlatformApp.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record CourseTagsRequest(

        @NotNull(message = "Tag IDs must not be null")
        List<
                @NotNull(message = "Tag ID must not be null")
                @Positive(message = "Tag ID must be greater than zero")
                        Long
                > tagIds

) {}
