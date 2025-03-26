package com.llt.hope.dto.request;

import java.math.BigDecimal;

import com.llt.hope.entity.Product;
import jakarta.validation.constraints.*;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreationRequest {
    private String buyerId;

    private Long productId;

    private String paymentMethod;

    private String notes;
}
