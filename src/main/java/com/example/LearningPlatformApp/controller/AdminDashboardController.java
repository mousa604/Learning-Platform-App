package com.example.LearningPlatformApp.controller;

import com.example.LearningPlatformApp.dto.response.AdminDashboardResponse;
import com.example.LearningPlatformApp.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/dashboard")

public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @Autowired
    public AdminDashboardController(AdminDashboardService adminDashboardService){
        this.adminDashboardService=adminDashboardService;
    }

    @GetMapping
    public ResponseEntity<AdminDashboardResponse> getDashboardStatistics() {

        return ResponseEntity.ok(
                adminDashboardService.getDashboardStatistics()
        );
    }
}