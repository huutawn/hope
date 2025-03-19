package com.llt.hope.dto.response;

import java.time.LocalDateTime;

import com.llt.hope.entity.Order;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private Long paymentId;
    private Order orderId;
    private String paymentMethod;
    private String paymentStatus;
    private LocalDateTime paymentDate;
}
