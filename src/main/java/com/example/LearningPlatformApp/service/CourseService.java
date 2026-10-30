package com.example.LearningPlatformApp.service;

import com.example.LearningPlatformApp.dto.request.CourseRejectionRequest;
import com.example.LearningPlatformApp.dto.request.CourseRequest;
import com.example.LearningPlatformApp.dto.request.CourseTagsRequest;
import com.example.LearningPlatformApp.dto.request.UpdateCourseRequest;
import com.example.LearningPlatformApp.dto.response.CoursePageResponse;
import com.example.LearningPlatformApp.dto.response.CourseResponse;
import com.example.LearningPlatformApp.dto.response.PendingCourseResponse;
import com.example.LearningPlatformApp.entity.Category;
import com.example.LearningPlatformApp.entity.Course;
import com.example.LearningPlatformApp.entity.Tag;
import com.example.LearningPlatformApp.entity.User;
import com.example.LearningPlatformApp.entity.enums.CourseLevel;
import com.example.LearningPlatformApp.entity.enums.CourseStatus;
import com.example.LearningPlatformApp.entity.enums.NotificationType;
import com.example.LearningPlatformApp.exception.BusinessRuleViolationException;
import com.example.LearningPlatformApp.exception.InvalidRequestException;
import com.example.LearningPlatformApp.exception.ResourceNotFoundException;
import com.example.LearningPlatformApp.mapper.CourseMapper;
import com.example.LearningPlatformApp.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class CourseService {
    private static final Logger log =
            LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private  final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;

    private final LessonRepository lessonRepository;

    private final TagRepository tagRepository;
    private final NotificationService notificationService;

    @Autowired
    public CourseService(
            CourseRepository courseRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository,
            CourseMapper courseMapper,
            EnrollmentRepository enrollmentRepository,
            LessonRepository lessonRepository,
            TagRepository tagRepository,
            NotificationService notificationService
    ) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.courseMapper = courseMapper;
        this.enrollmentRepository=enrollmentRepository;
        this.lessonRepository=lessonRepository;
        this.tagRepository=tagRepository;
        this.notificationService=notificationService;
    }

    private User getInstructorByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Instructor not found")
                );
    }

    @Transactional
    public CourseResponse createCourse(
            CourseRequest request,
            String instructorEmail) {

        Category category = categoryRepository
                .findById(request.categoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found")
                );

        User instructor = getInstructorByEmail(instructorEmail);

        Course course = courseMapper.toEntity(request);
        course.setCategory(category);
        course.setInstructor(instructor);
        course.setStatus(CourseStatus.DRAFT);

        Course savedCourse = courseRepository.save(course);
        log.info(
                "Course created successfully. courseId={}, instructorId={}",
                savedCourse.getId(),
                instructor.getId()
        );

        return courseMapper.toResponse(savedCourse);
    }

    @Transactional
    public CoursePageResponse getPublishedCourses(
            int page,
            int size
    ) {
        validatePagination(page,size);

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
            String instructorEmail,
            UpdateCourseRequest request) {

        User instructor = getInstructorByEmail(instructorEmail);

        Course course = courseRepository
                .findByIdAndInstructorId(courseId, instructor.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found or you are not its instructor"
                        )
                );

        Category category = categoryRepository
                .findById(request.categoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found")
                );

        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setPrice(request.price());
        course.setLevel(request.level());
        course.setCategory(category);
        course.setThumbnail(request.thumbnail());

        course = courseRepository.saveAndFlush(course);
        log.info(
                "Course updated successfully. courseId={}, instructorId={}",
                course.getId(),
                instructor.getId()
        );

        return courseMapper.toResponse(course);
    }

    @Transactional
    public void archiveCourse(Long courseId, String instructorEmail) {
        User instructor = getInstructorByEmail(instructorEmail);

        Course course = courseRepository
                .findByIdAndInstructorId(courseId, instructor.getId())
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
        log.info(
                "Course archived successfully. courseId={}, instructorId={}",
                course.getId(),
                instructor.getId()
        );
    }



    @Transactional(readOnly = true)
    public CoursePageResponse searchPublishedCourses(
            String keyword,
            Long categoryId,
            CourseLevel level,
            int page,
            int size
    ) {
        validatePagination(page,size);

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
    @Transactional
    public CourseResponse submitCourseForReview(
            Long courseId,
            String instructorEmail) {

        User instructor = getInstructorByEmail(instructorEmail);

        Course course = courseRepository
                .findByIdAndInstructorId(courseId, instructor.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found or you are not the owner"
                        )
                );

        if (course.getStatus() != CourseStatus.DRAFT
                && course.getStatus() != CourseStatus.REJECTED) {

            throw new BusinessRuleViolationException(
                    "Only draft or rejected courses can be submitted for review"
            );
        }

        long lessonsCount =
                lessonRepository.countByCourseId(courseId);

        if (lessonsCount == 0) {
            throw new BusinessRuleViolationException(
                    "Course must contain at least one lesson before submission"
            );
        }

        course.setStatus(CourseStatus.PENDING_REVIEW);

        Course savedCourse = courseRepository.save(course);
        log.info(
                "Course submitted for review. courseId={}, instructorId={}",
                savedCourse.getId(),
                instructor.getId()
        );

        return courseMapper.toResponse(savedCourse);
    }

    @Transactional
    public CourseResponse approveCourse(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found with id: " + courseId
                        )
                );

        if (course.getStatus() != CourseStatus.PENDING_REVIEW) {
            throw new BusinessRuleViolationException(
                    "Only courses pending review can be approved"
            );
        }

        course.setStatus(CourseStatus.PUBLISHED);
        course.setPublishedAt(LocalDateTime.now());
        course.setRejectionReason(null);

        Course savedCourse = courseRepository.save(course);

        // Notify the instructor
        notificationService.createNotification(
                savedCourse.getInstructor(),
                "Course Published!",
                "Congratulations! Your course: "
                        + savedCourse.getTitle()
                        + " has been approved and published successfully.",
                NotificationType.COURSE_PUBLISHED
        );

        log.info(
                "Course approved successfully. courseId={}",
                savedCourse.getId()
        );

        return courseMapper.toResponse(savedCourse);
    }


    @Transactional
    public CourseResponse rejectCourse(
            Long courseId,
            CourseRejectionRequest request
    ) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found with id: " + courseId
                        )
                );

        if (course.getStatus() != CourseStatus.PENDING_REVIEW) {
            throw new BusinessRuleViolationException(
                    "Only courses pending review can be rejected"
            );
        }

        String rejectionReason = request.reason().trim();

        course.setStatus(CourseStatus.REJECTED);
        course.setRejectionReason(rejectionReason);
        course.setPublishedAt(null);

        Course savedCourse = courseRepository.save(course);

        // Notify the instructor
        notificationService.createNotification(
                savedCourse.getInstructor(),
                "Course Rejected",
                "Your course: " + savedCourse.getTitle()
                        + " has been rejected. Reason: "
                        + rejectionReason,
                NotificationType.COURSE_REJECTED
        );

        log.info(
                "Course rejected. courseId={}, instructorId={}",
                savedCourse.getId(),
                savedCourse.getInstructor().getId()
        );

        return courseMapper.toResponse(savedCourse);
    }


    @Transactional(readOnly = true)
    public Page<PendingCourseResponse> getPendingCourses(
            int page,
            int size) {

        validatePagination(page,size);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.ASC, "createdAt")
        );

        Page<Course> courses = courseRepository.findByStatus(
                CourseStatus.PENDING_REVIEW,
                pageable
        );

        return courses.map(course ->
                new PendingCourseResponse(
                        course.getId(),
                        course.getTitle(),
                        course.getStatus(),
                        course.getInstructor().getId(),
                        course.getInstructor().getFirstName()
                                + " "
                                + course.getInstructor().getLastName(),
                        course.getCreatedAt()
                )
        );
    }


    @Transactional
    public CourseResponse assignTagsToCourse(
            Long courseId,
            String instructorEmail,
            CourseTagsRequest request
    ) {
        User instructor = getInstructorByEmail(instructorEmail);

        Course course = courseRepository
                .findByIdAndInstructorId(courseId, instructor.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found or you are not its instructor"
                        )
                );

        Set<Long> tagIds = new LinkedHashSet<>(request.tagIds());

        List<Tag> tags = tagRepository.findAllById(tagIds);

        if (tags.size() != tagIds.size()) {
            throw new ResourceNotFoundException(
                    "One or more tags were not found"
            );
        }

        course.setTags(new LinkedHashSet<>(tags));

        return courseMapper.toResponse(course);
    }


    private void validatePagination(int page, int size) {
        if (page < 0) {
            throw new InvalidRequestException(
                    "Page number must be zero or greater"
            );
        }

        if (size < 1 || size > 100) {
            throw new InvalidRequestException(
                    "Page size must be between 1 and 100"
            );
        }
    }
}
