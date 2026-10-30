
package com.example.LearningPlatformApp.service;

import com.example.LearningPlatformApp.dto.request.ReviewRequest;
import com.example.LearningPlatformApp.dto.response.ReviewResponse;
import com.example.LearningPlatformApp.entity.Course;
import com.example.LearningPlatformApp.entity.Review;
import com.example.LearningPlatformApp.entity.User;
import com.example.LearningPlatformApp.entity.enums.CourseStatus;
import com.example.LearningPlatformApp.entity.enums.EnrollmentStatus;
import com.example.LearningPlatformApp.entity.enums.NotificationType;
import com.example.LearningPlatformApp.exception.BusinessRuleViolationException;
import com.example.LearningPlatformApp.exception.ResourceNotFoundException;
import com.example.LearningPlatformApp.mapper.ReviewMapper;
import com.example.LearningPlatformApp.repository.CourseRepository;
import com.example.LearningPlatformApp.repository.EnrollmentRepository;
import com.example.LearningPlatformApp.repository.ReviewRepository;
import com.example.LearningPlatformApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ReviewMapper reviewMapper;
    private final NotificationService notificationService;

    public ReviewService(ReviewRepository reviewRepository,
                         EnrollmentRepository enrollmentRepository,
                         UserRepository userRepository,
                         CourseRepository courseRepository,
                         ReviewMapper reviewMapper,
                         NotificationService notificationService){
        this.reviewRepository=reviewRepository;
        this.enrollmentRepository=enrollmentRepository;
        this.courseRepository=courseRepository;
        this.userRepository=userRepository;
        this.reviewMapper=reviewMapper;
        this.notificationService=notificationService;
    }

    @Transactional
    public ReviewResponse createReview(
            String studentEmail,
            Long courseId,
            ReviewRequest request
    ) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found"
                ));

        Course course = courseRepository
                .findByIdAndStatus(courseId, CourseStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Published course not found with id: " + courseId
                ));

        Long studentId = student.getId();

        boolean enrolled = enrollmentRepository
                .existsByStudentIdAndCourseIdAndStatusIn(
                        studentId,
                        courseId,
                        List.of(
                                EnrollmentStatus.ACTIVE,
                                EnrollmentStatus.COMPLETED
                        )
                );

        if (!enrolled) {
            throw new BusinessRuleViolationException(
                    "You must be enrolled in the course to review it"
            );
        }

        if (reviewRepository.existsByStudentIdAndCourseId(
                studentId, courseId
        )) {
            throw new BusinessRuleViolationException(
                    "You have already reviewed this course"
            );
        }

        Review review = reviewMapper.toEntity(request);
        review.setStudent(student);
        review.setCourse(course);

        Review savedReview = reviewRepository.save(review);

        notificationService.createNotification(
                course.getInstructor(),
                "New Course Review!",
                student.getFirstName() + " " + student.getLastName()
                        + " has added a review to your course: "
                        + course.getTitle(),
                NotificationType.NEW_REVIEW
        );

        return reviewMapper.toResponse(savedReview);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getCourseReviews(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException(
                    "Course not found with id: " + courseId
            );
        }

        return reviewRepository
                .findByCourseIdOrderByCreatedAtDesc(courseId)
                .stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    @Transactional
    public ReviewResponse updateReview(
            String studentEmail,
            Long reviewId,
            ReviewRequest request
    ) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found"
                ));

        Review review = reviewRepository
                .findByIdAndStudentId(reviewId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Review not found with id: " + reviewId
                ));

        reviewMapper.updateEntity(request, review);

        return reviewMapper.toResponse(review);
    }

    @Transactional
    public void deleteReview(String studentEmail, Long reviewId) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found"
                ));

        Review review = reviewRepository
                .findByIdAndStudentId(reviewId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Review not found with id: " + reviewId
                ));

        reviewRepository.delete(review);
    }
}
