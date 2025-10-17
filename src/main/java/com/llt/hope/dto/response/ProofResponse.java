package com.llt.hope.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.*;

import com.llt.hope.entity.MediaFile;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProofResponse {
    Long id;
    private String title;

    private String content;

    private Set<MediaFile> proofImage;
    private Long postVolunteerId;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String status;
    private boolean isActive;
}
