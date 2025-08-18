package com.llt.hope.service;

import java.io.IOException;
import java.math.BigDecimal;
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
import com.llt.hope.dto.request.ActivePostVolunteerRequest;
import com.llt.hope.dto.request.DonationRequest;
import com.llt.hope.dto.request.PostVolunteerCreationRequest;
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

    @Transactional
    public DonationResponse donate(DonationRequest request) {
        PostVolunteer post = postVolunteerRepository
                .findById(request.getPostVolunteerId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (post.getTotalAmount() == null) {
            post.setTotalAmount(BigDecimal.valueOf(0));
            post = postVolunteerRepository.saveAndFlush(post);
        }
        User donator =
                userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (request.getAmount().compareTo(donator.getFund()) > 0) throw new AppException(ErrorCode.INSUFFICIENT_FUND);
        if (post.getFund().compareTo(post.getTotalAmount().add(request.getAmount())) < 0)
            throw new AppException(ErrorCode.ENOUGH_CAPITAL);
        if (post.getStatus().equals(StatusCons.FULLED) || post.getStatus().equals(StatusCons.EXPIRED))
            throw new AppException(ErrorCode.INVALID_POST);

        BigDecimal amount = post.getTotalAmount();

        BigDecimal total = amount.add(request.getAmount());
        if (total.compareTo(post.getFund()) == 0) post.setStatus(StatusCons.FULLED);
        post.setTotalAmount(total);
        donator.setFund(donator.getFund().subtract(request.getAmount()));
        donator = userRepository.save(donator);
        post = postVolunteerRepository.save(post);
        Support support = Support.builder()
                .user(donator)
                .donatedAt(LocalDateTime.now())
                .postVolunteer(post)
                .donatedMoney(request.getAmount())
                .build();
        supportRepository.save(support);
        DonationResponse donationResponse = DonationResponse.builder()
                .postId(post.getId())
                .donatorId(donator.getId())
                .totalAmount(post.getTotalAmount())
                .amount(request.getAmount())
                .build();
        return donationResponse;
    }

    public PostVolunteerResponse createPost(PostVolunteerCreationRequest request) {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<MediaFile> mediaFiles = new ArrayList<>();
        if (request.getFiles() != null || request.getFiles().isEmpty()) {
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
                .status(StatusCons.NORMAL)
                .stk(request.getStk())
                .bankName(request.getBankName())
                .location(request.getLocation())
                .createAt(LocalDateTime.now())
                .files(mediaFiles)
                .isActive(false)
                .user(user)
                .build();
        postVolunteer = postVolunteerRepository.save(postVolunteer);
        
        // Index post volunteer in Elasticsearch (only when active)
        if (postVolunteer.isActive()) {
            documentIndexingService.indexPostVolunteer(postVolunteer);
        }
        
        UserResponse userResponse = userMapper.toUserResponse(user);
        PostVolunteerResponse postVolunteerResponse = PostVolunteerResponse.builder()
                .id(postVolunteer.getId())
                .title(postVolunteer.getTitle())
                .content(postVolunteer.getContent())
                .location(postVolunteer.getLocation())
                .status(StatusCons.NORMAL)
                .createAt(postVolunteer.getCreateAt())
                .files(postVolunteer.getFiles())
                .stk(postVolunteer.getStk())
                .bankName(postVolunteer.getBankName())
                .isActive(false)
                .userId(postVolunteer.getUser().getId())
                .userPic(postVolunteer.getUser().getProfile().getProfilePicture()!=null?postVolunteer.getUser().getProfile().getProfilePicture().getUrl():null)
                .name(postVolunteer.getUser().getProfile().getFullName()==null?postVolunteer.getUser().getEmail():postVolunteer.getUser().getProfile().getFullName())
                .build();
        return postVolunteerResponse;
    }
    @PreAuthorize("isAuthenticated()")
    public PostVolunteerResponse likePost(Long id){
        PostVolunteer post=postVolunteerRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.POST_NOT_EXISTED));
        Integer currentLike=post.getLikes();
        if(post.getLikes()==null)
            currentLike=0;
        post.setLikes(currentLike+1);
        post=postVolunteerRepository.save(post);
        return postVolunteerMapper.toPostVolunteerResponse(post);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<PostVolunteerResponse> getAllPostNotActive(
            Specification<PostVolunteer> spec, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<PostVolunteer> posts = postVolunteerRepository.findPostVolunteerByIsActive(false, pageable);
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

    public PageResponse<PostVolunteerResponse> getAllPost(Specification<PostVolunteer> spec, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<PostVolunteer> posts = postVolunteerRepository.findPostVolunteerByIsActive(true, pageable);
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
    public ActivePostResponse activatePost(ActivePostVolunteerRequest request) {
        PostVolunteer postVolunteer = postVolunteerRepository
                .findById(request.getPostVolunteerId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        postVolunteer.setActive(true);
        postVolunteer.setFund(request.getFund());
        postVolunteer = postVolunteerRepository.save(postVolunteer);
        
        // Index post volunteer in Elasticsearch when activated
        documentIndexingService.indexPostVolunteer(postVolunteer);
        
        return ActivePostResponse.builder()
                .id(postVolunteer.getId())
                .isActive(true)
                .build();
    }

    public List<PostVolunteerResponse> getAllPostFulledByCurrentUser() {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<PostVolunteer> posts = user.getPostVolunteers().stream().toList();
        List<PostVolunteer> postFulled = new ArrayList<>();
        for (PostVolunteer post : posts) {
            if (post.getStatus().equals(StatusCons.FULLED)) postFulled.add(post);
        }
        return postFulled.stream()
                .map(postVolunteerMapper::toPostVolunteerResponse)
                .collect(Collectors.toList());
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

    public PostVolunteerResponse RequestRestore(long postId) {
        PostVolunteer postVolunteer = postVolunteerRepository
                .findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        postVolunteer.setStatus(StatusCons.WAITING);
        postVolunteer = postVolunteerRepository.save(postVolunteer);
        return postVolunteerMapper.toPostVolunteerResponse(postVolunteer);
    }
}
