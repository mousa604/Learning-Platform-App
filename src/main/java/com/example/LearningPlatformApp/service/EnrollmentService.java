package com.example.LearningPlatformApp.service;

import com.example.LearningPlatformApp.dto.response.EnrollmentResponse;
import com.example.LearningPlatformApp.entity.Course;
import com.example.LearningPlatformApp.entity.Enrollment;
import com.example.LearningPlatformApp.entity.User;
import com.example.LearningPlatformApp.entity.enums.CourseStatus;
import com.example.LearningPlatformApp.entity.enums.EnrollmentStatus;
import com.example.LearningPlatformApp.entity.enums.NotificationType;
import com.example.LearningPlatformApp.exception.BusinessRuleViolationException;
import com.example.LearningPlatformApp.exception.ResourceNotFoundException;
import com.example.LearningPlatformApp.mapper.EnrollmentMapper;
import com.example.LearningPlatformApp.repository.CourseRepository;
import com.example.LearningPlatformApp.repository.EnrollmentRepository;
import com.example.LearningPlatformApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final NotificationService notificationService;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, UserRepository userRepository
                           , CourseRepository courseRepository, EnrollmentMapper enrollmentMapper,
                              NotificationService notificationService
    ){
        this.courseRepository=courseRepository;
        this.enrollmentMapper=enrollmentMapper;
        this.enrollmentRepository=enrollmentRepository;
        this.userRepository=userRepository;
        this.notificationService=notificationService;

    }


    @Transactional
    public EnrollmentResponse enrollStudent(
            String studentEmail,
            Long courseId
    ) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found"
                ));

        Course course = courseRepository
                .findByIdAndStatus(courseId, CourseStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Published course not found with id: " + courseId
                ));

        if (enrollmentRepository.existsByStudentIdAndCourseId(
                student.getId(), courseId)) {
            throw new BusinessRuleViolationException(
                    "Student is already enrolled in this course"
            );
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setEnrolledAt(LocalDateTime.now());

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        notificationService.createNotification(
                student,
                "Course Enrollment Successful",
                "You have successfully enrolled in the course: "
                        + course.getTitle(),
                NotificationType.COURSE_ENROLLED
        );

        return enrollmentMapper.toResponse(savedEnrollment);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getStudentEnrollments(
            String studentEmail
    ) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found"
                ));

        return enrollmentRepository.findByStudentId(student.getId())
                .stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    @Transactional
    public void cancelEnrollment(
            String studentEmail,
            Long courseId
    ) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found"
                ));

        Enrollment enrollment = enrollmentRepository
                .findByStudentIdAndCourseId(student.getId(), courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enrollment not found"
                ));

        if (enrollment.getStatus() != EnrollmentStatus.ACTIVE) {
            throw new BusinessRuleViolationException(
                    "Only active enrollments can be cancelled"
            );
        }

        enrollment.setStatus(EnrollmentStatus.CANCELLED);
    }

}