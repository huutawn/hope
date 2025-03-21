package com.llt.hope.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    Job job;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User applicant;

    private String status;

    @Column(name = "appliedAt")
    LocalDateTime appliedAt;
}
