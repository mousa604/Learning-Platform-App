package com.example.LearningPlatformApp.service;

import com.example.LearningPlatformApp.dto.response.CourseProgressResponse;
import com.example.LearningPlatformApp.dto.response.LessonProgressResponse;
import com.example.LearningPlatformApp.entity.Enrollment;
import com.example.LearningPlatformApp.entity.Lesson;
import com.example.LearningPlatformApp.entity.LessonProgress;
import com.example.LearningPlatformApp.entity.User;
import com.example.LearningPlatformApp.entity.enums.EnrollmentStatus;
import com.example.LearningPlatformApp.entity.enums.NotificationType;
import com.example.LearningPlatformApp.exception.BusinessRuleViolationException;
import com.example.LearningPlatformApp.exception.ResourceNotFoundException;
import com.example.LearningPlatformApp.mapper.LessonProgressMapper;
import com.example.LearningPlatformApp.repository.EnrollmentRepository;
import com.example.LearningPlatformApp.repository.LessonProgressRepository;
import com.example.LearningPlatformApp.repository.LessonRepository;
import com.example.LearningPlatformApp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class LessonProgressService {

    private static final Logger log =
            LoggerFactory.getLogger(LessonProgressService.class);

    private final LessonProgressRepository lessonProgressRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonProgressMapper lessonProgressMapper;
    private final NotificationService notificationService;

    public LessonProgressService(
            LessonProgressRepository lessonProgressRepository,
            LessonRepository lessonRepository,
            UserRepository userRepository,
            EnrollmentRepository enrollmentRepository,
            LessonProgressMapper lessonProgressMapper,
            NotificationService notificationService
    ) {
        this.lessonProgressRepository = lessonProgressRepository;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.lessonProgressMapper = lessonProgressMapper;
        this.notificationService = notificationService;
    }

    @Transactional
    public LessonProgressResponse markLessonCompleted(
            String studentEmail,
            Long lessonId
    ) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lesson not found"));

        Long studentId = student.getId();
        Long courseId = lesson.getCourse().getId();

        Enrollment enrollment = getAccessibleEnrollment(studentId, courseId);

        LessonProgress progress = lessonProgressRepository
                .findByUserIdAndLessonId(studentId, lessonId)
                .orElseGet(() -> LessonProgress.builder()
                        .user(student)
                        .lesson(lesson)
                        .completed(false)
                        .build());

        if (!progress.isCompleted()) {
            progress.setCompleted(true);
            progress.setCompletedAt(LocalDateTime.now());
        }

        LessonProgress savedProgress =
                lessonProgressRepository.save(progress);

        long totalLessons = lessonRepository.countByCourseId(courseId);

        long completedLessons = lessonProgressRepository
                .countByUserIdAndLessonCourseIdAndCompletedTrue(
                        studentId, courseId
                );

        log.debug(
                "Course progress: studentId={}, courseId={}, totalLessons={}, completedLessons={}",
                studentId, courseId, totalLessons, completedLessons
        );

        if (totalLessons > 0 && completedLessons == totalLessons) {

            if (enrollment.getStatus() != EnrollmentStatus.COMPLETED) {

                enrollment.setCompletedAt(LocalDateTime.now());
                enrollment.setStatus(EnrollmentStatus.COMPLETED);

                enrollmentRepository.save(enrollment);

                notificationService.createNotification(
                        student,
                        "Course Completed!",
                        "Congratulations! You have successfully completed the course: "
                                + lesson.getCourse().getTitle(),
                        NotificationType.COURSE_COMPLETED
                );

                log.info(
                        "Course completed: studentId={}, courseId={}",
                        studentId, courseId
                );
            }
        }

        return lessonProgressMapper.toResponse(savedProgress);
    }

    @Transactional(readOnly = true)
    public CourseProgressResponse getCourseProgress(
            String studentEmail,
            Long courseId
    ) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        getAccessibleEnrollment(student.getId(), courseId);

        long totalLessons = lessonRepository.countByCourseId(courseId);

        long completedLessons =
                lessonProgressRepository
                        .countByUserIdAndLessonCourseIdAndCompletedTrue(
                                student.getId(), courseId
                        );

        double progressPercentage = totalLessons == 0
                ? 0.0
                : Math.round(
                completedLessons * 10000.0 / totalLessons
        ) / 100.0;

        return new CourseProgressResponse(
                courseId,
                totalLessons,
                completedLessons,
                progressPercentage
        );
    }

    private Enrollment getAccessibleEnrollment(
            Long studentId,
            Long courseId
    ) {
        Enrollment enrollment = enrollmentRepository
                .findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() ->
                        new BusinessRuleViolationException(
                                "You must enroll in the course first"
                        ));

        if (enrollment.getStatus() != EnrollmentStatus.ACTIVE
                && enrollment.getStatus() != EnrollmentStatus.COMPLETED) {
            throw new BusinessRuleViolationException(
                    "Your enrollment is not active"
            );
        }

        return enrollment;
    }
}