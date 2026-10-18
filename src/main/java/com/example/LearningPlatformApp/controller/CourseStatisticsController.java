package com.example.LearningPlatformApp.controller;

import com.example.LearningPlatformApp.dto.response.CourseStatisticsResponse;
import com.example.LearningPlatformApp.service.CourseStatisticsService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseStatisticsController {

    private final CourseStatisticsService courseStatisticsService;

    @Autowired
    public CourseStatisticsController(CourseStatisticsService courseStatisticsService){
        this.courseStatisticsService=courseStatisticsService;
    }

    @GetMapping("/{courseId}/statistics")
    public ResponseEntity<CourseStatisticsResponse> getCourseStatistics(
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(
                courseStatisticsService.getCourseStatistics(courseId)
        );
    }
}