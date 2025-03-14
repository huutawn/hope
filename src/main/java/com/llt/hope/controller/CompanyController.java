package com.llt.hope.controller;

import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.request.CompanyCreationRequest;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.CompanyResponse;
import com.llt.hope.service.CompanyService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyController {
    CompanyService companyService;

    @PostMapping
    public ApiResponse<CompanyResponse> createRecruitment(@RequestBody CompanyCreationRequest request) {
        return ApiResponse.<CompanyResponse>builder()
                .result(companyService.createCompany(request))
                .build();
    }
}
