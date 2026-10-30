
package com.example.LearningPlatformApp.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record UpdateUserRolesRequest(
        @NotEmpty(message = "At least one role is required")
        Set<
                @NotBlank(message = "Role name cannot be blank")
                        String
                > roles
) {
}
