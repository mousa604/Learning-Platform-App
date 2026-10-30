
package com.example.LearningPlatformApp.service;

import com.example.LearningPlatformApp.dto.request.LessonRequest;
import com.example.LearningPlatformApp.dto.response.LessonResponse;
import com.example.LearningPlatformApp.entity.Course;
import com.example.LearningPlatformApp.entity.Lesson;
import com.example.LearningPlatformApp.entity.User;
import com.example.LearningPlatformApp.exception.BusinessRuleViolationException;
import com.example.LearningPlatformApp.exception.ResourceNotFoundException;
import com.example.LearningPlatformApp.mapper.LessonMapper;
import com.example.LearningPlatformApp.repository.CourseRepository;
import com.example.LearningPlatformApp.repository.LessonRepository;
import com.example.LearningPlatformApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final LessonMapper lessonMapper;

    @Transactional
    public LessonResponse createLesson(
            Long courseId,
            String instructorEmail,
            LessonRequest request
    ) {
        Course course = getInstructorCourse(courseId, instructorEmail);

        if (lessonRepository.existsByCourseIdAndLessonOrder(
                courseId, request.lessonOrder())) {
            throw new BusinessRuleViolationException(
                    "A lesson with this order already exists in the course"
            );
        }

        Lesson lesson = lessonMapper.toEntity(request);
        lesson.setCourse(course);

        Lesson savedLesson = lessonRepository.save(lesson);

        return lessonMapper.toResponse(savedLesson);
    }

    @Transactional(readOnly = true)
    public List<LessonResponse> getCourseLessons(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException(
                    "Course not found with id: " + courseId
            );
        }

        return lessonRepository
                .findByCourseIdOrderByLessonOrderAsc(courseId)
                .stream()
                .map(lessonMapper::toResponse)
                .toList();
    }

    @Transactional
    public LessonResponse updateLesson(
            Long courseId,
            Long lessonId,
            String instructorEmail,
            LessonRequest request
    ) {
        getInstructorCourse(courseId, instructorEmail);

        Lesson lesson = lessonRepository
                .findByIdAndCourseId(lessonId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lesson not found with id: " + lessonId
                ));

        boolean orderUsed =
                lessonRepository.existsByCourseIdAndLessonOrderAndIdNot(
                        courseId,
                        request.lessonOrder(),
                        lessonId
                );

        if (orderUsed) {
            throw new BusinessRuleViolationException(
                    "A lesson with this order already exists in the course"
            );
        }

        lessonMapper.updateEntity(request, lesson);

        Lesson updatedLesson = lessonRepository.saveAndFlush(lesson);

        return lessonMapper.toResponse(updatedLesson);
    }

    @Transactional
    public void deleteLesson(
            Long courseId,
            Long lessonId,
            String instructorEmail
    ) {
        getInstructorCourse(courseId, instructorEmail);

        Lesson lesson = lessonRepository
                .findByIdAndCourseId(lessonId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lesson not found with id: " + lessonId
                ));

        lessonRepository.delete(lesson);
    }

    private Course getInstructorCourse(
            Long courseId,
            String instructorEmail
    ) {
        User instructor = userRepository.findByEmail(instructorEmail)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Instructor not found"
                ));

        return courseRepository
                .findByIdAndInstructorId(courseId, instructor.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Course not found or does not belong to this instructor"
                ));
    }
}
