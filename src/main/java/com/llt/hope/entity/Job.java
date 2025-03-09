package com.llt.hope.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    private User employer;

    private String title;
    private String description;
    private String requirements;
    private String responsibilities;
    private String benefits;
    private String suitableForDisability;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    private String location;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private Boolean isSalaryNegotiable = false;
    private LocalDate applicationDeadline;
    private Boolean isActive = true;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    private Integer views = 0;
}

enum JobType {
    FULL_TIME,
    PART_TIME,
    CONTRACT,
    FREELANCE,
    REMOTE
}
