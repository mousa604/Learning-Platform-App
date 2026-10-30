package com.example.LearningPlatformApp.controller;

import com.example.LearningPlatformApp.dto.response.NotificationResponse;
import com.example.LearningPlatformApp.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@Validated
@Tag(
        name = "Notifications",
        description = "Endpoints for managing user notifications"
)
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService
    ) {
        this.notificationService = notificationService;
    }

    @GetMapping
    @Operation(
            summary = "Get my notifications",
            description = "Returns the authenticated user's notifications with pagination"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication is required or token is invalid")
    })
    public ResponseEntity<Page<NotificationResponse>> getMyNotifications(
            @AuthenticationPrincipal UserDetails userDetails,

            @Parameter(description = "Page number, starting from zero", example = "0")
            @RequestParam(defaultValue = "0")
            @Min(0) int page,

            @Parameter(description = "Number of notifications per page (1-100)", example = "10")
            @RequestParam(defaultValue = "10")
            @Min(1) @Max(100) int size
    ) {
        return ResponseEntity.ok(
                notificationService.getMyNotifications(
                        userDetails.getUsername(),
                        page,
                        size
                )
        );
    }

    @GetMapping("/unread-count")
    @Operation(
            summary = "Get unread notifications count",
            description = "Returns the number of unread notifications for the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Unread count retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication is required or token is invalid")
    })
    public ResponseEntity<Long> getUnreadCount(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(
                notificationService.getUnreadCount(
                        userDetails.getUsername()
                )
        );
    }

    @PatchMapping("/{id}/read")
    @Operation(
            summary = "Mark notification as read",
            description = "Marks a specific notification as read for the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notification marked as read successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication is required or token is invalid"),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    public ResponseEntity<NotificationResponse> markAsRead(
            @Parameter(description = "Notification ID", example = "15")
            @PathVariable Long id,

            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(
                notificationService.markAsRead(
                        userDetails.getUsername(),
                        id
                )
        );
    }

    @PatchMapping("/read-all")
    @Operation(
            summary = "Mark all notifications as read",
            description = "Marks all unread notifications of the authenticated user as read"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notifications updated successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication is required or token is invalid")
    })
    public ResponseEntity<Integer> markAllAsRead(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(
                notificationService.markAllAsRead(
                        userDetails.getUsername()
                )
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete notification",
            description = "Deletes a specific notification belonging to the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Notification deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication is required or token is invalid"),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    public ResponseEntity<Void> deleteNotification(
            @Parameter(description = "Notification ID", example = "15")
            @PathVariable Long id,

            @AuthenticationPrincipal UserDetails userDetails
    ) {
        notificationService.deleteNotification(
                userDetails.getUsername(),
                id
        );

        return ResponseEntity.noContent().build();
    }
}