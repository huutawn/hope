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
import com.llt.hope.dto.response.SearchResponse;
import com.llt.hope.dto.request.SearchRequest;
import com.llt.hope.service.SearchService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/search")
@ConditionalOnProperty(value = "spring.elasticsearch.enabled", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
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
    
    /**
     * Unified search endpoint that searches across Job, Post, and PostVolunteer entities
     * Frontend just sends a keyword and gets results from all three entity types
     */
    @PostMapping("/unified")
    public ApiResponse<SearchResponse> searchUnified(@Valid @RequestBody SearchRequest request) {
        log.info("Unified search request received for keyword: {}", request.getKeyword());
        
        try {
            SearchResponse searchResponse = searchService.searchAllUnified(request);
            
            log.info("Unified search completed. Found {} total results for keyword: {}", 
                searchResponse.getTotalResults(), request.getKeyword());
            
            return ApiResponse.<SearchResponse>builder()
                    .result(searchResponse)
                    .build();
                    
        } catch (Exception e) {
            log.error("Error occurred during unified search for keyword: {}", request.getKeyword(), e);
            throw new RuntimeException("Search failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Simple GET endpoint for unified search
     */
    @GetMapping("/unified")
    public ApiResponse<SearchResponse> searchUnifiedByKeyword(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        
        SearchRequest request = SearchRequest.builder()
            .keyword(keyword)
            .page(page)
            .size(size)
            .build();
            
        return searchUnified(request);
    }
}
