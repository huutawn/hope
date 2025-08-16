package com.llt.hope.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    
    // Find comments by post ID
    Page<Comment> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);
    
    // Find comments by post volunteer ID
    Page<Comment> findByPostVolunteerIdOrderByCreatedAtDesc(Long postVolunteerId, Pageable pageable);
    
    // Find comments by user ID
    Page<Comment> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);
    
    // Count comments for a post
    long countByPostId(Long postId);
    
    // Count comments for a post volunteer
    long countByPostVolunteerId(Long postVolunteerId);
    
    // Find recent comments for a post
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId ORDER BY c.createdAt DESC")
    List<Comment> findRecentCommentsByPostId(@Param("postId") Long postId, Pageable pageable);
    
    // Find recent comments for a post volunteer
    @Query("SELECT c FROM Comment c WHERE c.postVolunteer.id = :postVolunteerId ORDER BY c.createdAt DESC")
    List<Comment> findRecentCommentsByPostVolunteerId(@Param("postVolunteerId") Long postVolunteerId, Pageable pageable);
}
