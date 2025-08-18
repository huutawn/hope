package com.llt.hope.controller;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.response.*;
import com.llt.hope.entity.JobApplication;
import com.llt.hope.service.ApplicationService;
import com.turkraft.springfilter.boot.Filter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobApplicationController {
    ApplicationService applicationService;

    @PostMapping()
    public ApiResponse<JobApplicationResponse> applyJob(@RequestParam(value = "jobId")Long jobId,
                                                        @RequestParam(value = "cvId")Long cvId) {
        return ApiResponse.<JobApplicationResponse>builder()
                .result(applicationService.applyJob(jobId,cvId))
                .build();
    }

    @GetMapping("/getAll")
    public ApiResponse<PageResponse<JobApplicationResponse>> getAllApplicantByJob(
            @Filter Specification<JobApplication> spec,
            @RequestParam(value = "jobId", required = true) long jobId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<JobApplicationResponse>>builder()
                .result(applicationService.getAllApplicantByJob(jobId, spec, page, size))
                .build();
    }
}
