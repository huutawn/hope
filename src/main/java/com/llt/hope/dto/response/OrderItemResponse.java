package com.llt.hope.dto.response;

import java.math.BigDecimal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderItemResponse {
    private Long itemId;
    private Long orderId;
    private Long productId;
    String productName;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}
