package com.example.LearningPlatformApp.repository;

import com.example.LearningPlatformApp.entity.Enrollment;
import com.example.LearningPlatformApp.entity.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Collection;

public interface EnrollmentRepository
        extends JpaRepository<Enrollment, Long> {

    boolean existsByStudentIdAndCourseId(
            Long studentId,
            Long courseId
    );

    Optional<Enrollment> findByStudentIdAndCourseId(
            Long studentId,
            Long courseId
    );

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByCourseId(Long courseId);

    boolean existsByCourseId(Long courseId);
    boolean existsByStudentIdAndCourseIdAndStatusIn(
            Long studentId,
            Long courseId,
            Collection<EnrollmentStatus> statuses
    );

    long countByCourseIdAndStatusIn(
            Long courseId,
            Collection<EnrollmentStatus> statuses
    );
}