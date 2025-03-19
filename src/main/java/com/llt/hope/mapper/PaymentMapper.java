package com.llt.hope.mapper;


import com.llt.hope.dto.request.PaymentCreationRequest;
import com.llt.hope.dto.response.PaymentResponse;
import com.llt.hope.entity.Payment;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface PaymentMapper {

    Payment  toPayment(PaymentCreationRequest request);
    PaymentResponse toPaymentResponse(Payment payment);
}
