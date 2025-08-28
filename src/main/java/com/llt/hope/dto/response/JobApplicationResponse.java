package com.llt.hope.dto.response;

import com.llt.hope.entity.CVForm;
import com.llt.hope.entity.Job;
import com.llt.hope.entity.User;

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
