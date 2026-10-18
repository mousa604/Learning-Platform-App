package com.example.LearningPlatformApp.service;

import com.example.LearningPlatformApp.dto.response.CourseStatisticsResponse;
import com.example.LearningPlatformApp.entity.Course;
import com.example.LearningPlatformApp.entity.enums.EnrollmentStatus;
import com.example.LearningPlatformApp.exception.ResourceNotFoundException;
import com.example.LearningPlatformApp.repository.CourseRepository;
import com.example.LearningPlatformApp.repository.EnrollmentRepository;
import com.example.LearningPlatformApp.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseStatisticsService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public CourseStatisticsService(CourseRepository courseRepository,
                                   EnrollmentRepository enrollmentRepository,
                                   ReviewRepository reviewRepository){
        this.courseRepository=courseRepository;
        this.enrollmentRepository=enrollmentRepository;
        this.reviewRepository=reviewRepository;
    }

    @Transactional(readOnly = true)
    public CourseStatisticsResponse getCourseStatistics(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Course not found with id: " + courseId
                ));

        long enrollmentCount =
                enrollmentRepository.countByCourseIdAndStatusIn(
                        courseId,
                        List.of(
                                EnrollmentStatus.ACTIVE,
                                EnrollmentStatus.COMPLETED
                        )
                );

        long reviewCount =
                reviewRepository.countByCourseId(courseId);

        Double averageRating =
                reviewRepository.findAverageRatingByCourseId(courseId);

        if (averageRating != null) {
            averageRating = Math.round(averageRating * 100.0) / 100.0;
        }

        return new CourseStatisticsResponse(
                course.getId(),
                course.getTitle(),
                course.getStatus(),
                enrollmentCount,
                reviewCount,
                averageRating
        );
    }
}