package com.llt.hope.controller;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.request.DonationRequest;
import com.llt.hope.dto.request.PostVolunteerCreationRequest;
import com.llt.hope.dto.request.WithdrawalRequestDto;
import com.llt.hope.dto.response.*;
import com.llt.hope.entity.PostVolunteer;
import com.llt.hope.service.PostVolunteerService;
import com.turkraft.springfilter.boot.Filter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/postVolunteer")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostVolunteerController {
    PostVolunteerService postVolunteerService;

    @PostMapping
    public ApiResponse<PostVolunteerResponse> createRecruitment(@ModelAttribute PostVolunteerCreationRequest request) {
        return ApiResponse.<PostVolunteerResponse>builder()
                .result(postVolunteerService.createPost(request))
                .build();
    }

    @PostMapping("/re-index")
    public String reIndex() {
        return postVolunteerService.reIndex();
    }

    @GetMapping("/getAll")
    public ApiResponse<PageResponse<PostVolunteerResponse>> getAllPost(
            @Filter Specification<PostVolunteer> spec,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size) {
        return ApiResponse.<PageResponse<PostVolunteerResponse>>builder()
                .result(postVolunteerService.getAllPost(spec, page, size))
                .build();
    }

    @GetMapping("/non-active")
    public ApiResponse<PageResponse> getAllCompanyNonActive(
            @Filter Specification<PostVolunteer> spec,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        return ApiResponse.<PageResponse>builder()
                .result(postVolunteerService.getAllPostNotActive(spec, page, size))
                .build();
    }

    @PatchMapping("/{postVolunteerId}/active")
    public ApiResponse<ActivePostResponse> activePost(@PathVariable Long postVolunteerId) {
        return ApiResponse.<ActivePostResponse>builder()
                .result(postVolunteerService.activatePost(postVolunteerId))
                .build();
    }

    @PatchMapping("/like")
    public ApiResponse<PostVolunteerResponse> likePost(@RequestParam(value = "postId") Long id) {
        return ApiResponse.<PostVolunteerResponse>builder()
                .result(postVolunteerService.likePost(id))
                .build();
    }

    @PatchMapping("/donate")
    public ApiResponse<DonationQRResponse> donate(@RequestBody DonationRequest request) {
        return ApiResponse.<DonationQRResponse>builder()
                .result(postVolunteerService.donate(request))
                .build();
    }

    @PostMapping("/withdrawal")
    public ApiResponse<WithdrawalResponse> requestWithdrawal(@RequestBody WithdrawalRequestDto request) {
        return ApiResponse.<WithdrawalResponse>builder()
                .result(postVolunteerService.requestWithdrawal(request))
                .message("Withdrawal request submitted successfully")
                .build();
    }

    @GetMapping("/{postId}/withdrawals")
    public ApiResponse<PageResponse<WithdrawalResponse>> getWithdrawalsByPost(
            @PathVariable Long postId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<WithdrawalResponse>>builder()
                .result(postVolunteerService.getWithdrawalsByPost(postId, page, size))
                .build();
    }

    @GetMapping("/fulled")
    public ApiResponse<List<PostVolunteerResponse>> getAllPostFulled() {
        return ApiResponse.<List<PostVolunteerResponse>>builder()
                .result(postVolunteerService.getAllPostFulledByCurrentUser())
                .build();
    }

    //    @GetMapping("/getMyPost")
    //    public ApiResponse<PageResponse<PostVolunteerResponse>> getMyPost(
    //            @Filter Specification<Post> spec,
    //            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
    //            @RequestParam(value = "size", required = false, defaultValue = "3") int size) {
    //        return ApiResponse.<PageResponse<PostVolunteerResponse>>builder()
    //                .result(postVolunteerService.getAllCurrentPosts(spec, page, size))
    //                .build();
    //    }
    @GetMapping("/waiting")
    public ApiResponse<PageResponse> getAllPostWaiting(
            @Filter Specification<PostVolunteer> spec,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        return ApiResponse.<PageResponse>builder()
                .result(postVolunteerService.getAllPostRestore(spec, page, size))
                .build();
    }

    @PatchMapping("/restore/{postId}")
    public ApiResponse<PostVolunteerResponse> restorePost(@PathVariable Long postId) {
        return ApiResponse.<PostVolunteerResponse>builder()
                .result(postVolunteerService.RequestRestore(postId))
                .build();
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postVolunteerService.deletePost(postId);
    }
}
