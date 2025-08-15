package com.llt.hope.repository.elasticsearch;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.llt.hope.document.PostVolunteerDocument;

@Repository
public interface PostVolunteerDocumentRepository extends ElasticsearchRepository<PostVolunteerDocument, String> {
    
    // Search volunteer posts by title, content, or location
    @Query("{\"bool\": {\"must\": [{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title^3\", \"content^2\", \"location^2\", \"userName\", \"bankName\"]}}], \"filter\": [{\"term\": {\"isActive\": true}}]}}")
    Page<PostVolunteerDocument> findByKeyword(String keyword, Pageable pageable);
    
    // Search volunteer posts by location
    Page<PostVolunteerDocument> findByLocationContainingIgnoreCaseAndIsActiveTrueOrderByCreateAtDesc(String location, Pageable pageable);
    
    // Search volunteer posts by status
    Page<PostVolunteerDocument> findByStatusAndIsActiveTrueOrderByCreateAtDesc(String status, Pageable pageable);
    
    // Search volunteer posts by user
    Page<PostVolunteerDocument> findByUserIdAndIsActiveTrueOrderByCreateAtDesc(Long userId, Pageable pageable);
    
    // Search volunteer posts by user name
    Page<PostVolunteerDocument> findByUserNameContainingIgnoreCaseAndIsActiveTrueOrderByCreateAtDesc(String userName, Pageable pageable);
    
    // Find all active volunteer posts
    Page<PostVolunteerDocument> findByIsActiveTrueOrderByCreateAtDesc(Pageable pageable);
    
    // Search volunteer posts by fund range
    @Query("{\"bool\": {\"must\": [{\"range\": {\"fund\": {\"gte\": ?0, \"lte\": ?1}}}], \"filter\": [{\"term\": {\"isActive\": true}}]}}")
    Page<PostVolunteerDocument> findByFundRange(BigDecimal minFund, BigDecimal maxFund, Pageable pageable);
    
    // Search volunteer posts by total amount range
    @Query("{\"bool\": {\"must\": [{\"range\": {\"totalAmount\": {\"gte\": ?0, \"lte\": ?1}}}], \"filter\": [{\"term\": {\"isActive\": true}}]}}")
    Page<PostVolunteerDocument> findByTotalAmountRange(BigDecimal minAmount, BigDecimal maxAmount, Pageable pageable);
    
    // Search volunteer posts by title
    Page<PostVolunteerDocument> findByTitleContainingIgnoreCaseAndIsActiveTrueOrderByCreateAtDesc(String title, Pageable pageable);
    
    // Search volunteer posts by content
    Page<PostVolunteerDocument> findByContentContainingIgnoreCaseAndIsActiveTrueOrderByCreateAtDesc(String content, Pageable pageable);
    
    // Search volunteer posts by bank name
    Page<PostVolunteerDocument> findByBankNameContainingIgnoreCaseAndIsActiveTrueOrderByCreateAtDesc(String bankName, Pageable pageable);
    
    // Advanced search for volunteer posts with multiple filters
    @Query("{\"bool\": {\"must\": [{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title^3\", \"content^2\", \"location^2\", \"userName\", \"bankName\"]}}], \"filter\": [{\"term\": {\"isActive\": true}}]}}")
    Page<PostVolunteerDocument> findByAdvancedSearch(String keyword, String location, String status, Long userId, BigDecimal minFund, BigDecimal maxFund, Pageable pageable);
}
