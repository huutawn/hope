package com.llt.hope.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class   PostVolunteerCreationRequest {
    private String title;
    private String location;
    private String content;
    private String stk;
    private String bankName;
    private List<MultipartFile> files;
}
