package com.example.LearningPlatformApp.controller;

import com.example.LearningPlatformApp.dto.response.PendingCourseResponse;
import com.example.LearningPlatformApp.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/courses")
public class AdminCourseController {

    private final CourseService courseService;

    @Autowired
    public AdminCourseController(CourseService courseService){
        this.courseService=courseService;

    }

    @GetMapping("/pending")
    public ResponseEntity<Page<PendingCourseResponse>> getPendingCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                courseService.getPendingCourses(page, size)
        );
    }
}