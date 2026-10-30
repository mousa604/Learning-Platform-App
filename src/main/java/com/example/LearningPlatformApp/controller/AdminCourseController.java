
package com.example.LearningPlatformApp.controller;

import com.example.LearningPlatformApp.dto.response.PendingCourseResponse;
import com.example.LearningPlatformApp.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/courses")
@Tag(name = "Admin - Courses", description = "Administrative operations for managing courses")
public class AdminCourseController {

    private final CourseService courseService;

    @Autowired
    public AdminCourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/pending")
    @Operation(
            summary = "Get pending courses",
            description = "Returns a paginated list of courses waiting for administrator review.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "page",
                            description = "Zero-based page number",
                            in = ParameterIn.QUERY
                    ),
                    @Parameter(
                            name = "size",
                            description = "Number of courses per page",
                            in = ParameterIn.QUERY
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pending courses retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Administrator privileges required")
    })
    public ResponseEntity<Page<PendingCourseResponse>> getPendingCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                courseService.getPendingCourses(page, size)
        );
    }
}
