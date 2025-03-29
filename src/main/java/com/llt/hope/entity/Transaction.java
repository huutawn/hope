package com.llt.hope.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String transactionCode; // Mã giao dịch của SePay

    @Column(nullable = false)
    private String senderAccount; // Số tài khoản người gửi

    @Column(nullable = false)
    private String senderName; // Tên người gửi

    @Column(nullable = false)
    private Long amount; // Số tiền giao dịch

    @Column(nullable = false)
    private LocalDateTime transactionTime; // Thời gian giao dịch

    @Column(nullable = false)
    private String description; // Nội dung giao dịch

    @Column(nullable = false)
    private boolean confirmed;

    @Column(nullable = true)
    private String groupTransactionId; // ID nhóm (để gộp các giao dịch)
}