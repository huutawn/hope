package com.llt.hope.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreationRequest {
    private String buyerId;

    @DecimalMin(value = "0.0", inclusive = false, message = "Tổng tiền phải lớn hơn 0")
    private BigDecimal totalAmount;

    private String paymentMethod;

    private String notes;
}
