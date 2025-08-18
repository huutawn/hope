package com.llt.hope.repository.elasticsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.llt.hope.document.PostDocument;

@Repository
public interface PostDocumentRepository extends ElasticsearchRepository<PostDocument, String> {
    
    // Search posts by title or content
    @Query("{\"bool\": {\"must\": [{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title^3\", \"content^2\", \"userName\"]}}], \"filter\": [{\"term\": {\"isActive\": true}}]}}")
    Page<PostDocument> findByKeyword(String keyword, Pageable pageable);
    
    // Search posts by type
    Page<PostDocument> findByTypeAndIsActiveTrueAndIsPublishedTrueOrderByCreatedAtDesc(String type, Pageable pageable);
    
    // Search posts by user
    Page<PostDocument> findByUserIdAndIsActiveTrueOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    // Search posts by user name
    Page<PostDocument> findByUserNameContainingIgnoreCaseAndIsActiveTrueAndIsPublishedTrueOrderByCreatedAtDesc(String userName, Pageable pageable);
    
    // Find all active and published posts
    Page<PostDocument> findByIsActiveTrueAndIsPublishedTrueOrderByCreatedAtDesc(Pageable pageable);
    
    // Find pinned posts
    Page<PostDocument> findByIsPinnedTrueAndIsActiveTrueAndIsPublishedTrueOrderByCreatedAtDesc(Pageable pageable);
    
    // Search posts by title
    Page<PostDocument> findByTitleContainingIgnoreCaseAndIsActiveTrueAndIsPublishedTrueOrderByCreatedAtDesc(String title, Pageable pageable);
    
    // Search posts by content
    Page<PostDocument> findByContentContainingIgnoreCaseAndIsActiveTrueAndIsPublishedTrueOrderByCreatedAtDesc(String content, Pageable pageable);
    
    // Advanced search for posts with multiple filters
    @Query("{\"bool\": {\"must\": [{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title^3\", \"content^2\", \"userName\"]}}], \"filter\": [{\"term\": {\"isActive\": true}}, {\"term\": {\"isPublished\": true}}]}}")
    Page<PostDocument> findByAdvancedSearch(String keyword, String type, Long userId, Boolean isPinned, Pageable pageable);
}
