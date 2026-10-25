
package com.example.LearningPlatformApp.dto.auth;

public record RegisterResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String message
) {}
