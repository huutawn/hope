package com.llt.hope.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.request.CommentCreationRequest;
import com.llt.hope.dto.request.CommentUpdateRequest;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.CommentResponse;
import com.llt.hope.dto.response.PageResponse;
import com.llt.hope.service.CommentService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {
    
    CommentService commentService;
    
    @PostMapping
    public ApiResponse<CommentResponse> createComment(@RequestBody @Valid CommentCreationRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.createComment(request))
                .build();
    }
    
    @PutMapping("/{commentId}")
    public ApiResponse<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody @Valid CommentUpdateRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.updateComment(commentId, request))
                .build();
    }
    
    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ApiResponse.<Void>builder()
                .message("Comment deleted successfully")
                .build();
    }
    
    @GetMapping("/{commentId}")
    public ApiResponse<CommentResponse> getComment(@PathVariable Long commentId) {
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.getCommentById(commentId))
                .build();
    }
    
    @GetMapping("/post/{postId}")
    public ApiResponse<PageResponse<CommentResponse>> getCommentsByPost(
            @PathVariable Long postId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<CommentResponse>>builder()
                .result(commentService.getCommentsByPostId(postId, page, size))
                .build();
    }
    
    @GetMapping("/post-volunteer/{postVolunteerId}")
    public ApiResponse<PageResponse<CommentResponse>> getCommentsByPostVolunteer(
            @PathVariable Long postVolunteerId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<CommentResponse>>builder()
                .result(commentService.getCommentsByPostVolunteerId(postVolunteerId, page, size))
                .build();
    }
    
    @GetMapping("/my-comments")
    public ApiResponse<PageResponse<CommentResponse>> getMyComments(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<CommentResponse>>builder()
                .result(commentService.getMyComments(page, size))
                .build();
    }
    
    @GetMapping("/post/{postId}/count")
    public ApiResponse<Long> getCommentCountByPost(@PathVariable Long postId) {
        return ApiResponse.<Long>builder()
                .result(commentService.countCommentsByPostId(postId))
                .build();
    }
    
    @GetMapping("/post-volunteer/{postVolunteerId}/count")
    public ApiResponse<Long> getCommentCountByPostVolunteer(@PathVariable Long postVolunteerId) {
        return ApiResponse.<Long>builder()
                .result(commentService.countCommentsByPostVolunteerId(postVolunteerId))
                .build();
    }
}
