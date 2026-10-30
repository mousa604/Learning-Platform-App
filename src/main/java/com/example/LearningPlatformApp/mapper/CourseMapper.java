
package com.example.LearningPlatformApp.mapper;

import com.example.LearningPlatformApp.dto.request.CourseRequest;
import com.example.LearningPlatformApp.dto.response.CourseResponse;
import com.example.LearningPlatformApp.dto.response.TagResponse;
import com.example.LearningPlatformApp.entity.Course;
import com.example.LearningPlatformApp.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "instructor", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "tags", ignore = true)
    Course toEntity(CourseRequest request);

    @Mapping(target = "instructorId", source = "instructor.id")
    @Mapping(
            target = "instructorName",
            expression = "java(course.getInstructor().getFirstName() + \" \" + course.getInstructor().getLastName())"
    )
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "tags", source = "tags")
    CourseResponse toResponse(Course course);

    TagResponse toTagResponse(Tag tag);

    List<TagResponse> toTagResponseList(List<Tag> tags);
}
