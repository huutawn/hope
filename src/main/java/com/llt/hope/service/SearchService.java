package com.llt.hope.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.llt.hope.document.JobDocument;
import com.llt.hope.document.PostDocument;
import com.llt.hope.document.PostVolunteerDocument;
import com.llt.hope.dto.response.JobResponse;
import com.llt.hope.dto.response.PageResponse;
import com.llt.hope.dto.response.PostResponse;
import com.llt.hope.dto.response.PostVolunteerResponse;
import com.llt.hope.dto.response.SearchResponse;
import com.llt.hope.dto.request.SearchRequest;
import com.llt.hope.entity.Job;
import com.llt.hope.entity.Post;
import com.llt.hope.entity.PostVolunteer;
import com.llt.hope.mapper.JobHandlerMapper;
import com.llt.hope.mapper.PostMapper;
import com.llt.hope.mapper.PostVolunteerMapper;
import com.llt.hope.repository.elasticsearch.JobDocumentRepository;
import com.llt.hope.repository.elasticsearch.PostDocumentRepository;
import com.llt.hope.repository.elasticsearch.PostVolunteerDocumentRepository;
import com.llt.hope.repository.jpa.JobRepository;
import com.llt.hope.repository.jpa.PostRepository;
import com.llt.hope.repository.jpa.PostVolunteerRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@ConditionalOnProperty(value = "spring.elasticsearch.enabled", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SearchService {
    JobDocumentRepository jobDocumentRepository;
    PostDocumentRepository postDocumentRepository;
    PostVolunteerDocumentRepository postVolunteerDocumentRepository;
    
    JobRepository jobRepository;
    PostRepository postRepository;
    PostVolunteerRepository postVolunteerRepository;
    
    JobHandlerMapper jobHandlerMapper;
    PostMapper postMapper;
    PostVolunteerMapper postVolunteerMapper;

    /**
     * Search jobs by keyword (title, description, benefits, suitableForDisability, company name)
     */
    public PageResponse<JobResponse> searchJobs(String keyword, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        
        Page<JobDocument> jobDocuments = jobDocumentRepository.findByKeyword(keyword, pageable);
        
        // Convert JobDocument to JobResponse using the actual Job entities
        List<JobResponse> jobResponses = jobDocuments.getContent()
            .stream()
            .map(jobDoc -> {
                Job job = jobRepository.findById(jobDoc.getEntityId()).orElse(null);
                if (job != null) {
                    return jobHandlerMapper.toJobResponse(job);
                }
                return null;
            })
            .filter(response -> response != null)
            .collect(Collectors.toList());
        
        return PageResponse.<JobResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(jobDocuments.getTotalElements())
                .totalPages(jobDocuments.getTotalPages())
                .data(jobResponses)
                .build();
    }

    /**
     * Search posts by keyword (title and content)
     */
    public PageResponse<PostResponse> searchPosts(String keyword, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        
        Page<PostDocument> postDocuments = postDocumentRepository.findByKeyword(keyword, pageable);
        
        // Convert PostDocument to PostResponse using the actual Post entities
        List<PostResponse> postResponses = postDocuments.getContent()
            .stream()
            .map(postDoc -> {
                Post post = postRepository.findById(postDoc.getEntityId()).orElse(null);
                if (post != null) {
                    return postMapper.toPostResponse(post);
                }
                return null;
            })
            .filter(response -> response != null)
            .collect(Collectors.toList());
        
        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(postDocuments.getTotalElements())
                .totalPages(postDocuments.getTotalPages())
                .data(postResponses)
                .build();
    }

    /**
     * Search volunteer posts by keyword (title and content)
     */
    public PageResponse<PostVolunteerResponse> searchPostVolunteers(String keyword, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        
        Page<PostVolunteerDocument> postVolunteerDocuments = postVolunteerDocumentRepository.findByKeyword(keyword, pageable);
        
        // Convert PostVolunteerDocument to PostVolunteerResponse using the actual PostVolunteer entities
        List<PostVolunteerResponse> postVolunteerResponses = postVolunteerDocuments.getContent()
            .stream()
            .map(postDoc -> {
                PostVolunteer postVolunteer = postVolunteerRepository.findById(postDoc.getEntityId()).orElse(null);
                if (postVolunteer != null) {
                    return postVolunteerMapper.toPostVolunteerResponse(postVolunteer);
                }
                return null;
            })
            .filter(response -> response != null)
            .collect(Collectors.toList());
        
        return PageResponse.<PostVolunteerResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(postVolunteerDocuments.getTotalElements())
                .totalPages(postVolunteerDocuments.getTotalPages())
                .data(postVolunteerResponses)
                .build();
    }

    /**
     * Search all entities by keyword
     */
    public PageResponse<Object> searchAll(String keyword, int page, int size) {
        // For a combined search, we can search each type and combine results
        // This is a simplified version - in production you might want more sophisticated merging
        PageResponse<JobResponse> jobs = searchJobs(keyword, page, size);
        PageResponse<PostResponse> posts = searchPosts(keyword, page, size);
        PageResponse<PostVolunteerResponse> volunteers = searchPostVolunteers(keyword, page, size);
        
        // Simple combination - in practice you'd want better ranking/mergingd
        List<Object> combinedResults = jobs.getData().stream()
            .map(Object.class::cast)
            .collect(Collectors.toList());
        combinedResults.addAll(posts.getData().stream()
            .map(Object.class::cast)
            .collect(Collectors.toList()));
        combinedResults.addAll(volunteers.getData().stream()
            .map(Object.class::cast)
            .collect(Collectors.toList()));
        
        // Take only the requested page size
        int start = (page - 1) * size;
        int end = Math.min(start + size, combinedResults.size());
        List<Object> pageData = combinedResults.subList(start, end);
        
        return PageResponse.<Object>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(combinedResults.size())
                .totalPages((int) Math.ceil((double) combinedResults.size() / size))
                .data(pageData)
                .build();
    }
    
    /**
     * Unified search across all entity types (Job, Post, PostVolunteer) by keyword
     */
    public SearchResponse searchAllUnified(SearchRequest request) {
        log.info("Unified search for keyword: {}", request.getKeyword());
        
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        
        try {
            // Execute all searches concurrently
            CompletableFuture<Page<JobDocument>> jobsFuture = CompletableFuture.supplyAsync(() -> 
                jobDocumentRepository.findByKeyword(request.getKeyword(), pageable)
            );
            
            CompletableFuture<Page<PostDocument>> postsFuture = CompletableFuture.supplyAsync(() -> 
                postDocumentRepository.findByKeyword(request.getKeyword(), pageable)
            );
            
            CompletableFuture<Page<PostVolunteerDocument>> postVolunteersFuture = CompletableFuture.supplyAsync(() -> 
                postVolunteerDocumentRepository.findByKeyword(request.getKeyword(), pageable)
            );
            
            // Wait for all searches to complete
            CompletableFuture.allOf(jobsFuture, postsFuture, postVolunteersFuture).join();
            
            Page<JobDocument> jobDocuments = jobsFuture.get();
            Page<PostDocument> postDocuments = postsFuture.get();
            Page<PostVolunteerDocument> postVolunteerDocuments = postVolunteersFuture.get();
            
            // Convert documents to response DTOs
            List<JobResponse> jobResponses = jobDocuments.getContent().stream()
                .map(jobDoc -> {
                    Job job = jobRepository.findById(jobDoc.getEntityId()).orElse(null);
                    if (job != null) {
                        return jobHandlerMapper.toJobResponse(job);
                    }
                    return null;
                })
                .filter(response -> response != null)
                .toList();
                
            List<PostResponse> postResponses = postDocuments.getContent().stream()
                .map(postDoc -> {
                    Post post = postRepository.findById(postDoc.getEntityId()).orElse(null);
                    if (post != null) {
                        return postMapper.toPostResponse(post);
                    }
                    return null;
                })
                .filter(response -> response != null)
                .toList();
                
            List<PostVolunteerResponse> postVolunteerResponses = postVolunteerDocuments.getContent().stream()
                .map(postDoc -> {
                    PostVolunteer postVolunteer = postVolunteerRepository.findById(postDoc.getEntityId()).orElse(null);
                    if (postVolunteer != null) {
                        return postVolunteerMapper.toPostVolunteerResponse(postVolunteer);
                    }
                    return null;
                })
                .filter(response -> response != null)
                .toList();
            
            int totalResults = jobResponses.size() + postResponses.size() + postVolunteerResponses.size();
            int maxTotalPages = Math.max(Math.max(jobDocuments.getTotalPages(), postDocuments.getTotalPages()), 
                                        postVolunteerDocuments.getTotalPages());
            
            return SearchResponse.builder()
                .keyword(request.getKeyword())
                .totalResults(totalResults)
                .currentPage(request.getPage())
                .totalPages(maxTotalPages)
                .pageSize(request.getSize())
                .jobs(jobResponses)
                .posts(postResponses)
                .postVolunteers(postVolunteerResponses)
                .jobCount(jobResponses.size())
                .postCount(postResponses.size())
                .postVolunteerCount(postVolunteerResponses.size())
                .build();
                
        } catch (Exception e) {
            log.error("Error occurred during unified search for keyword: {}", request.getKeyword(), e);
            throw new RuntimeException("Search failed: " + e.getMessage(), e);
        }
    }
}
