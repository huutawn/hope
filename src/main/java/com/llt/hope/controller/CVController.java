package com.llt.hope.controller;

import com.llt.hope.dto.request.CVCreateReq;
import com.llt.hope.dto.request.CVUpdateReq;
import com.llt.hope.dto.request.CompanyCreationRequest;
import com.llt.hope.dto.response.*;
import com.llt.hope.entity.Company;
import com.llt.hope.service.CVService;
import com.llt.hope.service.CompanyService;
import com.turkraft.springfilter.boot.Filter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cv")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CVController {
    CVService cvService;

    @PostMapping
    public ApiResponse<CVResponse> create(@RequestBody CVCreateReq req) {
        return ApiResponse.<CVResponse>builder()
                .result(cvService.create(req))
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<PageResponse> getAllCompanyNonActive(
            @Filter Specification<Company> spec,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        return ApiResponse.<PageResponse>builder()
                .result(cvService.getAll(page,size))
                .build();
    }
    @GetMapping("/{id}")
    public ApiResponse<CVResponse> getDetail(
            @PathVariable Long id
          ) {
        return ApiResponse.<CVResponse>builder()
                .result(cvService.get(id))
                .build();
    }

    @PutMapping
    public ApiResponse<CVResponse> update(
            @RequestBody CVUpdateReq req
    ) {
        return ApiResponse.<CVResponse>builder()
                .result(cvService.update(req))
                .build();
    }
}
