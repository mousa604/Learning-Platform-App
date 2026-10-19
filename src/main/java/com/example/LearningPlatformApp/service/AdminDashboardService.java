package com.example.LearningPlatformApp.service;

import com.example.LearningPlatformApp.dto.response.AdminDashboardResponse;
import com.example.LearningPlatformApp.entity.enums.CourseStatus;
import com.example.LearningPlatformApp.repository.CourseRepository;
import com.example.LearningPlatformApp.repository.EnrollmentRepository;
import com.example.LearningPlatformApp.repository.ReviewRepository;
import com.example.LearningPlatformApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public AdminDashboardService(UserRepository userRepository,
                                 CourseRepository courseRepository,
                                 EnrollmentRepository enrollmentRepository,
                                 ReviewRepository reviewRepository ){
        this.userRepository=userRepository;
        this.courseRepository=courseRepository;
        this.enrollmentRepository=enrollmentRepository;
        this.reviewRepository=reviewRepository;
    }

    @Transactional(readOnly = true)
    public AdminDashboardResponse getDashboardStatistics() {

        return new AdminDashboardResponse(
                userRepository.count(),
                courseRepository.count(),
                courseRepository.countByStatus(CourseStatus.PUBLISHED),
                courseRepository.countByStatus(CourseStatus.PENDING_REVIEW),
                courseRepository.countByStatus(CourseStatus.DRAFT),
                courseRepository.countByStatus(CourseStatus.REJECTED),
                courseRepository.countByStatus(CourseStatus.ARCHIVED),
                enrollmentRepository.count(),
                reviewRepository.count()
        );
    }
}