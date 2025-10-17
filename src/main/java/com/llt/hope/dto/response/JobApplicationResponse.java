package com.llt.hope.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplicationResponse {
    private Long id;
    UserResponse applicant;
    JobResponse job;
    CVFormResponse cvForm;
    private boolean isActive;
}
