package com.llt.hope.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.llt.hope.constant.StatusCons;
import com.llt.hope.dto.request.DonationRequest;
import com.llt.hope.dto.request.PostVolunteerCreationRequest;
import com.llt.hope.dto.request.ProcessWithdrawalRequest;
import com.llt.hope.dto.request.WithdrawalRequestDto;
import com.llt.hope.dto.response.*;
import com.llt.hope.entity.*;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.PostVolunteerMapper;
import com.llt.hope.mapper.UserMapper;
import com.llt.hope.repository.jpa.MediaFileRepository;
import com.llt.hope.repository.jpa.PostVolunteerRepository;
import com.llt.hope.repository.jpa.SupportRepository;
import com.llt.hope.repository.jpa.UserRepository;
import com.llt.hope.repository.jpa.WithdrawalRequestRepository;
import com.llt.hope.utils.SecurityUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostVolunteerService {
    UserRepository userRepository;
    PostVolunteerRepository postVolunteerRepository;
    SupportRepository supportRepository;
    UserMapper userMapper;
    PostVolunteerMapper postVolunteerMapper;
    CloudinaryService cloudinaryService;
    MediaFileRepository mediaFileRepository;
    DocumentIndexingService documentIndexingService;
    WithdrawalRequestRepository withdrawalRequestRepository;
    QRService qrService;

    @Transactional
    public DonationQRResponse donate(DonationRequest request) {
        PostVolunteer post = postVolunteerRepository
                .findById(request.getPostVolunteerId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));

        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User donator =
                userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Validate post status
        if (post.getStatus().equals(StatusCons.FULLED) || post.getStatus().equals(StatusCons.EXPIRED))
            throw new AppException(ErrorCode.INVALID_POST);

        // Generate user code if not exists
        if (donator.getCode() == null || donator.getCode().isEmpty()) {
            donator.setCode(generateUserCode());
            donator = userRepository.save(donator);
        }
        BigDecimal amount;
        if (request.getAmount() == null) {
            amount = new BigDecimal(0);
        } else amount = request.getAmount();

        // Create QR content: "hope" + userCode + postVolunteerId
        String content = "hope_" + donator.getCode() + "_" + post.getId(); // Format: hope<code>_<postId>

        try {
            // Generate QR code
            byte[] qrBytes = qrService.generateBankQrFile(amount.toString(), content, donator.getCode());

            // Upload QR to cloudinary
            String qrUrl = cloudinaryService.uploadFile(qrBytes, "donation_qr", donator.getCode());

            return DonationQRResponse.builder()
                    .qrCodeUrl(qrUrl)
                    .userCode(donator.getCode())
                    .content(content)
                    .message("QR code generated successfully. Please scan to complete donation.")
                    .build();

        } catch (Exception e) {
            log.error("Error generating QR code for donation: {}", e.getMessage());
            throw new AppException(ErrorCode.UPLOAD_FILE_ERROR);
        }
    }

    @Transactional
    public WithdrawalResponse requestWithdrawal(WithdrawalRequestDto request) {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PostVolunteer post = postVolunteerRepository
                .findById(request.getPostVolunteerId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));

        // Check if user is the owner of the post
        if (!post.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.INVALID_POST);
        }

        // Check if post has enough funds
        if (post.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppException(ErrorCode.INSUFFICIENT_FUND);
        }

        // Calculate maximum withdrawal amount (30% of total amount)
        BigDecimal maxWithdrawalAmount = post.getTotalAmount().multiply(BigDecimal.valueOf(0.3));

        // Validate requested amount
        if (request.getRequestedAmount().compareTo(maxWithdrawalAmount) > 0) {
            throw new AppException(ErrorCode.INSUFFICIENT_FUND);
        }

        // Check if there's an approved proof for the last withdrawal
        List<WithdrawalRequest> lastWithdrawals = withdrawalRequestRepository.findLastApprovedWithdrawal(post.getId());
        // If this is NOT the first withdrawal, check for proof of spending from the
        // previous one.
        if (!lastWithdrawals.isEmpty()) {
            WithdrawalRequest lastApprovedWithdrawal = lastWithdrawals.get(0);
            boolean hasApprovedProof = post.getProofs().stream()
                    .anyMatch(proof -> StatusCons.APPROVE.equals(proof.getStatus())
                            && proof.getCreateAt().isAfter(lastApprovedWithdrawal.getProcessedAt()));

            if (!hasApprovedProof) {
                throw new AppException(ErrorCode.PROOF_OF_SPENDING_REQUIRED);
            }
        }

        // Create withdrawal request
        WithdrawalRequest withdrawalRequest = WithdrawalRequest.builder()
                .postVolunteer(post)
                .user(user)
                .requestedAmount(request.getRequestedAmount())
                .status(StatusCons.WITHDRAWAL_PENDING)
                .requestedAt(LocalDateTime.now())
                .bankAccount(request.getBankAccount())
                .bankName(request.getBankName())
                .accountHolderName(request.getAccountHolderName())
                .build();

        withdrawalRequest = withdrawalRequestRepository.save(withdrawalRequest);

        return WithdrawalResponse.builder()
                .id(withdrawalRequest.getId())
                .postVolunteerId(post.getId())
                .userId(user.getId())
                .requestedAmount(withdrawalRequest.getRequestedAmount())
                .status(withdrawalRequest.getStatus())
                .requestedAt(withdrawalRequest.getRequestedAt())
                .bankAccount(withdrawalRequest.getBankAccount())
                .bankName(withdrawalRequest.getBankName())
                .accountHolderName(withdrawalRequest.getAccountHolderName())
                .build();
    }

    public PageResponse<WithdrawalResponse> getWithdrawalsByPost(Long postVolunteerId, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "requestedAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<WithdrawalRequest> withdrawals =
                withdrawalRequestRepository.findByPostVolunteerId(postVolunteerId, pageable);

        List<WithdrawalResponse> withdrawalResponses = withdrawals.getContent().stream()
                .map(this::mapToWithdrawalResponse)
                .toList();

        return PageResponse.<WithdrawalResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(withdrawals.getTotalElements())
                .totalPages(withdrawals.getTotalPages())
                .data(withdrawalResponses)
                .build();
    }

    private WithdrawalResponse mapToWithdrawalResponse(WithdrawalRequest withdrawal) {
        return WithdrawalResponse.builder()
                .id(withdrawal.getId())
                .postVolunteerId(withdrawal.getPostVolunteer().getId())
                .userId(withdrawal.getUser().getId())
                .requestedAmount(withdrawal.getRequestedAmount())
                .approvedAmount(withdrawal.getApprovedAmount())
                .status(withdrawal.getStatus())
                .reason(withdrawal.getReason())
                .requestedAt(withdrawal.getRequestedAt())
                .processedAt(withdrawal.getProcessedAt())
                .processedBy(withdrawal.getProcessedBy())
                .bankAccount(withdrawal.getBankAccount())
                .bankName(withdrawal.getBankName())
                .accountHolderName(withdrawal.getAccountHolderName())
                .build();
    }

    private String generateUserCode() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        java.security.SecureRandom random = new java.security.SecureRandom();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    public PageResponse<PostVolunteerResponse> getAllPost(Specification<PostVolunteer> spec, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<PostVolunteer> posts =
                postVolunteerRepository.findPostVolunteerByIsActiveAndStatus(true, StatusCons.NORMAL, pageable);
        List<PostVolunteerResponse> PostVolunteerResponses = posts.getContent().stream()
                .map(postVolunteerMapper::toPostVolunteerResponse)
                .toList();

        return PageResponse.<PostVolunteerResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .data(PostVolunteerResponses)
                .build();
    }

    public String reIndex() {
        List<PostVolunteer> postVolunteers = postVolunteerRepository.findAll();
        for (PostVolunteer postVolunteer : postVolunteers) {
            documentIndexingService.indexPostVolunteer(postVolunteer);
        }
        return "heheh";
    }

    public PostVolunteerResponse createPost(PostVolunteerCreationRequest request) {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<MediaFile> mediaFiles = new ArrayList<>();
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            for (MultipartFile file : request.getFiles()) {
                MediaFile mediaFile;
                try {
                    mediaFile = cloudinaryService.uploadFile(file, "postVolunteer", email);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                mediaFiles.add(mediaFile);
            }
            mediaFiles = mediaFileRepository.saveAll(mediaFiles);
        }
        PostVolunteer postVolunteer = PostVolunteer.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .status(StatusCons.INIT)
                .stk(request.getStk())
                .fund(request.getRequiredMoney())
                .bankName(request.getBankName())
                .location(request.getLocation())
                .createAt(LocalDateTime.now())
                .isActive(false)
                .user(user)
                .files(mediaFiles)
                .build();
        postVolunteer = postVolunteerRepository.save(postVolunteer);
        return postVolunteerMapper.toPostVolunteerResponse(postVolunteer);
    }

    public PostVolunteerResponse getPostById(Long id) {
        PostVolunteer postVolunteer =
                postVolunteerRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        return postVolunteerMapper.toPostVolunteerResponse(postVolunteer);
    }

    public PageResponse<PostVolunteerResponse> getAllPosts(Specification<PostVolunteer> spec, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<PostVolunteer> posts = postVolunteerRepository.findAll(spec, pageable);
        List<PostVolunteerResponse> PostVolunteerResponses = posts.getContent().stream()
                .map(postVolunteerMapper::toPostVolunteerResponse)
                .toList();

        return PageResponse.<PostVolunteerResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .data(PostVolunteerResponses)
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    public PostVolunteerResponse likePost(Long id) {
        PostVolunteer post =
                postVolunteerRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        Integer currentLike = post.getLikes();
        if (post.getLikes() == null) currentLike = 0;
        post.setLikes(currentLike + 1);
        post = postVolunteerRepository.save(post);
        return postVolunteerMapper.toPostVolunteerResponse(post);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<PostVolunteerResponse> getAllPostNotActive(
            Specification<PostVolunteer> spec, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<PostVolunteer> posts =
                postVolunteerRepository.findPostVolunteerByIsActiveAndStatus(false, StatusCons.INIT, pageable);
        List<PostVolunteerResponse> PostVolunteerResponses = posts.getContent().stream()
                .map(postVolunteerMapper::toPostVolunteerResponse)
                .toList();

        return PageResponse.<PostVolunteerResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .data(PostVolunteerResponses)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deletePost(Long id) {
        postVolunteerRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ActivePostResponse activatePost(Long postVolunteerId) {
        PostVolunteer postVolunteer = postVolunteerRepository
                .findById(postVolunteerId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        int date = calculateDays(postVolunteer.getFund());
        postVolunteer.setExpiryDate(LocalDate.now().plusDays(date));
        postVolunteer.setActive(true);
        postVolunteer.setStatus(StatusCons.NORMAL);
        postVolunteer = postVolunteerRepository.save(postVolunteer);

        // Index post volunteer in Elasticsearch when activated
        documentIndexingService.indexPostVolunteer(postVolunteer);

        return ActivePostResponse.builder()
                .id(postVolunteer.getId())
                .isActive(true)
                .build();
    }

    public int calculateDays(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(300000)) < 0) {
            return 3;
        } else {
            double calculatedValue = 3 + 8.5 * (Math.log10(amount.doubleValue()) - Math.log10(300000));
            return (int) Math.round(calculatedValue);
        }
    }

    public List<PostVolunteerResponse> getAllPostFulledByCurrentUser() {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<PostVolunteer> postVolunteers =
                postVolunteerRepository.findPostVolunteerByUserAndStatus(user, StatusCons.FULLED);
        return postVolunteers.stream()
                .map(postVolunteerMapper::toPostVolunteerResponse)
                .collect(Collectors.toList());
    }

    public PostVolunteerResponse RequestRestore(Long postId) {
        PostVolunteer postVolunteer = postVolunteerRepository
                .findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        postVolunteer.setStatus(StatusCons.WAITING);
        postVolunteer = postVolunteerRepository.save(postVolunteer);
        return postVolunteerMapper.toPostVolunteerResponse(postVolunteer);
    }

    public PageResponse<PostVolunteerResponse> getAllPostRestore(
            Specification<PostVolunteer> spec, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<PostVolunteer> posts = postVolunteerRepository.findPostVolunteerByStatus(StatusCons.WAITING, pageable);
        List<PostVolunteerResponse> PostVolunteerResponses = posts.getContent().stream()
                .map(postVolunteerMapper::toPostVolunteerResponse)
                .toList();

        return PageResponse.<PostVolunteerResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .data(PostVolunteerResponses)
                .build();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public WithdrawalResponse processWithdrawalRequest(ProcessWithdrawalRequest request) {
        String adminEmail =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        WithdrawalRequest withdrawalRequest = withdrawalRequestRepository
                .findById(request.getWithdrawalRequestId())
                .orElseThrow(() -> new AppException(ErrorCode.WITHDRAWAL_REQUEST_NOT_FOUND));

        if (!StatusCons.WITHDRAWAL_PENDING.equals(withdrawalRequest.getStatus())) {
            throw new AppException(ErrorCode.INVALID_STATE);
        }

        if (StatusCons.WITHDRAWAL_APPROVED.equals(request.getStatus())) {
            PostVolunteer post = withdrawalRequest.getPostVolunteer();
            BigDecimal approvedAmount = request.getApprovedAmount();

            if (approvedAmount == null
                    || approvedAmount.compareTo(BigDecimal.ZERO) <= 0
                    || approvedAmount.compareTo(withdrawalRequest.getRequestedAmount()) > 0) {
                throw new AppException(ErrorCode.INVALID_PARAMETER);
            }

            // Sửa lỗi logic: Kiểm tra trên `totalAmount` thay vì `usedAmount`
            if (post.getTotalAmount().compareTo(approvedAmount) < 0) {
                throw new AppException(ErrorCode.INSUFFICIENT_FUND);
            }
            post.setUsedAmount(post.getUsedAmount().add(approvedAmount));
            postVolunteerRepository.save(post);

            withdrawalRequest.setStatus(StatusCons.WITHDRAWAL_APPROVED);
            withdrawalRequest.setApprovedAmount(approvedAmount);
            withdrawalRequest.setReason(null);

        } else if (StatusCons.WITHDRAWAL_REJECTED.equals(request.getStatus())) {
            if (request.getReason() == null || request.getReason().isBlank()) {
                throw new AppException(ErrorCode.INVALID_PARAMETER);
            }
            withdrawalRequest.setStatus(StatusCons.WITHDRAWAL_REJECTED);
            withdrawalRequest.setReason(request.getReason());
            withdrawalRequest.setApprovedAmount(null);
        } else {
            throw new AppException(ErrorCode.INVALID_PARAMETER);
        }

        withdrawalRequest.setProcessedAt(LocalDateTime.now());
        withdrawalRequest.setProcessedBy(adminEmail);
        WithdrawalRequest savedRequest = withdrawalRequestRepository.save(withdrawalRequest);

        return mapToWithdrawalResponse(savedRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<WithdrawalDetailResponse> getAllWithdrawalRequests(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "requestedAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<WithdrawalRequest> requestsPage = withdrawalRequestRepository.findAll(pageable);

        List<WithdrawalDetailResponse> responseData = requestsPage.getContent().stream()
                .map(req -> WithdrawalDetailResponse.builder()
                        .id(req.getId())
                        .requestedAmount(req.getRequestedAmount())
                        .approvedAmount(req.getApprovedAmount())
                        .status(req.getStatus())
                        .reason(req.getReason())
                        .requestedAt(req.getRequestedAt())
                        .processedAt(req.getProcessedAt())
                        .processedBy(req.getProcessedBy())
                        .bankAccount(req.getBankAccount())
                        .bankName(req.getBankName())
                        .accountHolderName(req.getAccountHolderName())
                        .postVolunteer(postVolunteerMapper.toPostVolunteerResponse(req.getPostVolunteer()))
                        .user(userMapper.toUserResponse(req.getUser()))
                        .build())
                .toList();

        return PageResponse.<WithdrawalDetailResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(requestsPage.getTotalElements())
                .totalPages(requestsPage.getTotalPages())
                .data(responseData)
                .build();
    }
}
