package com.llt.hope.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.request.ReportRequest;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.ReportByPostResponse;
import com.llt.hope.service.ReportService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class   ReportController {
    ReportService reportService;

    @PostMapping
    public ApiResponse<ReportByPostResponse> report(@RequestBody ReportRequest request) {
        return ApiResponse.<ReportByPostResponse>builder()
                .result(reportService.report(request))
                .build();
    }


    @GetMapping("/{postId}")
    public ApiResponse<List<ReportByPostResponse>> getReportByPost(@PathVariable long postId) {
        return ApiResponse.<List<ReportByPostResponse>>builder()
                .result(reportService.getAllReportsByPost(postId))
                .build();
    }
}
