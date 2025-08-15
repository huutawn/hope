package com.llt.hope.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.JobResponse;
import com.llt.hope.dto.response.PageResponse;
import com.llt.hope.dto.response.PostResponse;
import com.llt.hope.dto.response.PostVolunteerResponse;
import com.llt.hope.service.SearchService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/search")
@ConditionalOnProperty(value = "spring.elasticsearch.enabled", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchController {
    SearchService searchService;

    /**
     * Search jobs by keyword
     * Searches in: title, description, benefits, suitableForDisability, company name
     */
    @GetMapping("/jobs")
    public ApiResponse<PageResponse<JobResponse>> searchJobs(
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<JobResponse>>builder()
                .result(searchService.searchJobs(keyword, page, size))
                .build();
    }

    /**
     * Search posts by keyword  
     * Searches in: title and content
     */
    @GetMapping("/posts")
    public ApiResponse<PageResponse<PostResponse>> searchPosts(
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<PostResponse>>builder()
                .result(searchService.searchPosts(keyword, page, size))
                .build();
    }

    /**
     * Search volunteer posts by keyword
     * Searches in: title and content
     */
    @GetMapping("/post-volunteers")
    public ApiResponse<PageResponse<PostVolunteerResponse>> searchPostVolunteers(
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<PostVolunteerResponse>>builder()
                .result(searchService.searchPostVolunteers(keyword, page, size))
                .build();
    }

    /**
     * Search across all entities (jobs, posts, volunteer posts)
     */
    @GetMapping("/all")
    public ApiResponse<PageResponse<Object>> searchAll(
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<Object>>builder()
                .result(searchService.searchAll(keyword, page, size))
                .build();
    }
}
