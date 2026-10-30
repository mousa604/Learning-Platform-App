package com.example.LearningPlatformApp.mapper;

import com.example.LearningPlatformApp.dto.response.CertificateResponse;
import com.example.LearningPlatformApp.entity.Certificate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CertificateMapper {

    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "courseTitle", source = "course.title")
    CertificateResponse toResponse(Certificate certificate);
}