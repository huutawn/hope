package com.llt.hope.controller;

import com.llt.hope.dto.request.*;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.AuthenticationResponse;
import com.llt.hope.dto.response.IntrospectResponse;
import com.llt.hope.dto.response.JobResponse;
import com.llt.hope.service.AuthenticationService;
import com.llt.hope.service.JobService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/job")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobController {
   JobService jobService;

   @PostMapping
    public ApiResponse<JobResponse> createRecruitment(@RequestBody RecruitmentCreationRequest request){
       return ApiResponse.<JobResponse>builder()
               .result(jobService.createRecruitmentNews(request))
               .build();
   }
}
