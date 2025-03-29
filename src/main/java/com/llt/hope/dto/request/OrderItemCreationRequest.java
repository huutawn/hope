package com.llt.hope.dto.request;

import jakarta.validation.constraints.Min;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderItemCreationRequest {

    private Long productId;

    @Min(value = 1)
    private int quantity;
}
