
package com.example.LearningPlatformApp.controller;

import com.example.LearningPlatformApp.dto.response.CertificateResponse;
import com.example.LearningPlatformApp.service.CertificateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Certificates", description = "Issue, verify, and download course certificates")
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @PostMapping("/courses/{courseId}/certificates")
    @Operation(
            summary = "Issue a certificate",
            description = "Issues a certificate for the authenticated student after completing a course.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Certificate issued successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Student role required"),
            @ApiResponse(responseCode = "404", description = "Student or course not found"),
            @ApiResponse(responseCode = "409", description = "Course not completed or certificate already exists")
    })
    public ResponseEntity<CertificateResponse> issueCertificate(
            @Parameter(description = "Course ID", example = "1")
            @PathVariable Long courseId,

            @AuthenticationPrincipal UserDetails userDetails
    ) {
        CertificateResponse response = certificateService.issueCertificate(
                userDetails.getUsername(),
                courseId
        );

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/certificates/my-certificates")
    @Operation(
            summary = "Get my certificates",
            description = "Returns all certificates belonging to the authenticated student.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Certificates retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Student role required")
    })
    public ResponseEntity<List<CertificateResponse>> getMyCertificates(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<CertificateResponse> certificates =
                certificateService.getMyCertificates(userDetails.getUsername());

        return ResponseEntity.ok(certificates);
    }

    @GetMapping("/certificates/verify/{certificateNumber}")
    @Operation(
            summary = "Verify a certificate",
            description = "Publicly verifies a certificate using its unique certificate number."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Certificate verified successfully"),
            @ApiResponse(responseCode = "404", description = "Certificate not found")
    })
    public ResponseEntity<CertificateResponse> verifyCertificate(
            @Parameter(description = "Unique certificate number", example = "a1b2c3d4")
            @PathVariable String certificateNumber
    ) {
        CertificateResponse response =
                certificateService.verifyCertificate(certificateNumber);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/certificates/{certificateNumber}/download")
    @Operation(
            summary = "Download a certificate",
            description = "Downloads the authenticated student's certificate as a PDF file.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "PDF downloaded successfully",
                    content = @Content(
                            mediaType = "application/pdf",
                            schema = @Schema(type = "string", format = "binary")
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Not authorized to download this certificate"),
            @ApiResponse(responseCode = "404", description = "Certificate or PDF file not found")
    })
    public ResponseEntity<Resource> downloadCertificate(
            @Parameter(description = "Unique certificate number")
            @PathVariable String certificateNumber,

            @AuthenticationPrincipal UserDetails userDetails
    ) throws IOException {

        Resource resource = certificateService.downloadCertificate(
                userDetails.getUsername(),
                certificateNumber
        );

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + certificateNumber + ".pdf\""
                )
                .body(resource);
    }
}
