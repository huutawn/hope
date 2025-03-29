package com.llt.hope.dto.request;

import java.util.List;

import jakarta.validation.constraints.*;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreationRequest {

    private String buyerId;

    private String paymentMethod;
    private List<OrderItemCreationRequest> items;
    private String notes;
}
