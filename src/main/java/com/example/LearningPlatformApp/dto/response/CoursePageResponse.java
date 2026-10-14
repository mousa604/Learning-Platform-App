package com.example.LearningPlatformApp.dto.response;

import java.util.List;

public record CoursePageResponse(
        List<CourseResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {}
