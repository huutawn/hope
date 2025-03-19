package com.llt.hope.dto.request;

import java.math.BigDecimal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderItemCreationRequest {
    private Long orderId;

    private Long productId;

    private int quantity;

    private BigDecimal price;
}
