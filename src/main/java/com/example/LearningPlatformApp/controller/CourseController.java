
package com.example.LearningPlatformApp.controller;

import com.example.LearningPlatformApp.dto.request.CourseRejectionRequest;
import com.example.LearningPlatformApp.dto.request.CourseRequest;
import com.example.LearningPlatformApp.dto.request.CourseTagsRequest;
import com.example.LearningPlatformApp.dto.request.UpdateCourseRequest;
import com.example.LearningPlatformApp.dto.response.CoursePageResponse;
import com.example.LearningPlatformApp.dto.response.CourseResponse;
import com.example.LearningPlatformApp.entity.enums.CourseLevel;
import com.example.LearningPlatformApp.service.CourseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
@Tag(name = "Courses", description = "Browse, search, and manage courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    @Operation(
            summary = "Create a course",
            description = "Creates a course for the authenticated instructor.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Course created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid course data"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Instructor role required")
    })
    public ResponseEntity<CourseResponse> createCourse(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CourseRequest request) {

        CourseResponse response = courseService.createCourse(
                request,
                userDetails.getUsername()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(
            summary = "Get published courses",
            description = "Returns published courses using pagination."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Courses retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<CoursePageResponse> getPublishedCourses(
            @Parameter(description = "Page index, starting from 0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of courses per page")
            @RequestParam(defaultValue = "10") int size) {

        CoursePageResponse response =
                courseService.getPublishedCourses(page, size);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a course",
            description = "Updates a course owned by the authenticated instructor.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid course data"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Not authorized to update this course"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<CourseResponse> updateCourse(
            @Parameter(description = "Course ID")
            @PathVariable Long id,

            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateCourseRequest request) {

        CourseResponse response = courseService.updateCourse(
                id,
                userDetails.getUsername(),
                request
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Archive a course",
            description = "Archives a course owned by the authenticated instructor.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Course archived successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Not authorized to archive this course"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<Void> archiveCourse(
            @Parameter(description = "Course ID")
            @PathVariable Long id,

            @AuthenticationPrincipal UserDetails userDetails) {

        courseService.archiveCourse(id, userDetails.getUsername());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search published courses",
            description = "Searches published courses by keyword, category, and level with pagination."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters")
    })
    public ResponseEntity<CoursePageResponse> searchPublishedCourses(
            @Parameter(description = "Keyword to search for")
            @RequestParam(required = false) String keyword,

            @Parameter(description = "Category ID")
            @RequestParam(required = false) Long categoryId,

            @Parameter(description = "Course difficulty level")
            @RequestParam(required = false) CourseLevel level,

            @Parameter(description = "Page index, starting from 0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of courses per page")
            @RequestParam(defaultValue = "10") int size) {

        CoursePageResponse response =
                courseService.searchPublishedCourses(
                        keyword,
                        categoryId,
                        level,
                        page,
                        size
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{courseId}/submit-for-review")
    @Operation(
            summary = "Submit a course for review",
            description = "Submits an instructor's course for admin approval.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course submitted for review"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Instructor role required"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "409", description = "Course cannot be submitted in its current state")
    })
    public ResponseEntity<CourseResponse> submitCourseForReview(
            @Parameter(description = "Course ID")
            @PathVariable Long courseId,

            @AuthenticationPrincipal UserDetails userDetails) {

        CourseResponse response = courseService.submitCourseForReview(
                courseId,
                userDetails.getUsername()
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{courseId}/approve")
    @Operation(
            summary = "Approve a course",
            description = "Approves a submitted course. Admin access is required.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course approved successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Admin role required"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<CourseResponse> approveCourse(
            @Parameter(description = "Course ID")
            @PathVariable Long courseId) {

        return ResponseEntity.ok(
                courseService.approveCourse(courseId)
        );
    }

    @PatchMapping("/{courseId}/reject")
    @Operation(
            summary = "Reject a course",
            description = "Rejects a submitted course. Admin access is required."
                    + " The request must contain the rejection details.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course rejected successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid rejection data"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Admin role required"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<CourseResponse> rejectCourse(
            @Parameter(description = "Course ID")
            @PathVariable Long courseId,

            @Valid @RequestBody CourseRejectionRequest request) {

        return ResponseEntity.ok(
                courseService.rejectCourse(courseId, request)
        );
    }

    @PutMapping("/{courseId}/tags")
    @Operation(
            summary = "Assign tags to a course",
            description = "Replaces the tags assigned to an instructor's course.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tags assigned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid tag IDs"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Not authorized to update this course"),
            @ApiResponse(responseCode = "404", description = "Course or tag not found")
    })
    public CourseResponse assignTagsToCourse(
            @PathVariable Long courseId,
            @Valid @RequestBody CourseTagsRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return courseService.assignTagsToCourse(
                courseId,
                userDetails.getUsername(),
                request
        );
    }
}
