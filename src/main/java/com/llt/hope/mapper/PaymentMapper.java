package com.llt.hope.mapper;

import org.mapstruct.Mapper;

import com.llt.hope.dto.request.PaymentCreationRequest;
import com.llt.hope.dto.response.PaymentResponse;
import com.llt.hope.entity.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    Payment toPayment(PaymentCreationRequest request);

    PaymentResponse toPaymentResponse(Payment payment);
}
