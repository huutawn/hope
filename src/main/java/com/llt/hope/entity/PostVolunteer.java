package com.llt.hope.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostVolunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String location;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String status;
    private String stk;
    private String bankName;
    private LocalDateTime createAt;
    private LocalDate expiryDate;
    private boolean isActive;
    private Integer likes;

    @Builder.Default
    private BigDecimal fund = BigDecimal.ZERO; // Số tiền mục tiêu

    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO; // Số tiền đã quyên góp được

    @Builder.Default
    private BigDecimal usedAmount = BigDecimal.ZERO; // Số tiền đã rút

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_volunteer_id")
    private List<MediaFile> files;

    @OneToMany(mappedBy = "postVolunteer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Support> supports;

    @OneToMany(mappedBy = "postVolunteer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Proof> proofs;

    @OneToMany(mappedBy = "postVolunteer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WithdrawalRequest> withdrawalRequests;
}
