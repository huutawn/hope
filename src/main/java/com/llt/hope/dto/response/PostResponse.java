package com.llt.hope.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

import com.llt.hope.entity.Comment;
import com.llt.hope.entity.Like;
import com.llt.hope.entity.MediaFile;
import com.llt.hope.entity.User;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private Long id;

    private User user; // Người đăng bài

    private String title;

    private String content;

    private Set<MediaFile> images;
    private boolean isPublished;
    private boolean isPinned;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Set<Comment> comments;

    private List<Like> likes;
}
