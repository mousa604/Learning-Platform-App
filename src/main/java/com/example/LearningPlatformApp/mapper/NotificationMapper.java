package com.example.LearningPlatformApp.mapper;

import com.example.LearningPlatformApp.dto.response.NotificationResponse;
import com.example.LearningPlatformApp.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationResponse toResponse(Notification notification);
}