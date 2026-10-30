
package com.example.LearningPlatformApp.controller;

import com.example.LearningPlatformApp.dto.request.ReviewRequest;
import com.example.LearningPlatformApp.dto.response.ReviewResponse;
import com.example.LearningPlatformApp.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Create, retrieve, update, and delete course reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/courses/{courseId}/reviews")
    @Operation(
            summary = "Create a course review",
            description = "Creates a review for a course by the authenticated student.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Review created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid review data"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Student role required or course enrollment requirements not met"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "409", description = "Review already exists or review requirements not met")
    })
    public ResponseEntity<ReviewResponse> createReview(
            @Parameter(description = "ID of the course")
            @PathVariable Long courseId,

            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ReviewRequest request) {

        ReviewResponse response = reviewService.createReview(
                userDetails.getUsername(),
                courseId,
                request
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/courses/{courseId}/reviews")
    @Operation(
            summary = "Get course reviews",
            description = "Returns the reviews associated with a course."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<List<ReviewResponse>> getCourseReviews(
            @Parameter(description = "ID of the course")
            @PathVariable Long courseId) {

        return ResponseEntity.ok(
                reviewService.getCourseReviews(courseId)
        );
    }

    @PutMapping("/reviews/{reviewId}")
    @Operation(
            summary = "Update my review",
            description = "Updates a review owned by the authenticated student.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Review updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid review data"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Student role required or review access denied"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<ReviewResponse> updateReview(
            @Parameter(description = "ID of the review")
            @PathVariable Long reviewId,

            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ReviewRequest request) {

        return ResponseEntity.ok(
                reviewService.updateReview(
                        userDetails.getUsername(),
                        reviewId,
                        request
                )
        );
    }

    @DeleteMapping("/reviews/{reviewId}")
    @Operation(
            summary = "Delete my review",
            description = "Deletes a review owned by the authenticated student.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Student role required or review access denied"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "ID of the review")
            @PathVariable Long reviewId,

            @AuthenticationPrincipal UserDetails userDetails) {

        reviewService.deleteReview(
                userDetails.getUsername(),
                reviewId
        );

        return ResponseEntity.noContent().build();
    }
}
