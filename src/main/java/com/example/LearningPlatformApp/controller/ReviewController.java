package com.example.LearningPlatformApp.controller;

import com.example.LearningPlatformApp.dto.request.ReviewRequest;
import com.example.LearningPlatformApp.dto.response.ReviewResponse;
import com.example.LearningPlatformApp.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public  ReviewController(ReviewService reviewService){
        this.reviewService=reviewService;
    }

    // Create a review
    @PostMapping("/courses/{courseId}/reviews")
    public ResponseEntity<ReviewResponse> createReview(
            @PathVariable Long courseId,
            @RequestParam Long studentId,
            @Valid @RequestBody ReviewRequest request
    ) {
        ReviewResponse response = reviewService.createReview(
                studentId,
                courseId,
                request
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get all reviews for a course
    @GetMapping("/courses/{courseId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getCourseReviews(
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(
                reviewService.getCourseReviews(courseId)
        );
    }

    // Update a review
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long reviewId,
            @RequestParam Long studentId,
            @Valid @RequestBody ReviewRequest request
    ) {
        return ResponseEntity.ok(
                reviewService.updateReview(
                        studentId,
                        reviewId,
                        request
                )
        );
    }

    // Delete a review
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @RequestParam Long studentId
    ) {
        reviewService.deleteReview(studentId, reviewId);

        return ResponseEntity.noContent().build();
    }
}