package com.llt.hope.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "Payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    String paymentMethod;

    String paymentStatus = "PENDING";

    @Column(updatable = false)
    private LocalDateTime paymentDate = LocalDateTime.now();
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

    public void setPaymentStatus(String paymentStatus) {
        if (!isValidPaymentStatus(paymentStatus)) {
            throw new IllegalArgumentException("Invalid payment status: " + paymentStatus);
        }
        this.paymentStatus = paymentStatus;
    }

    private boolean isValidPaymentStatus(String paymentStatus) {
        return paymentStatus.equals("PENDING")
                || paymentStatus.equals("PAID")
                || paymentStatus.equals("FAILED")
                || paymentStatus.equals("REFUNDED");
    }
}
