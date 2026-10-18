package com.example.LearningPlatformApp.mapper;

import com.example.LearningPlatformApp.dto.response.EnrollmentResponse;
import com.example.LearningPlatformApp.entity.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(
            target = "studentName",
            expression = "java(enrollment.getStudent().getFirstName() + \" \" + enrollment.getStudent().getLastName())"
    )
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "courseTitle", source = "course.title")
    EnrollmentResponse toResponse(Enrollment enrollment);

}