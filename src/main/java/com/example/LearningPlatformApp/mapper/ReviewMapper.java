package com.example.LearningPlatformApp.mapper;

import com.example.LearningPlatformApp.dto.request.ReviewRequest;
import com.example.LearningPlatformApp.dto.response.ReviewResponse;
import com.example.LearningPlatformApp.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "course", ignore = true)

    Review toEntity(ReviewRequest request);

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(
            target = "studentName",
            expression = "java(review.getStudent().getFirstName() + \" \" + review.getStudent().getLastName())"
    )
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "courseTitle", source = "course.title")
    ReviewResponse toResponse(Review review);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(
            ReviewRequest request,
            @MappingTarget Review review
    );
}