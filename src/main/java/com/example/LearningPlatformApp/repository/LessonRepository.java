package com.example.LearningPlatformApp.repository;

import com.example.LearningPlatformApp.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByCourseIdOrderByLessonOrder(Long courseId);

    Optional<Lesson> findByIdAndCourseId(Long id, Long courseId);

    boolean existsByCourseIdAndLessonOrder(
            Long courseId,
            Integer lessonOrder
    );

    boolean existsByCourseIdAndLessonOrderAndIdNot(
            Long courseId,
            Integer lessonOrder,
            Long id
    );
    List<Lesson> findByCourseIdOrderByLessonOrderAsc(
            Long courseId
    );

    long countByCourseId(Long courseId);


}