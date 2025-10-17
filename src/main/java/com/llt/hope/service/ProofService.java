package com.llt.hope.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.llt.hope.constant.StatusCons;
import com.llt.hope.dto.request.ProofRequest;
import com.llt.hope.dto.request.ProofStatusUpdateRequest;
import com.llt.hope.dto.request.ProofUpdateRequest;
import com.llt.hope.dto.response.PageResponse;
import com.llt.hope.dto.response.ProofResponse;
import com.llt.hope.entity.MediaFile;
import com.llt.hope.entity.PostVolunteer;
import com.llt.hope.entity.Proof;
import com.llt.hope.entity.User;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.ProofMapper;
import com.llt.hope.repository.jpa.MediaFileRepository;
import com.llt.hope.repository.jpa.PostVolunteerRepository;
import com.llt.hope.repository.jpa.ProofRepository;
import com.llt.hope.repository.jpa.UserRepository;
import com.llt.hope.utils.SecurityUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProofService {
    UserRepository userRepository;
    PostVolunteerRepository postVolunteerRepository;
    CloudinaryService cloudinaryService;
    ProofRepository proofRepository;
    ProofMapper proofMapper;
    MediaFileRepository mediaFileRepository;

    @Transactional
    public ProofResponse createProof(ProofRequest request) {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Proof proof = Proof.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .createAt(LocalDateTime.now())
                .status(StatusCons.PENDING)
                .isActive(true)
                .build();

        Set<MediaFile> mediaFiles = new HashSet<>();
        if (request.getProofImages() != null && !request.getProofImages().isEmpty()) {
            request.getProofImages().forEach(image -> {
                try {
                    MediaFile mediaFile = cloudinaryService.uploadFile(image, "", "");
                    mediaFiles.add(mediaFile);
                    mediaFileRepository.saveAndFlush(mediaFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        PostVolunteer postVolunteer = postVolunteerRepository
                .findById(request.getPostVolunteerId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));

        proof.setProofImage(mediaFiles);
        proof.setUser(user);
        proof.setPostVolunteer(postVolunteer);

        if (postVolunteer.getProofs() == null || postVolunteer.getProofs().isEmpty()) {
            List<Proof> proofs = new ArrayList<>();
            proofs.add(proof);
            postVolunteer.setProofs(proofs);
        } else {
            postVolunteer.getProofs().add(proof);
        }

        postVolunteerRepository.save(postVolunteer);
        proof = proofRepository.save(proof);
        return proofMapper.toProofResponse(proof);
    }

    public ProofResponse getProofById(Long id) {
        Proof proof = proofRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        return proofMapper.toProofResponse(proof);
    }

    @Transactional
    public ProofResponse updateProof(Long id, ProofUpdateRequest request) {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Proof proof = proofRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));

        // Check if user owns this proof
        if (!proof.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.INVALID_POST);
        }

        // Check if proof can be updated (only pending proofs can be updated)
        if (!StatusCons.PENDING.equals(proof.getStatus())) {
            throw new AppException(ErrorCode.INVALID_POST);
        }

        proof.setTitle(request.getTitle());
        proof.setContent(request.getContent());
        proof.setUpdateAt(LocalDateTime.now());

        // Update images if provided
        if (request.getProofImages() != null && !request.getProofImages().isEmpty()) {
            Set<MediaFile> mediaFiles = new HashSet<>();
            request.getProofImages().forEach(image -> {
                try {
                    MediaFile mediaFile = cloudinaryService.uploadFile(image, "", "");
                    mediaFiles.add(mediaFile);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            proof.setProofImage(mediaFiles);
        }

        proof = proofRepository.save(proof);
        return proofMapper.toProofResponse(proof);
    }

    @Transactional
    public void deleteProof(Long id) {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Proof proof = proofRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));

        // Check if user owns this proof
        if (!proof.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.INVALID_POST);
        }

        // Soft delete - set isActive to false
        proof.setActive(false);
        proof.setUpdateAt(LocalDateTime.now());
        proofRepository.save(proof);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProofResponse updateProofStatus(Long id, ProofStatusUpdateRequest request) {
        Proof proof = proofRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));

        // Validate status
        if (!StatusCons.PENDING.equals(request.getStatus())
                && !StatusCons.APPROVE.equals(request.getStatus())
                && !StatusCons.REJECT.equals(request.getStatus())) {
            throw new AppException(ErrorCode.INVALID_POST);
        }

        proof.setStatus(request.getStatus());
        proof.setUpdateAt(LocalDateTime.now());

        // If rejected, you might want to store the reason
        if (StatusCons.REJECT.equals(request.getStatus()) && request.getReason() != null) {
            // You could add a reason field to Proof entity if needed
            log.info("Proof {} rejected with reason: {}", id, request.getReason());
        }

        proof = proofRepository.save(proof);
        return proofMapper.toProofResponse(proof);
    }

    public PageResponse<ProofResponse> getProofByPost(Long postVolunteerId, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Proof> proofs = proofRepository.findByPostVolunteerId(postVolunteerId, pageable);

        List<ProofResponse> proofResponses =
                proofs.getContent().stream().map(proofMapper::toProofResponse).toList();

        return PageResponse.<ProofResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(proofs.getTotalElements())
                .totalPages(proofs.getTotalPages())
                .data(proofResponses)
                .build();
    }

    public PageResponse<ProofResponse> getProofByPostAndStatus(
            Long postVolunteerId, String status, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Proof> proofs = proofRepository.findByPostVolunteerIdAndStatus(postVolunteerId, status, pageable);

        List<ProofResponse> proofResponses =
                proofs.getContent().stream().map(proofMapper::toProofResponse).toList();

        return PageResponse.<ProofResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(proofs.getTotalElements())
                .totalPages(proofs.getTotalPages())
                .data(proofResponses)
                .build();
    }

    public PageResponse<ProofResponse> getMyProofs(int page, int size) {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Proof> proofs = proofRepository.findByUser(user, pageable);

        List<ProofResponse> proofResponses =
                proofs.getContent().stream().map(proofMapper::toProofResponse).toList();

        return PageResponse.<ProofResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(proofs.getTotalElements())
                .totalPages(proofs.getTotalPages())
                .data(proofResponses)
                .build();
    }

    public PageResponse<ProofResponse> getMyProofsByStatus(String status, int page, int size) {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Proof> proofs = proofRepository.findByUserAndStatus(user, status, pageable);

        List<ProofResponse> proofResponses =
                proofs.getContent().stream().map(proofMapper::toProofResponse).toList();

        return PageResponse.<ProofResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(proofs.getTotalElements())
                .totalPages(proofs.getTotalPages())
                .data(proofResponses)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<ProofResponse> getAllProofsByStatus(String status, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Proof> proofs = proofRepository.findByStatus(status, pageable);

        List<ProofResponse> proofResponses =
                proofs.getContent().stream().map(proofMapper::toProofResponse).toList();

        return PageResponse.<ProofResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(proofs.getTotalElements())
                .totalPages(proofs.getTotalPages())
                .data(proofResponses)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<ProofResponse> getAllProofs(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Proof> proofs = proofRepository.findAll(pageable);

        List<ProofResponse> proofResponses =
                proofs.getContent().stream().map(proofMapper::toProofResponse).toList();

        return PageResponse.<ProofResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(proofs.getTotalElements())
                .totalPages(proofs.getTotalPages())
                .data(proofResponses)
                .build();
    }
}
