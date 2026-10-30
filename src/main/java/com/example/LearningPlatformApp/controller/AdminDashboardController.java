
package com.example.LearningPlatformApp.controller;

import com.example.LearningPlatformApp.dto.response.AdminDashboardResponse;
import com.example.LearningPlatformApp.service.AdminDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
@Tag(name = "Admin - Dashboard", description = "Administrative dashboard and platform statistics")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @Autowired
    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    @GetMapping
    @Operation(
            summary = "Get admin dashboard statistics",
            description = "Returns summary statistics about the learning platform for administrators.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dashboard statistics retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Administrator privileges required")
    })
    public ResponseEntity<AdminDashboardResponse> getDashboardStatistics() {

        return ResponseEntity.ok(
                adminDashboardService.getDashboardStatistics()
        );
    }
}
