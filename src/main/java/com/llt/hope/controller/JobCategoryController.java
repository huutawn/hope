package com.llt.hope.controller;

import com.llt.hope.dto.request.JobCategoryCreationRequest;
import com.llt.hope.dto.request.RecruitmentCreationRequest;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.JobCategoryResponse;
import com.llt.hope.dto.response.JobResponse;
import com.llt.hope.service.JobCategoryService;
import com.llt.hope.service.JobService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobCategory")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobCategoryController {
   JobCategoryService jobCategoryService;

   @PostMapping
    public ApiResponse<JobCategoryResponse> createRecruitment(@RequestBody JobCategoryCreationRequest request){
       return ApiResponse.<JobCategoryResponse>builder()
               .result(jobCategoryService.createJobCategory(request))
               .build();
   }
}
