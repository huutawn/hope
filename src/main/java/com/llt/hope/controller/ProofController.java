package com.llt.hope.controller;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.request.ProofRequest;
import com.llt.hope.dto.request.ProofStatusUpdateRequest;
import com.llt.hope.dto.request.ProofUpdateRequest;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.PageResponse;
import com.llt.hope.dto.response.ProofResponse;
import com.llt.hope.service.ProofService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/proofs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Proof Controller", description = "APIs for managing proof submissions")
public class ProofController {

    private final ProofService proofService;

    @PostMapping
    @Operation(summary = "Create a new proof", description = "Create a new proof submission for a volunteer post")
    public ApiResponse<ProofResponse> createProof(@Valid @ModelAttribute ProofRequest request) {
        log.info("Creating proof for post volunteer ID: {}", request.getPostVolunteerId());
        ProofResponse response = proofService.createProof(request);
        return ApiResponse.<ProofResponse>builder()
                .result(response)
                .message("Proof created successfully")
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get proof by ID", description = "Retrieve a specific proof by its ID")
    public ApiResponse<ProofResponse> getProofById(@Parameter(description = "Proof ID") @PathVariable Long id) {
        log.info("Getting proof with ID: {}", id);
        ProofResponse response = proofService.getProofById(id);
        return ApiResponse.<ProofResponse>builder().result(response).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update proof", description = "Update an existing proof (only pending proofs can be updated)")
    public ApiResponse<ProofResponse> updateProof(
            @Parameter(description = "Proof ID") @PathVariable Long id,
            @Valid @ModelAttribute ProofUpdateRequest request) {
        log.info("Updating proof with ID: {}", id);
        ProofResponse response = proofService.updateProof(id, request);
        return ApiResponse.<ProofResponse>builder()
                .result(response)
                .message("Proof updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete proof", description = "Soft delete a proof (set isActive to false)")
    public ApiResponse<Void> deleteProof(@Parameter(description = "Proof ID") @PathVariable Long id) {
        log.info("Deleting proof with ID: {}", id);
        proofService.deleteProof(id);
        return ApiResponse.<Void>builder().message("Proof deleted successfully").build();
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update proof status", description = "Update the status of a proof (Admin only)")
    public ApiResponse<ProofResponse> updateProofStatus(
            @Parameter(description = "Proof ID") @PathVariable Long id,
            @Valid @RequestBody ProofStatusUpdateRequest request) {
        log.info("Updating status for proof ID: {} to status: {}", id, request.getStatus());
        ProofResponse response = proofService.updateProofStatus(id, request);
        return ApiResponse.<ProofResponse>builder()
                .result(response)
                .message("Proof status updated successfully")
                .build();
    }

    @GetMapping("/post/{postVolunteerId}")
    @Operation(summary = "Get proofs by post", description = "Get all proofs for a specific volunteer post")
    public ApiResponse<PageResponse<ProofResponse>> getProofsByPost(
            @Parameter(description = "Post Volunteer ID") @PathVariable Long postVolunteerId,
            @Parameter(description = "Page number (1-based)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        log.info("Getting proofs for post volunteer ID: {}, page: {}, size: {}", postVolunteerId, page, size);
        PageResponse<ProofResponse> response = proofService.getProofByPost(postVolunteerId, page, size);
        return ApiResponse.<PageResponse<ProofResponse>>builder()
                .result(response)
                .build();
    }

    @GetMapping("/post/{postVolunteerId}/status/{status}")
    @Operation(
            summary = "Get proofs by post and status",
            description = "Get proofs for a specific volunteer post filtered by status")
    public ApiResponse<PageResponse<ProofResponse>> getProofsByPostAndStatus(
            @Parameter(description = "Post Volunteer ID") @PathVariable Long postVolunteerId,
            @Parameter(description = "Status (PENDING, APPROVE, REJECT)") @PathVariable String status,
            @Parameter(description = "Page number (1-based)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        log.info(
                "Getting proofs for post volunteer ID: {} with status: {}, page: {}, size: {}",
                postVolunteerId,
                status,
                page,
                size);
        PageResponse<ProofResponse> response =
                proofService.getProofByPostAndStatus(postVolunteerId, status, page, size);
        return ApiResponse.<PageResponse<ProofResponse>>builder()
                .result(response)
                .build();
    }

    @GetMapping("/my-proofs")
    @Operation(summary = "Get my proofs", description = "Get all proofs submitted by the current user")
    public ApiResponse<PageResponse<ProofResponse>> getMyProofs(
            @Parameter(description = "Page number (1-based)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        log.info("Getting my proofs, page: {}, size: {}", page, size);
        PageResponse<ProofResponse> response = proofService.getMyProofs(page, size);
        return ApiResponse.<PageResponse<ProofResponse>>builder()
                .result(response)
                .build();
    }

    @GetMapping("/my-proofs/status/{status}")
    @Operation(
            summary = "Get my proofs by status",
            description = "Get proofs submitted by the current user filtered by status")
    public ApiResponse<PageResponse<ProofResponse>> getMyProofsByStatus(
            @Parameter(description = "Status (PENDING, APPROVE, REJECT)") @PathVariable String status,
            @Parameter(description = "Page number (1-based)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        log.info("Getting my proofs with status: {}, page: {}, size: {}", status, page, size);
        PageResponse<ProofResponse> response = proofService.getMyProofsByStatus(status, page, size);
        return ApiResponse.<PageResponse<ProofResponse>>builder()
                .result(response)
                .build();
    }

    // Admin endpoints
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all proofs", description = "Get all proofs in the system (Admin only)")
    public ApiResponse<PageResponse<ProofResponse>> getAllProofs(
            @Parameter(description = "Page number (1-based)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        log.info("Getting all proofs, page: {}, size: {}", page, size);
        PageResponse<ProofResponse> response = proofService.getAllProofs(page, size);
        return ApiResponse.<PageResponse<ProofResponse>>builder()
                .result(response)
                .build();
    }

    @GetMapping("/admin/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all proofs by status", description = "Get all proofs filtered by status (Admin only)")
    public ApiResponse<PageResponse<ProofResponse>> getAllProofsByStatus(
            @Parameter(description = "Status (PENDING, APPROVE, REJECT)") @PathVariable String status,
            @Parameter(description = "Page number (1-based)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        log.info("Getting all proofs with status: {}, page: {}, size: {}", status, page, size);
        PageResponse<ProofResponse> response = proofService.getAllProofsByStatus(status, page, size);
        return ApiResponse.<PageResponse<ProofResponse>>builder()
                .result(response)
                .build();
    }
}
