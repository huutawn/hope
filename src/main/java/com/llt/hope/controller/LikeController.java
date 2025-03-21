package com.llt.hope.controller;

import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.LikePostResponse;
import com.llt.hope.service.LikeService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikeController {
    LikeService likeService;

    @PostMapping("/{postId}")
    public ApiResponse<LikePostResponse> likeAction(@PathVariable long postId) {
        return ApiResponse.<LikePostResponse>builder()
                .result(likeService.likeAction(postId))
                .build();
    }
}
