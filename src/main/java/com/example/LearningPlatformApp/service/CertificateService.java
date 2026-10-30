
package com.example.LearningPlatformApp.service;

import com.example.LearningPlatformApp.dto.response.CertificateResponse;
import com.example.LearningPlatformApp.entity.Certificate;
import com.example.LearningPlatformApp.entity.Course;
import com.example.LearningPlatformApp.entity.User;
import com.example.LearningPlatformApp.entity.enums.NotificationType;
import com.example.LearningPlatformApp.exception.BusinessRuleViolationException;
import com.example.LearningPlatformApp.exception.ResourceNotFoundException;
import com.example.LearningPlatformApp.mapper.CertificateMapper;
import com.example.LearningPlatformApp.repository.CertificateRepository;
import com.example.LearningPlatformApp.repository.CourseRepository;
import com.example.LearningPlatformApp.repository.EnrollmentRepository;
import com.example.LearningPlatformApp.repository.LessonProgressRepository;
import com.example.LearningPlatformApp.repository.LessonRepository;
import com.example.LearningPlatformApp.repository.UserRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final CertificateMapper certificateMapper;
    private final CertificatePdfService certificatePdfService;
    private final NotificationService notificationService;

    public CertificateService(
            CertificateRepository certificateRepository,
            UserRepository userRepository,
            CourseRepository courseRepository,
            EnrollmentRepository enrollmentRepository,
            LessonRepository lessonRepository,
            LessonProgressRepository lessonProgressRepository,
            CertificateMapper certificateMapper,
            CertificatePdfService certificatePdfService,
            NotificationService notificationService
    ) {
        this.certificateRepository = certificateRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.lessonRepository = lessonRepository;
        this.lessonProgressRepository = lessonProgressRepository;
        this.certificateMapper = certificateMapper;
        this.certificatePdfService = certificatePdfService;
        this.notificationService = notificationService;
    }

    @Transactional
    public CertificateResponse issueCertificate(
            String studentEmail,
            Long courseId
    ) {

        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found"));

        boolean enrolled =
                enrollmentRepository.existsByStudentIdAndCourseId(
                        student.getId(),
                        courseId
                );

        if (!enrolled) {
            throw new BusinessRuleViolationException(
                    "You must enroll in the course first"
            );
        }

        long totalLessons = lessonRepository.countByCourseId(courseId);

        if (totalLessons == 0) {
            throw new BusinessRuleViolationException(
                    "Cannot issue a certificate for a course with no lessons"
            );
        }

        long completedLessons =
                lessonProgressRepository
                        .countByUserIdAndLessonCourseIdAndCompletedTrue(
                                student.getId(),
                                courseId
                        );

        if (completedLessons != totalLessons) {
            throw new BusinessRuleViolationException(
                    "You must complete all lessons before receiving a certificate"
            );
        }

        boolean certificateExists =
                certificateRepository.existsByStudentIdAndCourseId(
                        student.getId(),
                        courseId
                );

        if (certificateExists) {
            throw new BusinessRuleViolationException(
                    "You already have a certificate for this course"
            );
        }


        Certificate certificate = Certificate.builder()
                .student(student)
                .course(course)
                .certificateNumber(UUID.randomUUID().toString())
                .build();


        Certificate savedCertificate =
                certificateRepository.save(certificate);


        String filePath =
                certificatePdfService.generateCertificate(savedCertificate);


        savedCertificate.setFilePath(filePath);

        Certificate updatedCertificate =
                certificateRepository.save(savedCertificate);


        notificationService.createNotification(
                student,
                "Certificate Issued!",
                "Congratulations! Your certificate for the course: "
                        + course.getTitle()
                        + " has been issued successfully.",
                NotificationType.CERTIFICATE_ISSUED
        );

        return certificateMapper.toResponse(updatedCertificate);
    }

    @Transactional(readOnly = true)
    public List<CertificateResponse> getMyCertificates(String studentEmail) {

        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        List<Certificate> certificates =
                certificateRepository
                        .findByStudentIdOrderByIssuedAtDesc(student.getId());

        return certificates.stream()
                .map(certificateMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CertificateResponse verifyCertificate(String certificateNumber) {

        Certificate certificate = certificateRepository
                .findByCertificateNumber(certificateNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Certificate not found"));

        return certificateMapper.toResponse(certificate);
    }

    @Transactional(readOnly = true)
    public Resource downloadCertificate(
            String studentEmail,
            String certificateNumber
    ) throws IOException {


        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));


        Certificate certificate = certificateRepository
                .findByCertificateNumber(certificateNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Certificate not found"));


        if (!certificate.getStudent().getId().equals(student.getId())) {
            throw new AccessDeniedException(
                    "You are not allowed to download this certificate"
            );
        }


        if (certificate.getFilePath() == null
                || certificate.getFilePath().isBlank()) {
            throw new ResourceNotFoundException(
                    "Certificate file not found"
            );
        }


        Path directory = Path.of("uploads", "certificates")
                .toAbsolutePath()
                .normalize();

        Path path = Path.of(certificate.getFilePath())
                .toAbsolutePath()
                .normalize();

        if (!path.startsWith(directory)) {
            throw new ResourceNotFoundException(
                    "Invalid certificate file path"
            );
        }


        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new ResourceNotFoundException(
                    "Certificate PDF file does not exist"
            );
        }

        return resource;
    }
}
