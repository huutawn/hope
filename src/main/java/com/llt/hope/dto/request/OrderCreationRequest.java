package com.llt.hope.dto.request;


import com.llt.hope.entity.Order;
import jakarta.validation.constraints.*;

import lombok.*;

import java.util.List;

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
