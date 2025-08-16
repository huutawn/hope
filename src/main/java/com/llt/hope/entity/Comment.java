package com.llt.hope.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post; // Bài đăng mà bình luận thuộc về

    @ManyToOne
    @JoinColumn(name = "post_volunteer_id")
    private PostVolunteer postVolunteer; // Bài tình nguyện mà bình luận thuộc về

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user; // Người bình luận

    @Column(columnDefinition = "TEXT", nullable = false)
    @NotNull
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    // Helper method to check if comment belongs to a post
    public boolean isPostComment() {
        return post != null;
    }

    // Helper method to check if comment belongs to a post volunteer
    public boolean isPostVolunteerComment() {
        return postVolunteer != null;
    }
}
