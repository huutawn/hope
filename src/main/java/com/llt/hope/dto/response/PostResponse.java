package com.llt.hope.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

import com.llt.hope.entity.MediaFile;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private Long id;

    private UserResponse user;

    private String title;

    private String content;

    private Set<MediaFile> images;
    private boolean isPublished;
    private boolean isPinned;

    private LocalDateTime createdAt;

    private boolean isActive;



}
