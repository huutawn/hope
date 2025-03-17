package com.llt.hope.controller;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.request.*;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.JobResponse;
import com.llt.hope.dto.response.PageResponse;
import com.llt.hope.entity.Job;
import com.llt.hope.service.JobService;
import com.turkraft.springfilter.boot.Filter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/job")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobController {
    JobService jobService;

    @PostMapping
    public ApiResponse<JobResponse> createRecruitment(@RequestBody RecruitmentCreationRequest request) {
        return ApiResponse.<JobResponse>builder()
                .result(jobService.createRecruitmentNews(request))
                .build();
    }

    @GetMapping("/getAll")
    public ApiResponse<PageResponse<JobResponse>> getAllJobRecruitments(
            @Filter Specification<Job> spec,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<JobResponse>>builder()
                .result(jobService.getAllJobRecruitments(spec, page, size))
                .build();
    }

    @GetMapping("/filter")
    public ApiResponse<PageResponse<JobResponse>> filter(
            @RequestParam(value = "categoryName", required = false) String categoryName,
            @RequestParam(value = "requirement", required = false) String requirement,
            @RequestParam(value = "minSalary", required = false) BigDecimal minSalary,
            @RequestParam(value = "maxSalary", required = false) BigDecimal maxSalary,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<JobResponse>>builder()
                .result(jobService.filterJobs(categoryName, requirement, minSalary, maxSalary, page, size))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PageResponse<JobResponse>> search(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<JobResponse>>builder()
                .result(jobService.searchJobs(keyword, page, size))
                .build();
    }
}
