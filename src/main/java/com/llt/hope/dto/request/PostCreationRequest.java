package com.llt.hope.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreationRequest {
    private String title;

    private List<MultipartFile> images;
    private String content;

    private boolean isPublished = true;
    private boolean isPinned = false;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}
