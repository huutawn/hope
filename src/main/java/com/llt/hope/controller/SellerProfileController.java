package com.llt.hope.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.llt.hope.dto.request.SellerProfileCreationRequest;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.SellerProfileResponse;
import com.llt.hope.service.SellerProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/sellerProfile")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class SellerProfileController {
    SellerProfileService sellerProfileService;

    @PostMapping
    public ApiResponse<SellerProfileResponse> createSellerProfile(
            @ModelAttribute SellerProfileCreationRequest request) {
        return ApiResponse.<SellerProfileResponse>builder()
                .result(sellerProfileService.createSeller(request))
                .build();
    }
}
