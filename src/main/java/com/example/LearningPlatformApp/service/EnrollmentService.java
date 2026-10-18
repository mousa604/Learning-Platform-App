package com.example.LearningPlatformApp.service;

import com.example.LearningPlatformApp.dto.response.EnrollmentResponse;
import com.example.LearningPlatformApp.entity.Course;
import com.example.LearningPlatformApp.entity.Enrollment;
import com.example.LearningPlatformApp.entity.User;
import com.example.LearningPlatformApp.entity.enums.CourseStatus;
import com.example.LearningPlatformApp.entity.enums.EnrollmentStatus;
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

    public EnrollmentService(EnrollmentRepository enrollmentRepository, UserRepository userRepository
                           , CourseRepository courseRepository, EnrollmentMapper enrollmentMapper
    ){
        this.courseRepository=courseRepository;
        this.enrollmentMapper=enrollmentMapper;
        this.enrollmentRepository=enrollmentRepository;
        this.userRepository=userRepository;

    }

    @Transactional
    public EnrollmentResponse enrollStudent(
            Long studentId,
            Long courseId
    ) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found with id: " + studentId
                ));

        Course course = courseRepository
                .findByIdAndStatus(courseId, CourseStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Published course not found with id: " + courseId
                ));

        if (enrollmentRepository.existsByStudentIdAndCourseId(
                studentId, courseId)) {
            throw new BusinessRuleViolationException(
                    "Student is already enrolled in this course"
            );
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setEnrolledAt(LocalDateTime.now());

        Enrollment savedEnrollment =
                enrollmentRepository.save(enrollment);

        return enrollmentMapper.toResponse(savedEnrollment);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getStudentEnrollments(
            Long studentId
    ) {
        if (!userRepository.existsById(studentId)) {
            throw new ResourceNotFoundException(
                    "Student not found with id: " + studentId
            );
        }

        return enrollmentRepository.findByStudentId(studentId)
                .stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    @Transactional
    public void cancelEnrollment(
            Long studentId,
            Long courseId
    ) {
        Enrollment enrollment = enrollmentRepository
                .findByStudentIdAndCourseId(studentId, courseId)
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