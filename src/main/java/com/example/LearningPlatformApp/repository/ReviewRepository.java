package com.example.LearningPlatformApp.repository;

import com.example.LearningPlatformApp.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    Optional<Review> findByIdAndStudentId(
            Long reviewId,
            Long studentId
    );

    List<Review> findByCourseIdOrderByCreatedAtDesc(Long courseId);


    @Query("""
    SELECT AVG(r.rating)
    FROM Review r
    WHERE r.course.id = :courseId
    """)
    Double findAverageRatingByCourseId(
            @Param("courseId") Long courseId
    );

    long countByCourseId(Long courseId);
}