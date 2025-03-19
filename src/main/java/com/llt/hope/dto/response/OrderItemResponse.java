package com.llt.hope.dto.response;

import java.math.BigDecimal;

import com.llt.hope.entity.Order;
import com.llt.hope.entity.Product;

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
    private Order orderId;
    private Product productId;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}
