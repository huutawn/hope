package com.llt.hope.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.llt.hope.dto.request.PostCreationRequest;
import com.llt.hope.dto.response.PageResponse;
import com.llt.hope.dto.response.PostResponse;
import com.llt.hope.entity.MediaFile;
import com.llt.hope.entity.Post;
import com.llt.hope.entity.User;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.PostMapper;
import com.llt.hope.repository.MediaFileRepository;
import com.llt.hope.repository.PostRepository;
import com.llt.hope.repository.UserRepository;
import com.llt.hope.utils.SecurityUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    UserRepository userRepository;
    CloudinaryService cloudinaryService;
    MediaFileRepository mediaFileRepository;

    @PreAuthorize("isAuthenticated()")
    @Transactional
    public PostResponse createPost(PostCreationRequest request) {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        MediaFile mediaFile = null;
        if (request.getImage() != null) {
            try {
                mediaFile = cloudinaryService.uploadFile(request.getImage(), "post", email);
                mediaFileRepository.save(mediaFile);
            } catch (IOException e) {
                throw new AppException(ErrorCode.UPLOAD_FILE_ERROR);
            }
        }
        Post post = Post.builder()
                .createdAt(LocalDateTime.now())
                .title(request.getTitle())
                .image(mediaFile)
                .content(request.getContent())
                .isPublished(request.isPublished())
                .isPinned(request.isPinned())
                .user(user)
                .build();
        postRepository.save(post);
        return postMapper.toPostResponse(post);
    }

    @PreAuthorize("isAuthenticated()")
    public PageResponse<PostResponse> getAllPost(Specification<Post> spec, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Post> posts = postRepository.findAll(pageable);
        List<PostResponse> postResponses =
                posts.getContent().stream().map(postMapper::toPostResponse).toList();

        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .data(postResponses)
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    public PageResponse<PostResponse> getAllCurrentPosts(Specification<Post> specm, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Page<Post> posts = postRepository.findPostByUser(user, pageable);
        List<PostResponse> postResponses =
                posts.getContent().stream().map(postMapper::toPostResponse).toList();

        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .data(postResponses)
                .build();
    }
}
