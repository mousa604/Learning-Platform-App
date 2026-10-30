
package com.example.LearningPlatformApp.controller;

import com.example.LearningPlatformApp.dto.response.EnrollmentResponse;
import com.example.LearningPlatformApp.service.EnrollmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Enrollments", description = "Manage student course enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/courses/{courseId}/enrollments")
    @Operation(
            summary = "Enroll in a course",
            description = "Enrolls the authenticated student in the specified course.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Enrollment created successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Student role required"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "409", description = "Enrollment conflicts with the current state")
    })
    public ResponseEntity<EnrollmentResponse> enrollStudent(
            @Parameter(description = "ID of the course")
            @PathVariable Long courseId,

            @AuthenticationPrincipal UserDetails userDetails) {

        EnrollmentResponse response = enrollmentService.enrollStudent(
                userDetails.getUsername(),
                courseId
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/students/me/enrollments")
    @Operation(
            summary = "Get my enrollments",
            description = "Returns the enrollments belonging to the authenticated student.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Student role required")
    })
    public ResponseEntity<List<EnrollmentResponse>> getStudentEnrollments(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                enrollmentService.getStudentEnrollments(
                        userDetails.getUsername()
                )
        );
    }

    @PatchMapping("/courses/{courseId}/enrollments/cancel")
    @Operation(
            summary = "Cancel course enrollment",
            description = "Cancels the authenticated student's enrollment in a course.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Enrollment cancelled successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Student role required"),
            @ApiResponse(responseCode = "404", description = "Course or enrollment not found"),
            @ApiResponse(responseCode = "409", description = "Enrollment cannot be cancelled in its current state")
    })
    public ResponseEntity<Void> cancelEnrollment(
            @Parameter(description = "ID of the course")
            @PathVariable Long courseId,

            @AuthenticationPrincipal UserDetails userDetails) {

        enrollmentService.cancelEnrollment(
                userDetails.getUsername(),
                courseId
        );

        return ResponseEntity.noContent().build();
    }
}
