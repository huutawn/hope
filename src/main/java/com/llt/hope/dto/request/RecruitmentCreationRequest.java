package com.llt.hope.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitmentCreationRequest {
    private String title;
    private String description;
    private String requirements;
    private String responsibilities;
    private String benefits;
    private String suitableForDisability; // phù hợp cho người khuyết tật nào
    private String jobType;
    private String location;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private LocalDateTime applicationDeadline;
    private String categoryName;
}
