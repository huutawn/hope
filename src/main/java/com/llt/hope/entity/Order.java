package com.llt.hope.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "Orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @Column(updatable = false)
    private LocalDateTime orderDate = LocalDateTime.now();

    @NotBlank
    String status = "PENDING";

    @NotNull
    private BigDecimal totalAmount;

    private BigDecimal shippingFee = BigDecimal.ZERO;
    private BigDecimal taxAmount = BigDecimal.ZERO;
    private BigDecimal grandTotal;

    @NotBlank
    private String shippingAddress;

    private String shippingCity;
    private String shippingState;
    private String shippingPostalCode;
    private String shippingCountry = "Vietnam";

    @NotBlank
    private String paymentMethod;

    @NotBlank
    private String paymentStatus = "PENDING";

    private String notes;

    public void setStatus(String status) {
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }
        this.status = status;
    }

    private boolean isValidStatus(String status) {
        return status.equals("PENDING")
                || status.equals("PROCESSING")
                || status.equals("SHIPPED")
                || status.equals("DELIVERED")
                || status.equals("CANCELLED")
                || status.equals("REFUNDED");
    }

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
