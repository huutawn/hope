package com.llt.hope.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderCreationRequest {

    @NotNull
    private Long buyerId;

    @NotNull
    private BigDecimal totalAmount;

    @NotBlank
    private String shippingAddress;

    private String shippingCity;
    private String shippingState;
    private String shippingPostalCode;
    private String shippingCountry= "VietName";

    @NotBlank
    String paymentMethod;
}
