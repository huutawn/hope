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
public class ProductResponse {
    Long id;
    String name;
    BigDecimal price;
    String description;
    String category;
    Long categoryId;
    Long sellerId;
    Long inventory;
    Boolean isFeatured;
    String imageUrl;
    LocalDateTime createdAt;
}
