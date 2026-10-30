
package com.example.LearningPlatformApp.service;

import com.example.LearningPlatformApp.dto.response.TagResponse;
import com.example.LearningPlatformApp.entity.Tag;
import com.example.LearningPlatformApp.exception.BusinessRuleViolationException;
import com.example.LearningPlatformApp.exception.InvalidRequestException;
import com.example.LearningPlatformApp.exception.ResourceNotFoundException;
import com.example.LearningPlatformApp.mapper.CourseMapper;
import com.example.LearningPlatformApp.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class TagService {

    private final TagRepository tagRepository;
    private final CourseMapper courseMapper;

    public TagService(
            TagRepository tagRepository,
            CourseMapper courseMapper
    ) {
        this.tagRepository = tagRepository;
        this.courseMapper = courseMapper;
    }

    @Transactional
    public TagResponse createTag(String name) {
        String normalizedName = normalizeName(name);

        if (tagRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new BusinessRuleViolationException(
                    "Tag already exists: " + normalizedName
            );
        }

        Tag tag = new Tag(normalizedName);
        Tag savedTag = tagRepository.save(tag);

        return courseMapper.toTagResponse(savedTag);
    }

    @Transactional(readOnly = true)
    public List<TagResponse> getAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(courseMapper::toTagResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TagResponse getTagById(Long id) {
        return courseMapper.toTagResponse(findTagById(id));
    }

    @Transactional
    public TagResponse updateTag(Long id, String name) {
        String normalizedName = normalizeName(name);
        Tag tag = findTagById(id);

        if (!tag.getName().equalsIgnoreCase(normalizedName)
                && tagRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new BusinessRuleViolationException(
                    "Tag already exists: " + normalizedName
            );
        }

        tag.setName(normalizedName);

        return courseMapper.toTagResponse(tag);
    }

    @Transactional
    public void deleteTag(Long id) {
        Tag tag = findTagById(id);
        tagRepository.delete(tag);
    }

    @Transactional(readOnly = true)
    public List<TagResponse> searchTags(String keyword) {
        String normalizedKeyword = normalizeName(keyword);

        return tagRepository
                .findByNameContainingIgnoreCase(normalizedKeyword)
                .stream()
                .map(courseMapper::toTagResponse)
                .toList();
    }

    private Tag findTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Tag not found with id: " + id
                        )
                );
    }

    private String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidRequestException(
                    "Tag name must not be blank"
            );
        }

        String normalizedName = name.trim();

        if (normalizedName.length() > 50) {
            throw new InvalidRequestException(
                    "Tag name must not exceed 50 characters"
            );
        }

        return normalizedName;
    }
}