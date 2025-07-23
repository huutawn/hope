package com.llt.hope.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    User buyer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(nullable = false)
    LocalDateTime updatedAt;

    BigDecimal totalAmount;

    @Column(nullable = false, length = 20)
    String status = "PENDING"; // Trạng thái mặc định

    @Column(nullable = false, length = 20)
    String paymentMethod;

    @Column(nullable = false, length = 20)
    String paymentStatus = "PENDING";

    String notes;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Danh sách giá trị hợp lệ
    public static final String[] VALID_STATUSES = {
        "PENDING", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED", "REFUNDED"
    };
    public static final String[] VALID_PAYMENT_METHODS = {"COD", "BANK_TRANSFER", "CREDIT_CARD", "E_WALLET"};
    public static final String[] VALID_PAYMENT_STATUSES = {"PENDING", "PAID", "FAILED", "REFUNDED"};

    // Kiểm tra trạng thái hợp lệ
    public void setStatus(String status) {
        if (!isValidValue(status, VALID_STATUSES)) {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }
        this.status = status;
    }

    public void setPaymentMethod(String paymentMethod) {
        if (!isValidValue(paymentMethod, VALID_PAYMENT_METHODS)) {
            throw new IllegalArgumentException("Invalid payment method: " + paymentMethod);
        }
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentStatus(String paymentStatus) {
        if (!isValidValue(paymentStatus, VALID_PAYMENT_STATUSES)) {
            throw new IllegalArgumentException("Invalid payment status: " + paymentStatus);
        }
        this.paymentStatus = paymentStatus;
    }

    private boolean isValidValue(String value, String[] validValues) {
        for (String v : validValues) {
            if (v.equalsIgnoreCase(value)) return true;
        }
        return false;
    }
}
