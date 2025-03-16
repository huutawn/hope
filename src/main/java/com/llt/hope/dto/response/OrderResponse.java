package com.llt.hope.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    Long id;
    String buyer;
    LocalDateTime orderDate;
    BigDecimal totalAmount;
    String shippingAddress;
    String status;
    String paymentMethod;
    String paymentStatus;
}
