package com.llt.hope.controller;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.request.PostCreationRequest;
import com.llt.hope.dto.response.*;
import com.llt.hope.entity.Post;
import com.llt.hope.service.PostService;
import com.turkraft.springfilter.boot.Filter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;

    @PostMapping
    public ApiResponse<PostResponse> createRecruitment(@ModelAttribute PostCreationRequest request) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.createPost(request))
                .build();
    }

    @GetMapping("/getAll")
    public ApiResponse<PageResponse<PostResponse>> getAllPost(
            @Filter Specification<Post> spec,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size) {
        return ApiResponse.<PageResponse<PostResponse>>builder()
                .result(postService.getAllPost(spec, page, size))
                .build();
    }

    @GetMapping("/non-active")
    public ApiResponse<PageResponse> getAllCompanyNonActive(
            @Filter Specification<Post> spec,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        return ApiResponse.<PageResponse>builder()
                .result(postService.getAllPostNotActive(spec, page, size))
                .build();
    }

    @PatchMapping("/{postId}")
    public ApiResponse<ActivePostResponse> activeCompany(@PathVariable long postId) {
        return ApiResponse.<ActivePostResponse>builder()
                .result(postService.activePost(postId))
                .build();
    }

    @GetMapping("/getMyPost")
    public ApiResponse<PageResponse<PostResponse>> getMyPost(
            @Filter Specification<Post> spec,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size) {
        return ApiResponse.<PageResponse<PostResponse>>builder()
                .result(postService.getAllCurrentPosts(spec, page, size))
                .build();
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
    }
}
