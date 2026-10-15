package com.example.LearningPlatformApp.controller;

import com.example.LearningPlatformApp.dto.request.LessonRequest;
import com.example.LearningPlatformApp.dto.response.LessonResponse;
import com.example.LearningPlatformApp.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/lessons")
public class LessonController {

    private final LessonService lessonService;

    @Autowired
    public LessonController(LessonService lessonService){
        this.lessonService=lessonService;
    }

    // Create a lesson
    @PostMapping
    public ResponseEntity<LessonResponse> createLesson(
            @PathVariable Long courseId,
            @RequestParam Long instructorId,
            @Valid @RequestBody LessonRequest request
    ) {
        LessonResponse response = lessonService.createLesson(
                courseId,
                instructorId,
                request
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get all lessons for a course
    @GetMapping
    public ResponseEntity<List<LessonResponse>> getCourseLessons(
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(
                lessonService.getCourseLessons(courseId)
        );
    }

    // Update a lesson
    @PutMapping("/{lessonId}")
    public ResponseEntity<LessonResponse> updateLesson(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            @RequestParam Long instructorId,
            @Valid @RequestBody LessonRequest request
    ) {
        return ResponseEntity.ok(
                lessonService.updateLesson(
                        courseId,
                        lessonId,
                        instructorId,
                        request
                )
        );
    }

    // Delete a lesson
    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Void> deleteLesson(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            @RequestParam Long instructorId
    ) {
        lessonService.deleteLesson(
                courseId,
                lessonId,
                instructorId
        );

        return ResponseEntity.noContent().build();
    }
}