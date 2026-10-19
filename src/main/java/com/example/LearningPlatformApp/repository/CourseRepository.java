package com.example.LearningPlatformApp.repository;

import com.example.LearningPlatformApp.entity.Course;
import com.example.LearningPlatformApp.entity.enums.CourseLevel;
import com.example.LearningPlatformApp.entity.enums.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByStatus(
            CourseStatus status,
            Pageable pageable
    );

    Optional<Course> findByIdAndStatus(
            Long id,
            CourseStatus status
    );

    Optional<Course> findByIdAndInstructorId(
            Long id,
            Long instructorId
    );

    @Query("""
        SELECT c
        FROM Course c
        WHERE c.status = :status
          AND (
              :keyword IS NULL
              OR LOWER(c.title) LIKE LOWER(
                  CONCAT('%', :keyword, '%')
              )
          )
          AND (
              :categoryId IS NULL
              OR c.category.id = :categoryId
          )
          AND (
              :level IS NULL
              OR c.level = :level
          )
        """)
    Page<Course> searchPublishedCourses(
            @Param("status") CourseStatus status,
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            @Param("level") CourseLevel level,
            Pageable pageable
    );

    long countByStatus(CourseStatus status);


}
