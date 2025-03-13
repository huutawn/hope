package com.llt.hope.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.llt.hope.dto.request.*;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.JobResponse;
import com.llt.hope.service.JobService;

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
}
