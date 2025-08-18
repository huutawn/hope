package com.llt.hope.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.llt.hope.dto.request.CommentCreationRequest;
import com.llt.hope.dto.request.CommentUpdateRequest;
import com.llt.hope.dto.response.CommentResponse;
import com.llt.hope.dto.response.PageResponse;
import com.llt.hope.entity.Comment;
import com.llt.hope.entity.Post;
import com.llt.hope.entity.PostVolunteer;
import com.llt.hope.entity.User;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.CommentMapper;
import com.llt.hope.repository.jpa.CommentRepository;
import com.llt.hope.repository.jpa.PostRepository;
import com.llt.hope.repository.jpa.PostVolunteerRepository;
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
public class CommentService {
    
    CommentRepository commentRepository;
    CommentMapper commentMapper;
    UserRepository userRepository;
    PostRepository postRepository;
    PostVolunteerRepository postVolunteerRepository;
    
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public CommentResponse createComment(CommentCreationRequest request) {
        // Validate that either postId or postVolunteerId is provided, but not both
        if ((request.getPostId() == null && request.getPostVolunteerId() == null) ||
            (request.getPostId() != null && request.getPostVolunteerId() != null)) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        
        Comment.CommentBuilder commentBuilder = Comment.builder()
                .content(request.getContent())
                .user(user)
                .createdAt(LocalDateTime.now());
        
        // Set either post or postVolunteer relationship
        if (request.getPostId() != null) {
            Post post = postRepository.findById(request.getPostId())
                    .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
            commentBuilder.post(post);
        } else {
            PostVolunteer postVolunteer = postVolunteerRepository.findById(request.getPostVolunteerId())
                    .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
            commentBuilder.postVolunteer(postVolunteer);
        }
        
        Comment comment = commentBuilder.build();
        comment = commentRepository.save(comment);
        
        log.info("Created comment with ID: {} for user: {}", comment.getId(), user.getEmail());
        
        return commentMapper.toCommentResponse(comment);
    }
    
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public CommentResponse updateComment(Long commentId, CommentUpdateRequest request) {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));
        
        // Check if user owns the comment or is admin
        if (!comment.getUser().getId().equals(currentUser.getId()) && 
            !currentUser.getRoles().stream().anyMatch(role -> "ADMIN".equals(role.getName()))) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        
        comment.setContent(request.getContent());
        comment.setUpdatedAt(LocalDateTime.now());
        
        comment = commentRepository.save(comment);
        
        log.info("Updated comment with ID: {} by user: {}", comment.getId(), currentUser.getEmail());
        
        return commentMapper.toCommentResponse(comment);
    }
    
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public void deleteComment(Long commentId) {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));
        
        // Check if user owns the comment or is admin
        if (!comment.getUser().getId().equals(currentUser.getId()) && 
            !currentUser.getRoles().stream().anyMatch(role -> "ADMIN".equals(role.getName()))) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        
        commentRepository.delete(comment);
        
        log.info("Deleted comment with ID: {} by user: {}", commentId, currentUser.getEmail());
    }
    
    public PageResponse<CommentResponse> getCommentsByPostId(Long postId, int page, int size) {
        // Verify post exists
        postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        
        Page<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId, pageable);
        
        return createPageResponse(comments, page);
    }
    
    public PageResponse<CommentResponse> getCommentsByPostVolunteerId(Long postVolunteerId, int page, int size) {
        // Verify post volunteer exists
        postVolunteerRepository.findById(postVolunteerId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        
        Page<Comment> comments = commentRepository.findByPostVolunteerIdOrderByCreatedAtDesc(postVolunteerId, pageable);
        
        return createPageResponse(comments, page);
    }
    
    @PreAuthorize("isAuthenticated()")
    public PageResponse<CommentResponse> getMyComments(int page, int size) {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        
        Page<Comment> comments = commentRepository.findByUserIdOrderByCreatedAtDesc(currentUser.getId(), pageable);
        
        return createPageResponse(comments, page);
    }
    
    public CommentResponse getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));
        
        return commentMapper.toCommentResponse(comment);
    }
    
    public long countCommentsByPostId(Long postId) {
        // Verify post exists
        postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        
        return commentRepository.countByPostId(postId);
    }
    
    public long countCommentsByPostVolunteerId(Long postVolunteerId) {
        // Verify post volunteer exists
        postVolunteerRepository.findById(postVolunteerId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        
        return commentRepository.countByPostVolunteerId(postVolunteerId);
    }
    
    private PageResponse<CommentResponse> createPageResponse(Page<Comment> comments, int page) {
        var commentResponses = comments.getContent().stream()
                .map(commentMapper::toCommentResponse)
                .toList();
        
        return PageResponse.<CommentResponse>builder()
                .currentPage(page)
                .pageSize(comments.getSize())
                .totalElements(comments.getTotalElements())
                .totalPages(comments.getTotalPages())
                .data(commentResponses)
                .build();
    }
}
