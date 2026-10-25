package com.example.LearningPlatformApp.dto.response;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String bio,
        String profileImage,
        boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}