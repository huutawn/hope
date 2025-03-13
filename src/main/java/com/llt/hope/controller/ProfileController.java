package com.llt.hope.controller;

import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.request.ProfileCreationRequest;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.ProfileResponse;
import com.llt.hope.service.ProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/product")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ProfileController {
    ProfileService profileService;

    @PostMapping
    public ApiResponse<ProfileResponse> createMyProfile(@ModelAttribute ProfileCreationRequest request) {
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.createMyProfile(request))
                .build();
    }

    @GetMapping
    public ApiResponse<ProfileResponse> getMyProfile() {
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.getMyProfile())
                .build();
    }
}
