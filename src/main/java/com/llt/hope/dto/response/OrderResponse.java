package com.llt.hope.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.llt.hope.entity.Product;
import com.llt.hope.entity.User;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long orderId;
    private User buyerId;
    private Product productId;
    private LocalDateTime createdAt;
    private String status;
    private String paymentMethod;
    private String paymentStatus;
    private String notes;
}
