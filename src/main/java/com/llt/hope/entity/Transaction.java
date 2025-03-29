package com.llt.hope.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private User user;
    private String transactionId;
    private Double amount;
    private String content;
    private String accountNumber;
    private String transactionDate;
    private String description;
    private String referenceNumber;
    private LocalDateTime createdAt;
}
