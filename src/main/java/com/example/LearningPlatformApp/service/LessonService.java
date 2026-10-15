package com.example.LearningPlatformApp.service;

import com.example.LearningPlatformApp.dto.request.LessonRequest;
import com.example.LearningPlatformApp.dto.response.LessonResponse;
import com.example.LearningPlatformApp.entity.Course;
import com.example.LearningPlatformApp.entity.Lesson;
import com.example.LearningPlatformApp.exception.BusinessRuleViolationException;
import com.example.LearningPlatformApp.exception.ResourceNotFoundException;
import com.example.LearningPlatformApp.mapper.LessonMapper;
import com.example.LearningPlatformApp.repository.CourseRepository;
import com.example.LearningPlatformApp.repository.LessonRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final LessonMapper lessonMapper;
    @Autowired
    public LessonService(LessonRepository lessonRepository,CourseRepository courseRepository,LessonMapper lessonMapper){
        this.courseRepository=courseRepository;
        this.lessonRepository=lessonRepository;
        this.lessonMapper=lessonMapper;
    }

    @Transactional
    public LessonResponse createLesson(
            Long courseId,
            Long instructorId,
            LessonRequest request
    ) {
        Course course = getInstructorCourse(courseId, instructorId);

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
            Long instructorId,
            LessonRequest request
    ) {
        getInstructorCourse(courseId, instructorId);

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
         lesson = lessonRepository.saveAndFlush(lesson);

        return lessonMapper.toResponse(lesson);
    }

    @Transactional
    public void deleteLesson(
            Long courseId,
            Long lessonId,
            Long instructorId
    ) {
        getInstructorCourse(courseId, instructorId);

        Lesson lesson = lessonRepository
                .findByIdAndCourseId(lessonId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lesson not found with id: " + lessonId
                ));

        lessonRepository.delete(lesson);
    }

    private Course getInstructorCourse(
            Long courseId,
            Long instructorId
    ) {
        return courseRepository
                .findByIdAndInstructorId(courseId, instructorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Course not found or does not belong to this instructor"
                ));
    }
}