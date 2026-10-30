
package com.example.LearningPlatformApp.controller;

import com.example.LearningPlatformApp.dto.request.ChangePasswordRequest;
import com.example.LearningPlatformApp.dto.request.UpdateUserRequest;
import com.example.LearningPlatformApp.dto.request.UpdateUserRolesRequest;
import com.example.LearningPlatformApp.dto.request.UpdateUserStatusRequest;
import com.example.LearningPlatformApp.dto.response.UserResponse;
import com.example.LearningPlatformApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "Manage user profiles, passwords, roles, and account status")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/me")
    @Operation(
            summary = "Update current user's profile",
            description = "Updates the profile information of the authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<UserResponse> updateCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateUserRequest request) {

        return ResponseEntity.ok(
                userService.updateCurrentUser(userDetails, request)
        );
    }

    @GetMapping("/me")
    @Operation(
            summary = "Get current user's profile",
            description = "Returns the profile of the authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<UserResponse> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                userService.getCurrentUser(userDetails)
        );
    }

    @PatchMapping("/me/password")
    @Operation(
            summary = "Change current user's password",
            description = "Changes the authenticated user's password after validating the request.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or password change rejected"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequest request) {

        userService.changePassword(userDetails, request);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/roles")
    @Operation(
            summary = "Update a user's roles",
            description = "Replaces or updates the roles assigned to a user. Intended for administrators.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "ID of the user whose roles will be updated",
                            required = true,
                            in = ParameterIn.PATH
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User roles updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid role data"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Administrator privileges required"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> updateUserRoles(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRolesRequest request) {

        return ResponseEntity.ok(
                userService.updateUserRoles(userId, request)
        );
    }

    @PatchMapping("/{userId}/status")
    @Operation(
            summary = "Update a user's account status",
            description = "Updates whether a user account is enabled or disabled. Intended for administrators.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "ID of the user whose status will be updated",
                            required = true,
                            in = ParameterIn.PATH
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status data"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Administrator privileges required"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> updateUserStatus(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserStatusRequest request) {

        return ResponseEntity.ok(
                userService.updateUserStatus(userId, request)
        );
    }
}
