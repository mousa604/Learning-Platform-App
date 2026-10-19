package com.example.LearningPlatformApp.dto.response;

public record AdminDashboardResponse(
        long totalUsers,
        long totalCourses,
        long publishedCourses,
        long pendingCourses,
        long draftCourses,
        long rejectedCourses,
        long archivedCourses,
        long totalEnrollments,
        long totalReviews
) {}