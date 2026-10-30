
package com.example.LearningPlatformApp.controller;

import com.example.LearningPlatformApp.dto.request.LessonRequest;
import com.example.LearningPlatformApp.dto.response.LessonResponse;
import com.example.LearningPlatformApp.service.LessonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/lessons")
@Tag(name = "Lessons", description = "Create, retrieve, update, and delete course lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping
    @Operation(
            summary = "Create a lesson",
            description = "Creates a lesson in a course owned by the authenticated instructor.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Lesson created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid lesson data"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Instructor role required or course access denied"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "409", description = "Lesson order conflicts with an existing lesson")
    })
    public ResponseEntity<LessonResponse> createLesson(
            @Parameter(description = "ID of the course")
            @PathVariable Long courseId,

            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody LessonRequest request) {

        LessonResponse response = lessonService.createLesson(
                courseId,
                userDetails.getUsername(),
                request
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(
            summary = "Get course lessons",
            description = "Returns the lessons belonging to a course."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lessons retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<List<LessonResponse>> getCourseLessons(
            @Parameter(description = "ID of the course")
            @PathVariable Long courseId) {

        return ResponseEntity.ok(
                lessonService.getCourseLessons(courseId)
        );
    }

    @PutMapping("/{lessonId}")
    @Operation(
            summary = "Update a lesson",
            description = "Updates a lesson in a course owned by the authenticated instructor.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lesson updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid lesson data"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Instructor role required or course access denied"),
            @ApiResponse(responseCode = "404", description = "Course or lesson not found"),
            @ApiResponse(responseCode = "409", description = "Lesson order conflicts with another lesson")
    })
    public ResponseEntity<LessonResponse> updateLesson(
            @Parameter(description = "ID of the course")
            @PathVariable Long courseId,

            @Parameter(description = "ID of the lesson")
            @PathVariable Long lessonId,

            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody LessonRequest request) {

        return ResponseEntity.ok(
                lessonService.updateLesson(
                        courseId,
                        lessonId,
                        userDetails.getUsername(),
                        request
                )
        );
    }

    @DeleteMapping("/{lessonId}")
    @Operation(
            summary = "Delete a lesson",
            description = "Deletes a lesson from a course owned by the authenticated instructor.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Lesson deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Instructor role required or course access denied"),
            @ApiResponse(responseCode = "404", description = "Course or lesson not found")
    })
    public ResponseEntity<Void> deleteLesson(
            @Parameter(description = "ID of the course")
            @PathVariable Long courseId,

            @Parameter(description = "ID of the lesson")
            @PathVariable Long lessonId,

            @AuthenticationPrincipal UserDetails userDetails) {

        lessonService.deleteLesson(
                courseId,
                lessonId,
                userDetails.getUsername()
        );

        return ResponseEntity.noContent().build();
    }
}
