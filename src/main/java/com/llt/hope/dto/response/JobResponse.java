package com.llt.hope.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.llt.hope.entity.Company;

import com.llt.hope.entity.User;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobResponse {
    private Long id;
    private User employer;
    private String title;
    private String description;
    private String requirements;
    private String responsibilities;
    private String benefits;
    private String suitableForDisability;
    private String jobType;
    private String location;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private LocalDate applicationDeadline;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private Integer views;
}
