
package com.example.LearningPlatformApp.service;

import com.example.LearningPlatformApp.dto.request.ChangePasswordRequest;
import com.example.LearningPlatformApp.dto.request.UpdateUserRequest;
import com.example.LearningPlatformApp.dto.request.UpdateUserRolesRequest;
import com.example.LearningPlatformApp.dto.request.UpdateUserStatusRequest;
import com.example.LearningPlatformApp.dto.response.UserResponse;
import com.example.LearningPlatformApp.entity.Role;
import com.example.LearningPlatformApp.entity.User;
import com.example.LearningPlatformApp.exception.BusinessRuleViolationException;
import com.example.LearningPlatformApp.exception.InvalidRequestException;
import com.example.LearningPlatformApp.exception.ResourceNotFoundException;
import com.example.LearningPlatformApp.repository.RoleRepository;
import com.example.LearningPlatformApp.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse updateCurrentUser(
            UserDetails userDetails,
            UpdateUserRequest request
    ) {
        User user = getUserByEmail(userDetails.getUsername());

        if (request.firstName() != null) {
            if (request.firstName().isBlank()) {
                throw new InvalidRequestException(
                        "First name cannot be blank"
                );
            }

            user.setFirstName(request.firstName().trim());
        }

        if (request.lastName() != null) {
            if (request.lastName().isBlank()) {
                throw new InvalidRequestException(
                        "Last name cannot be blank"
                );
            }

            user.setLastName(request.lastName().trim());
        }

        if (request.phone() != null) {
            user.setPhone(request.phone().trim());
        }

        if (request.bio() != null) {
            user.setBio(request.bio().trim());
        }

        if (request.profileImage() != null) {
            user.setProfileImage(request.profileImage().trim());
        }

        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(UserDetails userDetails) {

        User user = getUserByEmail(userDetails.getUsername());

        return toResponse(user);
    }

    @Transactional
    public void changePassword(
            UserDetails userDetails,
            ChangePasswordRequest request
    ) {
        User user = getUserByEmail(userDetails.getUsername());

        if (!passwordEncoder.matches(
                request.currentPassword(),
                user.getPassword()
        )) {
            throw new InvalidRequestException(
                    "Current password is incorrect"
            );
        }

        if (passwordEncoder.matches(
                request.newPassword(),
                user.getPassword()
        )) {
            throw new InvalidRequestException(
                    "New password must be different from current password"
            );
        }

        user.setPassword(
                passwordEncoder.encode(request.newPassword())
        );
    }

    @Transactional
    public UserResponse updateUserRoles(
            Long userId,
            UpdateUserRolesRequest request
    ) {
        User user = getUserById(userId);

        Set<String> roleNames = request.roles().stream()
                .map(role -> role.trim().toUpperCase(Locale.ROOT))
                .collect(Collectors.toSet());

        Set<Role> roles = new HashSet<>();

        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new InvalidRequestException(
                            "Role not found: " + roleName
                    ));

            roles.add(role);
        }

        boolean currentlyAdmin = user.getRoles().stream()
                .anyMatch(role -> "ADMIN".equalsIgnoreCase(role.getName()));

        boolean willRemainAdmin = roleNames.contains("ADMIN");

        if (currentlyAdmin
                && !willRemainAdmin
                && userRepository.countByRoles_Name("ADMIN") <= 1) {

            throw new BusinessRuleViolationException(
                    "Cannot remove the last ADMIN from the system"
            );
        }

        user.setRoles(roles);

        return toResponse(user);
    }

    @Transactional
    public UserResponse updateUserStatus(
            Long userId,
            UpdateUserStatusRequest request
    ) {
        User user = getUserById(userId);

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> "ADMIN".equalsIgnoreCase(role.getName()));

        if (isAdmin
                && Boolean.FALSE.equals(request.enabled())
                && userRepository.countByRoles_Name("ADMIN") <= 1) {

            throw new BusinessRuleViolationException(
                    "Cannot disable the last ADMIN in the system"
            );
        }

        user.setEnabled(request.enabled());

        return toResponse(user);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found"
                ));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found"
                ));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getBio(),
                user.getProfileImage(),
                Boolean.TRUE.equals(user.getEnabled()),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
