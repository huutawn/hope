package com.llt.hope.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.security.Timestamp;

@Entity
@Table(name = "PaymentTransactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_method", nullable = false)
    String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('pending', 'completed', 'failed', 'refunded') DEFAULT 'pending'")
    private TransactionStatus status = TransactionStatus.PENDING;

    @Column(name = "transaction_date", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp transactionDate;

    @Column(name = "reference_number", length = 100)
    private String referenceNumber;
    public void setPaymentMethod(String paymentMethod) {
        if (!isValidPaymentMethod(paymentMethod)) {
            throw new IllegalArgumentException("Invalid payment method: " + paymentMethod);
        }
        this.paymentMethod = paymentMethod;
    }

    private boolean isValidPaymentMethod(String paymentMethod) {
        return paymentMethod.equals("COD")
                || paymentMethod.equals("BANK_TRANSFER")
                || paymentMethod.equals("CREDIT_CARD")
                || paymentMethod.equals("E_WALLET");
    }
}
enum TransactionStatus{
    PENDING,COMPLETED,FAILED,REFUNDED
}
