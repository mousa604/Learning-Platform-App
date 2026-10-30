package com.example.LearningPlatformApp.controller;

import com.example.LearningPlatformApp.dto.request.TagRequest;
import com.example.LearningPlatformApp.dto.response.TagResponse;
import com.example.LearningPlatformApp.service.TagService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
@Tag(
        name = "Tags",
        description = "Endpoints for managing and searching course tags"
)
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    @Operation(
            summary = "Create a new tag",
            description = "Creates a new tag"
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tag created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Authentication is required or token is invalid"),
            @ApiResponse(responseCode = "403", description = "User does not have permission"),
            @ApiResponse(responseCode = "409", description = "Tag already exists")
    })
    public ResponseEntity<TagResponse> createTag(
            @Valid @RequestBody TagRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tagService.createTag(request.name()));
    }

    @GetMapping
    @Operation(
            summary = "Get all tags or search tags",
            description = "Returns all tags when keyword is absent, or searches tags by keyword"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tags retrieved successfully")
    })
    public ResponseEntity<List<TagResponse>> getTags(
            @Parameter(description = "Optional keyword to search tags", example = "Java")
            @RequestParam(required = false) String keyword
    ) {
        if (keyword == null || keyword.isBlank()) {
            return ResponseEntity.ok(tagService.getAllTags());
        }

        return ResponseEntity.ok(tagService.searchTags(keyword));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get tag by ID",
            description = "Returns a specific tag by its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tag retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    public ResponseEntity<TagResponse> getTagById(
            @Parameter(description = "Tag ID", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update tag",
            description = "Updates an existing tag"
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tag updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Authentication is required or token is invalid"),
            @ApiResponse(responseCode = "403", description = "User does not have permission"),
            @ApiResponse(responseCode = "404", description = "Tag not found"),
            @ApiResponse(responseCode = "409", description = "Tag already exists")
    })
    public ResponseEntity<TagResponse> updateTag(
            @Parameter(description = "Tag ID", example = "1")
            @PathVariable Long id,

            @Valid @RequestBody TagRequest request
    ) {
        return ResponseEntity.ok(
                tagService.updateTag(id, request.name())
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete tag",
            description = "Deletes an existing tag"
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tag deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication is required or token is invalid"),
            @ApiResponse(responseCode = "403", description = "User does not have permission"),
            @ApiResponse(responseCode = "404", description = "Tag not found"),
            @ApiResponse(responseCode = "409", description = "Tag cannot be deleted")
    })
    public ResponseEntity<Void> deleteTag(
            @Parameter(description = "Tag ID", example = "1")
            @PathVariable Long id
    ) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}