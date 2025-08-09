package com.llt.hope.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.request.JobCategoryCreationRequest;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.JobCategoryResponse;
import com.llt.hope.service.JobCategoryService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/jobCategory")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobCategoryController {
    JobCategoryService jobCategoryService;

    @PostMapping
    public ApiResponse<String> createRecruitment(@RequestBody List<JobCategoryCreationRequest> request) {
        return ApiResponse.<String>builder()
                .result(jobCategoryService.createJobCategory(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<JobCategoryResponse>> getAllRecruitment() {
        return ApiResponse.<List<JobCategoryResponse>>builder()
                .result(jobCategoryService.getAllJobCategory())
                .build();
    }
}
