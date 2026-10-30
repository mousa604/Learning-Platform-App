package com.example.LearningPlatformApp.controller;

import com.example.LearningPlatformApp.dto.response.CourseProgressResponse;
import com.example.LearningPlatformApp.dto.response.LessonProgressResponse;
import com.example.LearningPlatformApp.service.LessonProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lessons")
@Tag(
        name = "Lesson Progress",
        description = "Endpoints for tracking student lesson and course progress"
)
@SecurityRequirement(name = "bearerAuth")
public class LessonProgressController {

    private final LessonProgressService lessonProgressService;

    public LessonProgressController(
            LessonProgressService lessonProgressService
    ) {
        this.lessonProgressService = lessonProgressService;
    }

    @PutMapping("/{lessonId}/progress/complete")
    @Operation(
            summary = "Mark lesson as completed",
            description = "Marks a lesson as completed for the authenticated student"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lesson marked as completed successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication is required or token is invalid"),
            @ApiResponse(responseCode = "403", description = "User does not have permission to perform this action"),
            @ApiResponse(responseCode = "404", description = "Lesson or student not found"),
            @ApiResponse(responseCode = "409", description = "Business rule violation")
    })
    public ResponseEntity<LessonProgressResponse> completeLesson(
            @Parameter(description = "ID of the lesson", example = "10")
            @PathVariable Long lessonId,

            @AuthenticationPrincipal UserDetails userDetails
    ) {
        LessonProgressResponse response =
                lessonProgressService.markLessonCompleted(
                        userDetails.getUsername(),
                        lessonId
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/courses/{courseId}/progress")
    @Operation(
            summary = "Get course progress",
            description = "Returns the authenticated student's progress in a specific course"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course progress retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication is required or token is invalid"),
            @ApiResponse(responseCode = "403", description = "User does not have permission to access this resource"),
            @ApiResponse(responseCode = "404", description = "Course or student not found")
    })
    public ResponseEntity<CourseProgressResponse> getCourseProgress(
            @Parameter(description = "ID of the course", example = "5")
            @PathVariable Long courseId,

            @AuthenticationPrincipal UserDetails userDetails
    ) {
        CourseProgressResponse response =
                lessonProgressService.getCourseProgress(
                        userDetails.getUsername(),
                        courseId
                );

        return ResponseEntity.ok(response);
    }
}