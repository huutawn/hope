package com.llt.hope.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.Post;
import com.llt.hope.entity.User;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    Page<Post> findPostByUser(User user, Pageable pageable);

    Page<Post> findPostByIsActive(boolean isActive, Pageable pageable);
    @Query("SELECT p.user FROM Post p WHERE p.id = :postId")
    Optional<User> findUserByPost(@Param("postId") Long postId);
}
