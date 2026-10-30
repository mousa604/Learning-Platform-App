
package com.example.LearningPlatformApp.controller;

import com.example.LearningPlatformApp.dto.response.CourseStatisticsResponse;
import com.example.LearningPlatformApp.service.CourseStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
@Tag(name = "Course Statistics", description = "Statistics and analytics for individual courses")
public class CourseStatisticsController {

    private final CourseStatisticsService courseStatisticsService;

    @Autowired
    public CourseStatisticsController(CourseStatisticsService courseStatisticsService) {
        this.courseStatisticsService = courseStatisticsService;
    }

    @GetMapping("/{courseId}/statistics")
    @Operation(
            summary = "Get course statistics",
            description = "Returns statistics for a specific course. Access requires authentication and the appropriate permissions.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "courseId",
                            description = "ID of the course",
                            required = true,
                            in = ParameterIn.PATH
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course statistics retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to view course statistics"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<CourseStatisticsResponse> getCourseStatistics(
            @PathVariable Long courseId) {

        return ResponseEntity.ok(
                courseStatisticsService.getCourseStatistics(courseId)
        );
    }
}
