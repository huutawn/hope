package com.llt.hope.entity;

import jakarta.persistence.*;

import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmployerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Boolean isVerified;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false) // Nhà tuyển dụng đã được xác minh chưa?
    private Company company;

    private String position;
    private String accessibilityFeatures;
}
