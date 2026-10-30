package com.example.LearningPlatformApp.service;

import com.example.LearningPlatformApp.dto.response.NotificationResponse;
import com.example.LearningPlatformApp.entity.Notification;
import com.example.LearningPlatformApp.entity.User;
import com.example.LearningPlatformApp.entity.enums.NotificationType;
import com.example.LearningPlatformApp.exception.ResourceNotFoundException;
import com.example.LearningPlatformApp.mapper.NotificationMapper;
import com.example.LearningPlatformApp.repository.NotificationRepository;
import com.example.LearningPlatformApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getMyNotifications(
            String userEmail,
            int page,
            int size
    ) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Notification> notifications =
                notificationRepository.findByUserIdOrderByCreatedAtDesc(
                        user.getId(),
                        pageable
                );

        return notifications.map(notificationMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return notificationRepository.countByUserIdAndReadFalse(
                user.getId()
        );
    }

    @Transactional
    public NotificationResponse markAsRead(
            String userEmail,
            Long notificationId
    ) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Notification notification =
                notificationRepository.findByIdAndUserId(
                        notificationId,
                        user.getId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException("Notification not found"));

        if (!notification.isRead()) {
            notification.setRead(true);
            notification.setReadAt(LocalDateTime.now());
        }

        Notification savedNotification =
                notificationRepository.save(notification);

        return notificationMapper.toResponse(savedNotification);
    }

    @Transactional
    public void createNotification(
            User recipient,
            String title,
            String message,
            NotificationType type
    ) {

        Notification notification = Notification.builder()
                .user(recipient)
                .title(title)
                .message(message)
                .type(type)
                .build();

        notificationRepository.save(notification);
    }

    @Transactional
    public int markAllAsRead(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        LocalDateTime now = LocalDateTime.now();

        return notificationRepository.markAllAsReadByUserId(
                user.getId(),
                now,
                now
        );

    }

    @Transactional
    public void deleteNotification(
            String userEmail,
            Long notificationId
    ) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        long deletedRows = notificationRepository.deleteByIdAndUserId(
                notificationId,
                user.getId()
        );

        if (deletedRows == 0) {
            throw new ResourceNotFoundException("Notification not found");
        }
    }
}