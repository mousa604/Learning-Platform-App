package com.example.LearningPlatformApp.controller;

import com.example.LearningPlatformApp.dto.response.EnrollmentResponse;
import com.example.LearningPlatformApp.service.EnrollmentService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService){
        this.enrollmentService=enrollmentService;
    }

    // Enroll a student in a course
    @PostMapping("/courses/{courseId}/enrollments")
    public ResponseEntity<EnrollmentResponse> enrollStudent(
            @PathVariable Long courseId,
            @RequestParam Long studentId
    ) {
        EnrollmentResponse response =
                enrollmentService.enrollStudent(studentId, courseId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get all enrollments for a student
    @GetMapping("/students/{studentId}/enrollments")
    public ResponseEntity<List<EnrollmentResponse>> getStudentEnrollments(
            @PathVariable Long studentId
    ) {
        return ResponseEntity.ok(
                enrollmentService.getStudentEnrollments(studentId)
        );
    }

    // Cancel a student's enrollment
    @PatchMapping("/courses/{courseId}/enrollments/cancel")
    public ResponseEntity<Void> cancelEnrollment(
            @PathVariable Long courseId,
            @RequestParam Long studentId
    ) {
        enrollmentService.cancelEnrollment(studentId, courseId);

        return ResponseEntity.noContent().build();
    }
}