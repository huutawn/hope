package com.llt.hope.dto.request;

import jakarta.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentUpdateRequest {
    @NotNull
    private String paymentStatus;
}
