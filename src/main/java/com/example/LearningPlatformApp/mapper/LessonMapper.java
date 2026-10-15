package com.example.LearningPlatformApp.mapper;

import com.example.LearningPlatformApp.dto.request.LessonRequest;
import com.example.LearningPlatformApp.dto.response.LessonResponse;
import com.example.LearningPlatformApp.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)

    Lesson toEntity(LessonRequest request);

    @Mapping(target = "courseId", source = "course.id")
    LessonResponse toResponse(Lesson lesson);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(
            LessonRequest request,
            @MappingTarget Lesson lesson
    );
}