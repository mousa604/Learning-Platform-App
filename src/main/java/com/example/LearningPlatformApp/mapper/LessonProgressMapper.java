
package com.example.LearningPlatformApp.mapper;

import com.example.LearningPlatformApp.dto.response.LessonProgressResponse;
import com.example.LearningPlatformApp.entity.LessonProgress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonProgressMapper {

    @Mapping(target = "lessonId", source = "lesson.id")
    @Mapping(target = "lessonTitle", source = "lesson.title")
    LessonProgressResponse toResponse(LessonProgress progress);
}
