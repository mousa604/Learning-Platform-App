package com.example.LearningPlatformApp.repository;

import com.example.LearningPlatformApp.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository
        extends JpaRepository<Certificate, Long> {

    boolean existsByStudentIdAndCourseId(
            Long studentId,
            Long courseId
    );

    Optional<Certificate> findByCertificateNumber(
            String certificateNumber
    );

    List<Certificate> findByStudentIdOrderByIssuedAtDesc(
            Long studentId
    );


}