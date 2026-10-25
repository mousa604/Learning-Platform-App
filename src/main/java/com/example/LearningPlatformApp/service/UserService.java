package com.example.LearningPlatformApp.service;

import com.example.LearningPlatformApp.dto.request.ChangePasswordRequest;
import com.example.LearningPlatformApp.dto.request.UpdateUserRequest;
import com.example.LearningPlatformApp.dto.response.UserResponse;
import com.example.LearningPlatformApp.entity.User;
import com.example.LearningPlatformApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }




    @Transactional
    public UserResponse updateCurrentUser(
            UserDetails userDetails,
            UpdateUserRequest request) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        if (request.firstName() != null) {
            if (request.firstName().isBlank()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "First name cannot be blank"
                );
            }

            user.setFirstName(request.firstName().trim());
        }

        if (request.lastName() != null) {
            if (request.lastName().isBlank()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
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

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new IllegalStateException("Authenticated user not found"));

        return toResponse(user);
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


    @Transactional
    public void changePassword(
            UserDetails userDetails,
            ChangePasswordRequest request) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User not found"
                        ));

        if (!passwordEncoder.matches(
                request.currentPassword(),
                user.getPassword())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Current password is incorrect"
            );
        }

        if (passwordEncoder.matches(
                request.newPassword(),
                user.getPassword())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "New password must be different from current password"
            );
        }

        user.setPassword(
                passwordEncoder.encode(request.newPassword())
        );
    }

}