package com.example.LearningPlatformApp.service;

import com.example.LearningPlatformApp.dto.request.CourseRequest;
import com.example.LearningPlatformApp.dto.request.UpdateCourseRequest;
import com.example.LearningPlatformApp.dto.response.CoursePageResponse;
import com.example.LearningPlatformApp.dto.response.CourseResponse;
import com.example.LearningPlatformApp.entity.Category;
import com.example.LearningPlatformApp.entity.Course;
import com.example.LearningPlatformApp.entity.User;
import com.example.LearningPlatformApp.entity.enums.CourseLevel;
import com.example.LearningPlatformApp.entity.enums.CourseStatus;
import com.example.LearningPlatformApp.exception.BusinessRuleViolationException;
import com.example.LearningPlatformApp.exception.ResourceNotFoundException;
import com.example.LearningPlatformApp.mapper.CourseMapper;
import com.example.LearningPlatformApp.repository.CategoryRepository;
import com.example.LearningPlatformApp.repository.CourseRepository;
import com.example.LearningPlatformApp.repository.EnrollmentRepository;
import com.example.LearningPlatformApp.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private  final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;

    @Autowired
    public CourseService(
            CourseRepository courseRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository,
            CourseMapper courseMapper,
            EnrollmentRepository enrollmentRepository
    ) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.courseMapper = courseMapper;
        this.enrollmentRepository=enrollmentRepository;
    }

    @Transactional
    public CourseResponse createCourse(
            CourseRequest request,
            Long instructorId
    ) {
        Category category = categoryRepository
                .findById(request.categoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found"
                        )
                );

        User instructor = userRepository
                .findById(instructorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Instructor not found"
                        )
                );

        Course course = courseMapper.toEntity(request);

        course.setCategory(category);
        course.setInstructor(instructor);
        course.setStatus(CourseStatus.DRAFT);

        Course savedCourse = courseRepository.save(course);

        return courseMapper.toResponse(savedCourse);
    }

    @Transactional
    public CoursePageResponse getPublishedCourses(
            int page,
            int size
    ) {
        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page number must be zero or greater"
            );
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100"
            );
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Course> coursesPage =
                courseRepository.findByStatus(
                        CourseStatus.PUBLISHED,
                        pageable
                );

        List<CourseResponse> content =
                coursesPage.getContent()
                        .stream()
                        .map(courseMapper::toResponse)
                        .toList();

        return new CoursePageResponse(
                content,
                coursesPage.getNumber(),
                coursesPage.getSize(),
                coursesPage.getTotalElements(),
                coursesPage.getTotalPages(),
                coursesPage.isFirst(),
                coursesPage.isLast()
        );
    }

    @Transactional
    public CourseResponse getPublishedCourseById(Long id) {
        Course course = courseRepository
                .findByIdAndStatus(id, CourseStatus.PUBLISHED)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Published course not found"
                        )
                );

        return courseMapper.toResponse(course);
    }


    @Transactional
    public CourseResponse updateCourse(
            Long courseId,
            Long instructorId,
            UpdateCourseRequest request
    ) {
        Course course = courseRepository
                .findByIdAndInstructorId(courseId, instructorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found or you are not its instructor"
                        )
                );

        Category category = categoryRepository
                .findById(request.categoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found"
                        )
                );

        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setPrice(request.price());
        course.setLevel(request.level());
        course.setCategory(category);
        course.setThumbnail(request.thumbnail());

        return courseMapper.toResponse(course);
    }

    @Transactional
    public void archiveCourse(
            Long courseId,
            Long instructorId
    ) {
        Course course = courseRepository
                .findByIdAndInstructorId(courseId, instructorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found or you are not its instructor"
                        )
                );

        boolean hasEnrollments =
                enrollmentRepository.existsByCourseId(courseId);

        if (hasEnrollments) {
            throw new BusinessRuleViolationException(
                    "Cannot archive a course with existing enrollments"
            );
        }

        if (course.getStatus() == CourseStatus.ARCHIVED) {
            throw new BusinessRuleViolationException(
                    "Course is already archived"
            );
        }

        course.setStatus(CourseStatus.ARCHIVED);
    }



    @Transactional(readOnly = true)
    public CoursePageResponse searchPublishedCourses(
            String keyword,
            Long categoryId,
            CourseLevel level,
            int page,
            int size
    ) {
        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page number must be zero or greater"
            );
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100"
            );
        }

        if (categoryId != null && categoryId < 1) {
            throw new IllegalArgumentException(
                    "Category ID must be greater than zero"
            );
        }

        if (keyword != null) {
            keyword = keyword.trim();

            if (keyword.isBlank()) {
                keyword = null;
            }
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Course> coursesPage =
                courseRepository.searchPublishedCourses(
                        CourseStatus.PUBLISHED,
                        keyword,
                        categoryId,
                        level,
                        pageable
                );

        List<CourseResponse> content =
                coursesPage.getContent()
                        .stream()
                        .map(courseMapper::toResponse)
                        .toList();

        return new CoursePageResponse(
                content,
                coursesPage.getNumber(),
                coursesPage.getSize(),
                coursesPage.getTotalElements(),
                coursesPage.getTotalPages(),
                coursesPage.isFirst(),
                coursesPage.isLast()
        );
        }
}
