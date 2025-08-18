package com.llt.hope.repository.elasticsearch;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.llt.hope.document.JobDocument;

@Repository
public interface JobDocumentRepository extends ElasticsearchRepository<JobDocument, String> {
    
    // Search jobs by title, description, benefits, suitableForDisability, or company name
    @Query("{\"bool\": {\"must\": [{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title^3\", \"description^2\", \"benefits^2\", \"suitableForDisability^2\", \"companyName^3\", \"location\", \"requirements\", \"responsibilities\"]}}]}}")
    Page<JobDocument> findByKeyword(String keyword, Pageable pageable);
    
    // Search jobs by location
    Page<JobDocument> findByLocationContainingIgnoreCaseAndIsActiveTrue(String location, Pageable pageable);
    
    // Search jobs by job category
    Page<JobDocument> findByJobCategoryIdAndIsActiveTrue(Long jobCategoryId, Pageable pageable);
    
    // Search jobs by company
    Page<JobDocument> findByCompanyIdAndIsActiveTrue(Long companyId, Pageable pageable);
    
    // Search jobs by salary range
    @Query("{\"bool\": {\"must\": [{\"range\": {\"salaryMin\": {\"lte\": ?1}}}, {\"range\": {\"salaryMax\": {\"gte\": ?0}}}], \"filter\": [{\"term\": {\"isActive\": true}}]}}")
    Page<JobDocument> findBySalaryRange(BigDecimal minSalary, BigDecimal maxSalary, Pageable pageable);
    
    // Search jobs suitable for disability
    Page<JobDocument> findBySuitableForDisabilityContainingIgnoreCaseAndIsActiveTrue(String disabilityType, Pageable pageable);
    
    // Search jobs by job type
    Page<JobDocument> findByJobTypeAndIsActiveTrue(String jobType, Pageable pageable);
    
    // Advanced search with multiple filters
    @Query("{\"bool\": {\"must\": [{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title^3\", \"description^2\", \"companyName^2\", \"location\", \"requirements\", \"responsibilities\", \"benefits\"]}}], \"filter\": [{\"term\": {\"isActive\": true}}]}}")
    Page<JobDocument> findByAdvancedSearch(String keyword, String location, String jobType, Long categoryId, BigDecimal minSalary, BigDecimal maxSalary, Pageable pageable);
    
    // Find all active jobs
    Page<JobDocument> findByIsActiveTrueOrderByCreatedAtDesc(Pageable pageable);
    
    // Find jobs by employer
    Page<JobDocument> findByEmployerIdAndIsActiveTrueOrderByCreatedAtDesc(Long employerId, Pageable pageable);
}
