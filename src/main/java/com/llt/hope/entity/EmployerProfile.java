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

    private String companyName;
    private String industry;
    private String companySize;
    private String companyDescription;
    private String website;
    private String logo;
    private String accessibilityFeatures;
    private String businessRegistrationNumber;
}
