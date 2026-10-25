package com.example.LearningPlatformApp.controller;

import com.example.LearningPlatformApp.dto.request.ChangePasswordRequest;
import com.example.LearningPlatformApp.dto.request.UpdateUserRequest;
import com.example.LearningPlatformApp.dto.response.UserResponse;
import com.example.LearningPlatformApp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userProfileService){
        this.userService =userProfileService;
    }



    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateUserRequest request) {

        return ResponseEntity.ok(
                userService.updateCurrentUser(userDetails, request)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                userService.getCurrentUser(userDetails)
        );
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequest request) {

        userService.changePassword(userDetails, request);

        return ResponseEntity.noContent().build();
    }
}