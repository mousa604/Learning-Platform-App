package com.example.LearningPlatformApp.dto.response;

import java.time.LocalDateTime;

public record CertificateResponse(
        Long id,
        String certificateNumber,
        Long courseId,
        String courseTitle,
        LocalDateTime issuedAt,
        String filePath
) {
}