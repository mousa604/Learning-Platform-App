
package com.example.LearningPlatformApp.controller;

import com.example.LearningPlatformApp.dto.request.CourseRequest;
import com.example.LearningPlatformApp.dto.request.UpdateCourseRequest;
import com.example.LearningPlatformApp.dto.response.CoursePageResponse;
import com.example.LearningPlatformApp.dto.response.CourseResponse;
import com.example.LearningPlatformApp.entity.enums.CourseLevel;
import com.example.LearningPlatformApp.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("")
    public ResponseEntity<CourseResponse> createCourse(
            @Valid @RequestBody CourseRequest request
    ) {
        // Temporary instructor ID for development before Security
        Long instructorId = 2L;

        CourseResponse response =
                courseService.createCourse(request, instructorId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("")
    public ResponseEntity<CoursePageResponse> getPublishedCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        CoursePageResponse response =
                courseService.getPublishedCourses(page, size);

        return ResponseEntity.ok(response);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<CourseResponse> getPublishedCourseById(
//            @PathVariable Long id
//    ) {
//        return ResponseEntity.ok(
//                courseService.getPublishedCourseById(id)
//        );
//    }


    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable Long id,
            @RequestParam Long instructorId,
            @Valid @RequestBody UpdateCourseRequest request
    ) {
        CourseResponse response = courseService.updateCourse(
                id,
                instructorId,
                request
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> archiveCourse(
            @PathVariable Long id,
            @RequestParam Long instructorId
    ) {
        courseService.archiveCourse(id, instructorId);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/search")
    public ResponseEntity<CoursePageResponse> searchPublishedCourses(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) CourseLevel level,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
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
}
