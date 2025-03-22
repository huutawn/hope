package com.llt.hope.controller;

import com.llt.hope.dto.response.UserResponse;
import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.request.ProfileUpdateRequest;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.ProfileResponse;
import com.llt.hope.service.ProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/profile")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ProfileController {
    ProfileService profileService;

    @PatchMapping
    public ApiResponse<ProfileResponse> updateMyProfile(@ModelAttribute ProfileUpdateRequest request) {
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.updateMyProfile(request))
                .build();
    }

    @GetMapping
    public ApiResponse<UserResponse> getMyProfile() {
        return ApiResponse.
                <UserResponse>builder()
                .result(profileService.getMyProfile())
                .build();
    }
}
