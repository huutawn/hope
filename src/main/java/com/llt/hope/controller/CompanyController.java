package com.llt.hope.controller;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.request.CompanyCreationRequest;
import com.llt.hope.dto.response.ActiveCompanyResponse;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.CompanyResponse;
import com.llt.hope.dto.response.PageResponse;
import com.llt.hope.entity.Company;
import com.llt.hope.service.CompanyService;
import com.turkraft.springfilter.boot.Filter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyController {
    CompanyService companyService;

    @PostMapping
    public ApiResponse<CompanyResponse> createRecruitment(@ModelAttribute CompanyCreationRequest request) {
        return ApiResponse.<CompanyResponse>builder()
                .result(companyService.createCompany(request))
                .build();
    }

    @GetMapping("/non-active")
    public ApiResponse<PageResponse> getAllCompanyNonActive(
            @Filter Specification<Company> spec,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        return ApiResponse.<PageResponse>builder()
                .result(companyService.getAllCompanyNonActive(spec, page, size))
                .build();
    }
    @GetMapping("/{id}")
    public ApiResponse<CompanyResponse> getDetail(
            @PathVariable Long id
          ) {
        return ApiResponse.<CompanyResponse>builder()
                .result(companyService.getDetail(id))
                .build();
    }

    @PatchMapping("/{companyId}")
    public ApiResponse<ActiveCompanyResponse> activeCompany(@PathVariable long companyId) {
        return ApiResponse.<ActiveCompanyResponse>builder()
                .result(companyService.activeCompany(companyId))
                .build();
    }

    @DeleteMapping("/{companyId}")
    public ApiResponse<Void> deleteCompany(@PathVariable long companyId) {
        companyService.deleteCompany(companyId);
        return ApiResponse.<Void>builder().build();
    }
}
