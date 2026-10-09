package com.example.LearningPlatformApp.repository;

import com.example.LearningPlatformApp.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository
        extends JpaRepository<Review, Long> {

    boolean existsByStudentIdAndCourseId(
            Long studentId,
            Long courseId
    );

    Optional<Review> findByStudentIdAndCourseId(
            Long studentId,
            Long courseId
    );

    List<Review> findByCourseId(Long courseId);

    List<Review> findByStudentId(Long studentId);
}