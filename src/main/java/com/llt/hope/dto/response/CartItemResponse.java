package com.llt.hope.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.llt.hope.entity.Product;
import com.llt.hope.entity.User;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    Long id;
    User userId;
    Product productId;
    Long quantity;
    BigDecimal totalPrice;
    LocalDateTime addedAt;
}
