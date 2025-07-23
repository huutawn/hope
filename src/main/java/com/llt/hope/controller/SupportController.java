package com.llt.hope.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.SupportResponse;
import com.llt.hope.service.SupportService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/support")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class SupportController {
    SupportService supportService;

    @GetMapping("/post/{id}")
    public ApiResponse<List<SupportResponse>> getSupportByPost(@PathVariable Long id) {
        return ApiResponse.<List<SupportResponse>>builder()
                .result(supportService.getSupportersByPostVolunteer(id))
                .build();
    }

    @GetMapping("/volunteer")
    public ApiResponse<List<SupportResponse>> getSupportByVolunteer() {
        return ApiResponse.<List<SupportResponse>>builder()
                .result(supportService.getDonatedPostsByCurrentUser())
                .build();
    }
}
