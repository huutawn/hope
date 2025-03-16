package com.llt.hope.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "Shipments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    String trackingNumber;
    String carrier;
    LocalDateTime shippingDate;
    LocalDate estimatedDeliveryDate;
    private LocalDateTime actualDeliveryDate;

    String status = "PROCESSING";

    public void setStatus(String status) {
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }
        this.status = status;
    }

    private boolean isValidStatus(String status) {
        return status.equals("PROCESSING")
                || status.equals("in_transit")
                || status.equals("DELIVERED")
                || status.equals("failed");
    }
}
