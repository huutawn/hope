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

    private String userId;
    private String name;
    private String userPic;

    private String title;

    private String content;

    private Set<MediaFile> images;
    private boolean isPublished;
    private boolean isPinned;
    private String type;

    private LocalDateTime createdAt;

    private boolean isActive;
    
    // Comment information
    private long commentCount;

}
