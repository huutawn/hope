package com.llt.hope.dto.request;

import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProofUpdateRequest {
    private String title;
    private String content;
    private Set<MultipartFile> proofImages;
}
