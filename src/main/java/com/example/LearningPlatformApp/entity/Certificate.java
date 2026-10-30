package com.example.LearningPlatformApp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "certificates",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_certificate_number",
                        columnNames = "certificate_number"
                ),
                @UniqueConstraint(
                        name = "uk_student_course_certificate",
                        columnNames = {"student_id", "course_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certificate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(
            name = "certificate_number",
            nullable = false,
            length = 100
    )
    private String certificateNumber;

    @Column(
            name = "issued_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime issuedAt;

    @Column(name = "file_path", length = 500)
    private String filePath;

    @PrePersist
    protected void onCreate() {
        if (issuedAt == null) {
            issuedAt = LocalDateTime.now();
        }
    }
}