
package com.example.LearningPlatformApp.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateUserRequest(

        @Size(max = 50,
                message = "First name must not exceed 50 characters")
        String firstName,

        @Size(max = 50,
                message = "Last name must not exceed 50 characters")
        String lastName,

        @Size(max = 20,
                message = "Phone must not exceed 20 characters")
        String phone,

        @Size(max = 1000,
                message = "Bio must not exceed 1000 characters")
        String bio,

        @Size(max = 255,
                message = "Profile image URL must not exceed 255 characters")
        String profileImage

) {}
